package bobzone.hrf.humanresource.Fragments.Customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import bobzone.hrf.humanresource.Core.Base.Activity.FormActivity
import bobzone.hrf.humanresource.Core.Base.Callback.CreationResponseCallBack
import bobzone.hrf.humanresource.Core.Base.Form.*
import bobzone.hrf.humanresource.Core.Base.Loading.LoadingDelegate
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Core.Model.CreationResponse
import bobzone.hrf.humanresource.Model.Address.AddressData
import bobzone.hrf.humanresource.Model.Customer.CustomerData
import bobzone.hrf.humanresource.Model.Customer.CustomerGroupData
import bobzone.hrf.humanresource.Model.DynamicLink.DynamicLinkData
import bobzone.hrf.humanresource.Model.Territory.TerritoryData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.list_customer.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by davidwibisono on 2/14/18.
 */

open class AddCustomer : FormActivity() {

    var fullNameForm = BasicInput("Fullname","customer_name")
    var customerGroupForm = APIBasicSpinner<CustomerGroupData>("Customer Group","customer_group")
    var territoryForm = APIBasicSpinner<TerritoryData>("Territory","territory")
    var customerType = BasicSpinner("Type","customer_type", arrayListOf("Company","Individual"))
    var locationForm = IntentBasicInput("Location","location")
    var address = AddressData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.add_customer)
        fullNameForm.isRequired = true
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
        formBuilder.formSheets.add(territoryForm)

        formBuilder.formSheets.add(customerType)
        locationForm.activity = this
        locationForm.intent = Intent(applicationContext,CustomerLocation::class.java)
        locationForm.intentListener = object :FormIntentListener {
            override fun prepareIntent(i: Intent): Intent {
                return i
            }

            override fun onIntentResult(data: Intent):String {
                val street = data.getStringExtra("route")
                val city = data.getStringExtra("administrative_area_level_2")
                val province = data.getStringExtra("administrative_area_level_1")
                val country = data.getStringExtra("country")
                val pincode = data.getStringExtra("postal_code")
                val latitude = data.getStringExtra("latitude")
                val longitude = data.getStringExtra("longitude")
                address.address_line1 = street
                address.city = city
                address.state = province
                address.country = country
                address.pincode = pincode
                address.latitude = latitude
                address.longitude = longitude


                return "$street, $city, $province - $country"
            }

            override fun getField():String {
                return ""
            }
        }
        formBuilder.formSheets.add(locationForm)

        formBuilder.build()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        formBuilder.onResult(resultCode,requestCode,data)
    }


    override fun requestDidResponseError(errorCode: Int) {
        Log.e("asd", "$errorCode")
    }

    override fun submitDidSuccess(creationResponse: CreationResponse) {

        if (!address.address_line1.equals("")) {
            var json = creationResponse.data
            var customer_name = json.get("customer_name").asString
            setLoadingText("Linking new address to $customer_name")
            val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
            val api = Helper.instance.getAPIWithCookie(applicationContext, cookieJar)
            val dynamicLink = DynamicLinkData()
            dynamicLink.link_doctype = "Customer"
            dynamicLink.link_name = customer_name
            address.links.add(dynamicLink)
            val callAddress = api.submitAddress(address)
            trySubmitAddress(callAddress)
        } else {
            setResult(Activity.RESULT_OK)
            finish()
        }


    }

    fun trySubmitAddress(callAddress:Call<CreationResponse>) {
        startLoading()
        callAddress.enqueue(object :CreationResponseCallBack(applicationContext) {
            override fun responseData(data: JsonObject) {
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun failed(error: Throwable) {
                trySubmitAddress(callAddress)
            }
        })
    }


    override fun getCallSubmit(jsonObject: JsonObject): Call<CreationResponse> {
        setLoadingText("Adding new customer")
        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext,cookieJar)
        return api.submitCustomer(jsonObject)
    }

    override fun validating(): Boolean {
        return super.validating()
    }


}