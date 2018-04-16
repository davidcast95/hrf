package bobzone.hrf.humanresource.Core.Base.Form

import bobzone.hrf.humanresource.Core.Model.CreationResponse
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call

/**
 * Created by davidwibisono on 2/15/18.
 */

open interface FormEventListener {
    fun getCallFetch():Call<JsonObject>?
    fun getCallSubmit(jsonObject: JsonObject): Call<CreationResponse>?
    fun getCallUpdate(jsonObject: JsonObject): Call<CreationResponse>?
    fun submitDidSuccess(creationResponse:CreationResponse)
    fun fetchDidSuccess(fetchResponse: JsonObject)
    fun requestDidResponseError(errorCode:Int)
    fun requestDidFailed(t:Throwable?)
    fun requestDidNullResponse()
    fun validating() : Boolean

}