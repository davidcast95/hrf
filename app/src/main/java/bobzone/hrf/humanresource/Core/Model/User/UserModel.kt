package bobzone.hrf.humanresource.Model.User

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import bobzone.hrf.humanresource.Core.Global.Constant
import bobzone.hrf.humanresource.Core.Global.MyCookieJar
import bobzone.hrf.humanresource.Core.Helper.Helper
import com.google.gson.Gson


/**
 * Created by davidwibisono on 12/23/17.
 */

class UserModel private constructor() {
    private object Holder { val INSTANCE = UserModel() }

    var email = "USER_MODEL_EMAIL"
    var fullname = "USER_MODEL_FULLNAME"


    companion object {
        val instance: UserModel by lazy { Holder.INSTANCE }
    }

    fun getUserPreferencesAsBoolean(context: Context, key:String, default:Boolean):Boolean {
        val preferences = context.getSharedPreferences(Constant.instance.SHARED_USER_PREFERENCES, Context.MODE_PRIVATE)
        val res = preferences.getBoolean(key, default)
        return res
    }
    fun setUserPreferencesAsBoolean(context: Context, key:String, value:Boolean) {
        val preferences = context.getSharedPreferences(Constant.instance.SHARED_USER_PREFERENCES, Context.MODE_PRIVATE)
        val ed = preferences.edit()
        ed.putBoolean(key,value)
        ed.commit()
    }

    fun destroy(context: Context) {
        val preferences = context.getSharedPreferences(Constant.instance.SHARED_PREF_GLOBAL_MODEL, Context.MODE_PRIVATE)
        val ed = preferences.edit();
        ed.putString(Constant.instance.GLOBAL_MODEL_COOKIE_JAR, null)
        ed.commit()
    }

    fun saveCookieJar(cookieJar: MyCookieJar, context: Context) {
        val preferences = context.getSharedPreferences(Constant.instance.SHARED_PREF_GLOBAL_MODEL, Context.MODE_PRIVATE)

        val json = Helper.instance.convertJsonToString(cookieJar)
        val ed = preferences.edit();
        ed.putString(Constant.instance.GLOBAL_MODEL_COOKIE_JAR, json);
        ed.commit()
    }
    fun loadCookieJar(context: Context) : MyCookieJar? {
        val preferences = context.getSharedPreferences(Constant.instance.SHARED_PREF_GLOBAL_MODEL, Context.MODE_PRIVATE)
        val cookieJson = preferences.getString(Constant.instance.GLOBAL_MODEL_COOKIE_JAR, "")
        if (cookieJson == "") return null
        val gson = Gson()
        var cookieJar: MyCookieJar? = gson.fromJson<MyCookieJar>(cookieJson, MyCookieJar::class.java)
        return cookieJar
    }
    fun saveString(string: String, key:String, context: Context) {
        val preferences = context.getSharedPreferences(Constant.instance.SHARED_PREF_GLOBAL_MODEL, Context.MODE_PRIVATE)
        val ed = preferences.edit();
        ed.putString(key, string);
        ed.commit()
    }
    fun loadString(key:String, context: Context) : String {
        val preferences = context.getSharedPreferences(Constant.instance.SHARED_PREF_GLOBAL_MODEL, Context.MODE_PRIVATE)
        val string = preferences.getString(key, "")
        return string
    }

}