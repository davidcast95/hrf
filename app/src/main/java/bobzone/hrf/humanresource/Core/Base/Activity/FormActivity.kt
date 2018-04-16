package bobzone.hrf.humanresource.Core.Base.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import bobzone.hrf.humanresource.Core.Base.Form.FormBuilder
import bobzone.hrf.humanresource.Core.Base.Form.FormEventListener
import bobzone.hrf.humanresource.Core.Base.Loading.LoadingEventListener
import bobzone.hrf.humanresource.Core.Base.Loading.LoadingHandler
import bobzone.hrf.humanresource.Core.Model.CreationResponse
import bobzone.hrf.humanresource.R
import com.google.gson.JsonObject
import retrofit2.Call

/**
 * Created by davidwibisono on 2/15/18.
 */

open class FormActivity : LoadingHandler(), FormEventListener, LoadingEventListener {


    open var formBuilder: FormBuilder = FormBuilder()
    open var successAlert = "Data has been created"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        formBuilder.setup(applicationContext, this)
        formBuilder.listener = this

        loading = findViewById<RelativeLayout>(R.id.loading_view)
        loadingIndicator = findViewById<TextView>(R.id.loading_indicator_text)
        loadingEventListener = this

    }

    override fun getOptionsMenu(): Int? {
        return R.menu.menu_submit
    }

    override fun optionsMenuDidSelected(id: Int): Boolean {
        when (id) {
            R.id.submit -> {
                if (formBuilder.validate())
                    startLoading()
                formBuilder.submit(false)
                return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        formBuilder.onResult(resultCode,requestCode,data)
    }

    override fun getCallSubmit(jsonObject: JsonObject): Call<CreationResponse>? {
        return null
    }

    override fun getCallUpdate(jsonObject: JsonObject): Call<CreationResponse>? {
        return null
    }

    override fun getCallFetch(): Call<JsonObject>? {
        return null
    }

    override fun fetchDidSuccess(fetchResponse: JsonObject) {

    }

    override fun submitDidSuccess(creationResponse: CreationResponse) {
        Toast.makeText(applicationContext, successAlert, Toast.LENGTH_LONG).show()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun requestDidResponseError(errorCode: Int) {

    }

    override fun requestDidNullResponse() {

    }

    override fun requestDidFailed(t: Throwable?) {

    }
    override fun loadingDidStart() {
        formBuilder.disabled()
    }

    override fun loadingDidStop() {
        formBuilder.enabled()
    }

    override fun validating() :Boolean {
        return true
    }
}