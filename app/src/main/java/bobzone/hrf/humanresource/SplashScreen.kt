package bobzone.hrf.humanresource

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import bobzone.hrf.humanresource.Core.Global.AppConfiguration
import bobzone.hrf.humanresource.Core.Global.MyCookieJar
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.User.UserModel
import java.util.*


class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity_splash_screen)
        Handler().postDelayed({
            val today = Date().time
            val expired = Helper.instance.stringToDate(AppConfiguration.instance.EXPIRY_APP).time

            if (today < expired || true) {
                val cookieJar = UserModel.instance.loadCookieJar(this)
                if (cookieJar is MyCookieJar) {
                    val mainIntent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                } else {
                    val mainIntent = Intent(this@SplashScreen, LoginActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                }
            } else {
                val mainIntent = Intent(this@SplashScreen, SplashTrial::class.java)
                startActivity(mainIntent)
                finish()
            }
        }, AppConfiguration.instance.SPLASH_SCREEN_INTERVAL_TIME)
    }


}
