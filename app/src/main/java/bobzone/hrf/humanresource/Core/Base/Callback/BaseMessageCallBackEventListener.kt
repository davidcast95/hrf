package bobzone.hrf.humanresource.Core.Base.Callback

import bobzone.hrf.humanresource.Model.BaseMessage

/**
 * Created by davidwibisono on 3/8/18.
 */
open interface BaseMessageCallBackEventListener<T:Any> {
    fun responseData(data:MutableList<T>)
    fun failed(error:Throwable)
}