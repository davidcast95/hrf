package bobzone.hrf.humanresource.Core.Base.Callback

import com.google.gson.JsonObject

/**
 * Created by davidwibisono on 3/8/18.
 */
open interface CreationResponseCallBackEventListener{
    fun responseData(data:JsonObject)
    fun failed(error:Throwable)
}