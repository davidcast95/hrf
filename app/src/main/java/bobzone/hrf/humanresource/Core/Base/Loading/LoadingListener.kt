package bobzone.hrf.humanresource.Core.Base.Loading

/**
 * Created by davidwibisono on 12/24/17.
 */

interface LoadingListener {
    fun setLoading(view:Boolean)
    fun hasLoading() : Boolean
    fun startLoading()
    fun stopLoading()
    fun setLoadingText(text:String)
}