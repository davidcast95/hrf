package bobzone.hrf.humanresource.Core.Base.Loading

/**
 * Created by davidwibisono on 3/9/18.
 */
open abstract interface LoadingEventListener {
    fun loadingDidStart()
    fun loadingDidStop()
}