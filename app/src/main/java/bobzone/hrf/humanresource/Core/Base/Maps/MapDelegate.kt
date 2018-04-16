package bobzone.hrf.humanresource.Core.Base.Maps

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentActivity
import bobzone.hrf.humanresource.Core.Base.Location.LocationServicesDelegate
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Created by davidwibisono on 2/16/18.
 */
open class MapDelegate : OnMapReadyCallback {

    var markers = mutableListOf<Marker>()
    var gMap:GoogleMap? = null
    var mapEventListener:MapEventListener? = null
    var initializeLocation:LatLng? = null

    //settings
    var isMyLocationEnabled = true

    fun setup(idMap:Int, fragmentActivity:FragmentActivity, context: Context) {
        val mapFragment = fragmentActivity.supportFragmentManager.findFragmentById(idMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        gMap = p0
        val ml = mapEventListener
        val gm = gMap
        if (ml is MapEventListener && gm is GoogleMap) {
            ml.googleMapReady(gm)
            val loc = initializeLocation
            if (loc is LatLng) {
                gm.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,5f))
            }
            gm.isMyLocationEnabled = isMyLocationEnabled
        }
    }

    fun moveCamera(loc:LatLng, zoom:Float, animate:Boolean) {
        val gm = gMap
        if (gm is GoogleMap) {
            if (animate) {
                gm.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,zoom))
            } else {
                gm.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,zoom))
            }
        }
    }

    fun addMarker(loc:LatLng, title:String, subtitle:String) {
        val gm = gMap
        if (gm is GoogleMap) {
            val newMarker = gm.addMarker(MarkerOptions().position(loc).title(title).snippet(subtitle))
            newMarker.showInfoWindow()
            markers.add(newMarker)
            gm.uiSettings.isMapToolbarEnabled = true

        }
    }


}