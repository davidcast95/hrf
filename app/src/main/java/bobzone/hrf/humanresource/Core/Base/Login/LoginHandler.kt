package bobzone.hrf.humanresource.Core.Base.Login

import android.util.Log
import bobzone.hrf.humanresource.Core.Base.Activity.BaseActivity
import bobzone.hrf.humanresource.Core.Base.Loading.LoadingHandler
import bobzone.hrf.humanresource.Core.Global.MyCookieJar
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.Model.User.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by davidwibisono on 12/23/17.
 */

open class LoginHandler : BaseActivity(), LoginListener {

    override fun failureNetwork(call: Call<UserResponse>, throwable: Throwable) {
        Log.e("LOGINHANDLER",throwable.localizedMessage,throwable)
        Helper.instance.showConnectivityUnstable(this)
    }

    override fun errorLogin(errorCode: Int) {

    }

    override fun successLogin(userResponse: Response<UserResponse>) {

    }

    fun doLogin(usr: String, pwd: String) {
        var cookieJar = MyCookieJar()
        val api = Helper.instance.getAPIWithCookie(applicationContext, cookieJar)
        val enqueueLogin = api.loginUser(usr,pwd,"mobile")
        val thisActivity = this
//        startLoading()
        enqueueLogin.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.code() == 200) {
                    UserModel.instance.saveCookieJar(cookieJar, thisActivity)
                    UserModel.instance.saveString(usr,UserModel.instance.email,applicationContext)
                    val userResponse = response.body()
                    if (userResponse != null)
                        UserModel.instance.saveString(userResponse.full_name,UserModel.instance.fullname,applicationContext)
                    successLogin(response)
                } else {
//                    stopLoading()
                    errorLogin(response.code())
                }
            }

            override fun onFailure(call: Call<UserResponse>, throwable: Throwable) {
//                stopLoading()
                failureNetwork(call,throwable)
            }
        })
    }

}