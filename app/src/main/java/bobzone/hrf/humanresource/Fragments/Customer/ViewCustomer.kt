package bobzone.hrf.humanresource.Fragments.Customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import bobzone.hrf.humanresource.Core.Base.Activity.FormActivity
import bobzone.hrf.humanresource.Core.Base.Callback.BaseResponseCallBack
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleCallBack
import bobzone.hrf.humanresource.Core.Base.Callback.CreationResponseCallBack
import bobzone.hrf.humanresource.Core.Base.Form.*
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Core.Model.CreationResponse
import bobzone.hrf.humanresource.Helper.APIHelper
import bobzone.hrf.humanresource.Model.Address.AddressData
import bobzone.hrf.humanresource.Model.Address.AddressFetchData
import bobzone.hrf.humanresource.Model.Customer.CustomerData
import bobzone.hrf.humanresource.Model.Customer.CustomerGroupData
import bobzone.hrf.humanresource.Model.Territory.TerritoryData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call

/**
 * Created by davidwibisono on 3/6/18.
 */
open class ViewCustomer : FormActivity() {

    var fullNameForm = BasicInput("Fullname","customer_name")
    var customerGroupForm = APIBasicSpinner<CustomerGroupData>("Customer Group","customer_group")
    var territoryForm = APIBasicSpinner<TerritoryData>("Territory","territory")
    var customerType = BasicSpinner("Type","customer_type", arrayListOf("Company","Individual"))
    var locationForm = IntentBasicInput("Location","location")
    var addresses = mutableListOf<AddressFetchData>()
    var address = AddressData()

    var customer = CustomerData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.view_customer)
        formBuilder.editMode = true

        fullNameForm.isRequired = true
        fullNameForm.isReadOnly = true
        formBuilder.formSheets.add(fullNameForm)

        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext, cookieJar)
        val customerGroupCall = api.getCustomerGroup()
        customerGroupForm.call = customerGroupCall
        customerGroupForm.listener = object : APIBasicSpinnerEventListener<CustomerGroupData> {
            override fun getItem(item: CustomerGroupData): String {
                return item.name
            }

            override fun getField(item: CustomerGroupData): String {
                return item.name
            }

            override fun fetchSuccess(objects: List<CustomerGroupData>) {

            }
        }
        customerGroupForm.isReadOnly = true
        formBuilder.formSheets.add(customerGroupForm)
        val territoryCall = api.getTerritory()
        territoryForm.call = territoryCall
        territoryForm.listener = object : APIBasicSpinnerEventListener<TerritoryData> {
            override fun getItem(item: TerritoryData): String {
                return item.name
            }

            override fun getField(item: TerritoryData): String {
                return item.name
            }

            override fun fetchSuccess(objects: List<TerritoryData>) {

            }
        }
        territoryForm.isReadOnly = true
        formBuilder.formSheets.add(territoryForm)

        customerType.isReadOnly = true
        formBuilder.formSheets.add(customerType)
        locationForm.activity = this
        locationForm.intent = Intent(applicationContext,CustomerLocation::class.java)
        locationForm.intentListener = object :FormIntentListener {
            override fun onIntentResult(data: Intent):String {
                val street = data.getStringExtra("route")
                val city = data.getStringExtra("administrative_area_level_2")
                val province = data.getStringExtra("administrative_area_level_1")
                val country = data.getStringExtra("country")
                val pincode = data.getStringExtra("postal_code")
                address.address_line1 = street
                address.city = city
                address.state = province
                address.country = country
                address.pincode = pincode

                return "$street, $city, $province - $country"
            }

            override fun getField():String {
                return ""
            }

            override fun prepareIntent(i:Intent): Intent {
                if (addresses.size > 0) {
                    val currentAddress = addresses[0]
                    val json = Gson().toJson(currentAddress)
                    i.putExtra("address",json)
                    val custJson = Gson().toJson(customer)
                    i.putExtra("customer",custJson)
                }
                return i
            }
        }
        formBuilder.formSheets.add(locationForm)

        val customer_name = intent.getStringExtra("customer_name")
        fetchCustomer(customer_name)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        formBuilder.onResult(resultCode,requestCode,data)
    }


    override fun requestDidResponseError(errorCode: Int) {
        Log.e("asd", "$errorCode")
    }


    fun trySubmitAddress(callAddress:Call<CreationResponse>) {
        startLoading()
        callAddress.enqueue(object : CreationResponseCallBack(applicationContext) {
            override fun responseData(data: JsonObject) {
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun failed(error: Throwable) {
                trySubmitAddress(callAddress)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return false
    }


    override fun getCallUpdate(jsonObject: JsonObject): Call<CreationResponse>? {
        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext,cookieJar)
        return api.updateCustomer(jsonObject)
    }


    fun fetchCustomer(customer_name:String) {
        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext,cookieJar)
        val call = api.getSpecifiedCustomer(customer_name)
        call.enqueue(object : BaseSingleCallBack<CustomerData>(applicationContext) {
            override fun responseData(data: CustomerData) {
                customer = data
                fullNameForm.field = customer.name
                customerGroupForm.selectedField = customer.customer_group
                territoryForm.selectedField = customer.territory
                customerType.selectedField = customer.customer_type
                fetchAddress(customer_name,api)
            }

            override fun failed(error: Throwable) {

            }
        })
    }

    fun fetchAddress(customer_name: String, api:APIHelper) {
        val call = api.getAddress("[[\"Address\",\"name\",\"like\",\"%$customer_name%\"]]")
        call.enqueue(object : BaseResponseCallBack<AddressFetchData>(applicationContext){
            override fun responseData(data: MutableList<AddressFetchData>) {

                addresses = data
                if (addresses.size > 0) {
                    var currentAddress = addresses[0]

                    locationForm.field = "${currentAddress.address_line1}, ${currentAddress.city} - ${currentAddress.country}"
                } else {
                    locationForm.isReadOnly = true
                    locationForm.field = "Location is not provided"
                }
                formBuilder.build()
            }

            override fun failed(error: Throwable) {

            }
        })
    }

    override fun validating():Boolean {
        return super.validating()
    }


}