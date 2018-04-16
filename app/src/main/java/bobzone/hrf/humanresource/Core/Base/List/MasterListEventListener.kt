package bobzone.hrf.humanresource.Core.Base.List

import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import retrofit2.Call
import retrofit2.Response
import java.text.FieldPosition

/**
 * Created by davidwibisono on 2/13/18.
 */

open interface MasterListEventListener<T : Any> {

    fun willRefreshItems()
    fun fetchDidSuccess(data:MutableList<T>)
    fun getAPIHandlingOnBaseResponse(query:String, page:Int): Call<BaseResponse<T>>?
    fun getAPIHandlingOnBaseMessage(query:String, page:Int): Call<BaseMessage<T>>?
    fun onListTapped(item:T,position: Int)
}