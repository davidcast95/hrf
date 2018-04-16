package bobzone.hrf.humanresource.Core.Base.Form

/**
 * Created by davidwibisono on 2/15/18.
 */

open interface APIBasicSpinnerEventListener<T : Any> {
    fun getItem(item:T):String
    fun getField(item:T):String
    fun fetchSuccess(objects:List<T>)
}