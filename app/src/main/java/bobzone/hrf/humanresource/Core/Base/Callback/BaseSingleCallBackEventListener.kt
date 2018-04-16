package bobzone.hrf.humanresource.Core.Base.Callback

/**
 * Created by davidwibisono on 3/8/18.
 */
open interface BaseSingleCallBackEventListener<T:Any> {
    fun responseData(data:T)
    fun failed(error:Throwable)
}