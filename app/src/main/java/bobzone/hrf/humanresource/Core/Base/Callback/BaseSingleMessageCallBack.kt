package bobzone.hrf.humanresource.Core.Base.Callback

import android.content.Context
import android.util.Log
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.BaseSingleMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by davidwibisono on 3/8/18.
 */
open abstract class BaseSingleMessageCallBack<T:Any>(context:Context) : Callback<BaseSingleMessage<T>>, BaseSingleCallBackEventListener<T> {
    var context:Context? = null

    init {
        this.context = context
    }
    override fun onFailure(call: Call<BaseSingleMessage<T>>?, t: Throwable?) {
        val c = context
        if (c is Context) {
            Helper.instance.showConnectivityUnstable(c)
        }
        val throwable = t
        if (throwable is Throwable) {
            failed(throwable)

            val cal = call
            if (cal != null) {
                Log.e("API LOG", "URL FROM BaseSingleMessageCallBack")
                Log.e("API LOG", cal.request().url().toString())
                Log.e("API LOG", throwable.localizedMessage)
            }
        }
    }

    override fun onResponse(call: Call<BaseSingleMessage<T>>?, response: Response<BaseSingleMessage<T>>?) {
        val res = response
        if (res is Response<BaseSingleMessage<T>>) {
            val body = res.body()
            if (body is BaseSingleMessage<T>) {
                val data = body.data
                if (data is T)
                    responseData(data)
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