package bobzone.hrf.humanresource.Core.Base.Callback

/**
 * Created by davidwibisono on 3/8/18.
 */
open interface BaseResponseCallBackEventListener<T:Any> {
    fun responseData(data:MutableList<T>)
    fun failed(error:Throwable)
}