package bobzone.hrf.humanresource.Core.Base.List

import bobzone.hrf.humanresource.Model.BaseResponse
import retrofit2.Call

/**
 * Created by davidwibisono on 2/13/18.
 */

open interface BasicListEventListener<T : Any> {

    fun getAPIHandlingOn(): Call<BaseResponse<T>>?
}