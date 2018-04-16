package bobzone.hrf.humanresource.Fragments.Customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import bobzone.hrf.humanresource.Core.Base.Activity.BaseActivity
import bobzone.hrf.humanresource.Core.Base.Form.BasicInput
import bobzone.hrf.humanresource.Core.Base.Form.FormBuilder
import bobzone.hrf.humanresource.Core.Base.GoogleMap.GoogleMapsPlacesDelegate
import bobzone.hrf.humanresource.Core.Base.GoogleMap.GoogleMapsPlacesListener
import bobzone.hrf.humanresource.Core.Base.GoogleMap.GoogleMapsRoadsDelegate
import bobzone.hrf.humanresource.Core.Base.GoogleMap.GoogleMapsRoadsListener
import bobzone.hrf.humanresource.Core.Base.Location.LocationServicesDelegate
import bobzone.hrf.humanresource.Core.Base.Location.LocationServicesListener
import bobzone.hrf.humanresource.Core.Base.Maps.MapDelegate
import bobzone.hrf.humanresource.Core.Base.Maps.MapEventListener
import bobzone.hrf.humanresource.Core.Model.GoogleMap.Places.GoogleMapsPlaceDetailsData
import bobzone.hrf.humanresource.Core.Model.GoogleMap.Places.GoogleMapsPlacesData
import bobzone.hrf.humanresource.Core.Model.GoogleMap.Roads.GoogleMapRoadsData
import bobzone.hrf.humanresource.Model.Address.AddressFetchData
import bobzone.hrf.humanresource.Model.Customer.CustomerData
import bobzone.hrf.humanresource.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_customer_location.*

/**
 * Created by davidwibisono on 2/16/18.
 */

open class CustomerLocation : BaseActivity(), LocationServicesListener, GoogleMapsRoadsListener, GoogleMapsPlacesListener {
    var locationServicesDelegate = LocationServicesDelegate()
    var mapDelegate = MapDelegate()
    var fetching = true
    var gMapRoadsDelegate = GoogleMapsRoadsDelegate()
    var gMapPlacesDelegate = GoogleMapsPlacesDelegate()
    var lat = 0.0
    var lng = 0.0
    val zoom = 18f

    var formBuilder = FormBuilder()
    val formFields = arrayListOf<String>("phone","route","administrative_area_level_2","administrative_area_level_1","country","postal_code")
    val formFieldNames = arrayListOf<String>("Phone", "Street","Kota","Provinsi","Negara","Kode Pos")
    val currentFormFields = arrayListOf<String>("","","","","","","")

    var address = AddressFetchData()
    var customer = CustomerData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_location)
        setTitle(R.string.customer_location)



        mapDelegate.setup(R.id.map,this,applicationContext)


        locationServicesDelegate.setup(mapDelegate, applicationContext,this)
        locationServicesDelegate.listener = this

        gMapRoadsDelegate.setup(applicationContext)
        gMapRoadsDelegate.listener = this

        gMapPlacesDelegate.setup(applicationContext)
        gMapPlacesDelegate.listener = this

        formBuilder.setup(applicationContext,this)


        mapDelegate.isMyLocationEnabled = true
        val json = intent.getStringExtra("address")
        if (json != null) {
            mapDelegate.isMyLocationEnabled = false
            address = Gson().fromJson(json,AddressFetchData::class.java)
            currentFormFields[0] = address.phone
            currentFormFields[1] = address.address_line1
            currentFormFields[2] = address.city
            currentFormFields[3] = address.state
            currentFormFields[4] = address.country
            currentFormFields[5] = address.pincode
            fetching = false
            locationServicesDelegate.navigationMode = false

            val custJson = intent.getStringExtra("customer")
            if (custJson != null) {
                customer = Gson().fromJson(custJson,CustomerData::class.java)
            }
            val latitude = address.latitude
            val longitude = address.longitude

            if (!latitude.equals("") && !longitude.equals("")) {
                lat = latitude.toDouble()
                lng = longitude.toDouble()
                mapholder.visibility = View.VISIBLE
            } else {
                mapholder.visibility = View.GONE
            }
        }

        if (formFieldNames.size == formFields.size) {
            for ((i,v) in formFields.withIndex()) {
                val field = BasicInput(formFieldNames[i],v)
                field.isRequired = true
                field.field = currentFormFields[i]
                field.isReadOnly = json != null
                formBuilder.formSheets.add(field)


            }
        }



        formBuilder.build()
    }

    override fun googleMapReady(gm: GoogleMap) {
        if (lat != 0.0 && lng != 0.0) {
            mapDelegate.moveCamera(LatLng(lat, lng), zoom, false)
            mapDelegate.addMarker(LatLng(lat, lng), customer.name, customer.customer_type)
        }
    }

    override fun getOptionsMenu(): Int? {
        return R.menu.menu_submit
    }

    override fun optionsMenuDidSelected(id: Int): Boolean {
        when (id) {
            R.id.submit -> {
                if (formBuilder.validate()) {
                    val newIntent = Intent()
                    formBuilder.formSheets.forEach { fs ->
                        newIntent.putExtra(fs.fieldID,fs.field)
                    }
                    newIntent.putExtra("latitude","$lat")
                    newIntent.putExtra("longitude","$lng")

                    setResult(Activity.RESULT_OK, newIntent)
                    finish()
                    return true
                }
            }
        }
        return false
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        locationServicesDelegate.permissionResult(requestCode,grantResults)
    }

    override fun onLocationChange(lat: Double, lng: Double, gm:GoogleMap) {
        if (fetching) {
            this.lat = lat
            this.lng = lng
            gMapRoadsDelegate.requestNearestRoads(lat,lng)
        }
    }

    override fun onResultNearestRoads(data: List<GoogleMapRoadsData>) {
        if (data.size > 0) {
            val nearest = data[0]
            gMapPlacesDelegate.requestPlaceDetails(nearest.placeId)
        } else {
            gMapPlacesDelegate.requestNearbySearch(lat, lng, "","")
        }
    }

    override fun onResultNearbySearch(data: List<GoogleMapsPlacesData>) {
        if (data.size > 0) {
            val nearest = data[0]
            gMapPlacesDelegate.requestPlaceDetails(nearest.place_id)
        } else {
            Toast.makeText(applicationContext,getString(R.string.cannot_find_any_matching_location),Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResultPlaceDetails(data: GoogleMapsPlaceDetailsData) {
        var index = 0
        data.address_components.forEach { address ->
            for (i in 0 until formFields.size) {
                val field = formFields[i]
                if (address.types.contains(field)) {
                    formBuilder.setText(i, address.long_name)
                    fetching = false
                }
            }
        }
    }
}