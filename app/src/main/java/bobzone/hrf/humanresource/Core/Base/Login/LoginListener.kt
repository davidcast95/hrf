package bobzone.hrf.humanresource.Core.Base.Login

import bobzone.hrf.humanresource.Model.User.UserResponse
import retrofit2.Call
import retrofit2.Response

/**
 * Created by davidwibisono on 12/24/17.
 */
interface LoginListener {
    fun successLogin(response: Response<UserResponse>)
    fun errorLogin(errorCode:Int)
    fun failureNetwork(call: Call<UserResponse>, throwable: Throwable)
}