package bobzone.hrf.humanresource.Core.Base.Callback

import android.content.Context
import android.util.Log
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Core.Model.CreationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by davidwibisono on 3/8/18.
 */
open abstract class CreationResponseCallBack(context:Context) : Callback<CreationResponse>, CreationResponseCallBackEventListener {
    var context:Context? = null

    init {
        this.context = context
    }
    override fun onFailure(call: Call<CreationResponse>?, t: Throwable?) {
        val c = context
        if (c is Context) {
            Helper.instance.showConnectivityUnstable(c)
        }
        val throwable = t
        if (throwable is Throwable) {
            failed(throwable)

            val cal = call
            if (cal != null) {
                Log.e("API LOG", "URL FROM CreationResponseCallBack")
                Log.e("API LOG", cal.request().url().toString())
                Log.e("API LOG", throwable.localizedMessage)
            }
        }
    }

    override fun onResponse(call: Call<CreationResponse>?, response: Response<CreationResponse>?) {
        val res = response
        if (res is Response<CreationResponse>) {
            val body = res.body()
            if (body is CreationResponse) {
                responseData(body.data)
            } else {
                val c = context
                if (c is Context)
                    Helper.instance.showServerError(c,res.code())
            }
        } else {
            val c = context
            if (c is Context)
                Helper.instance.showServerNotResponding(c)
        }
    }



}