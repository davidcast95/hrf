package bobzone.hrf.humanresource

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import bobzone.hrf.humanresource.Core.Base.Login.LoginHandler
import bobzone.hrf.humanresource.Core.Global.AppConfiguration
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.Model.User.UserResponse
import retrofit2.Response


class LoginActivity : LoginHandler() {

    var usernameET:EditText ? = null
    var passwordET:EditText? = null
    var loginButton:TextView? = null
    var loading:RelativeLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)
        super.onCreate(savedInstanceState)

        usernameET = findViewById<EditText>(R.id.username)
        passwordET = findViewById<EditText>(R.id.password)
        loginButton = findViewById<TextView>(R.id.login_button)
        loading = findViewById<RelativeLayout>(R.id.loading_view)
        val _loading = loading
        if (_loading is RelativeLayout) {
            _loading.visibility = View.GONE
        }

        handler()

    }

    override fun successLogin(userResponse: Response<UserResponse>) {
        super.successLogin(userResponse)
        val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }
    override fun errorLogin(errorCode: Int) {
        val _loading = loading
        if (_loading is RelativeLayout) {
            _loading.visibility = View.GONE
        }
        Toast.makeText(applicationContext,R.string.invalid_username_or_password, Toast.LENGTH_SHORT).show()
    }

    fun handler() {
        val _loginButton = loginButton
        if (_loginButton is TextView) {
            _loginButton.setOnClickListener {
                doLogin()
            }
        }
    }

    fun doLogin() {
        val _usernameET = usernameET
        val _passwordET = passwordET

        val productionMode = findViewById<Switch>(R.id.production_mode)
        if (productionMode.isChecked) {
            AppConfiguration.instance.BASE_URL = AppConfiguration.instance.BASE_PROD_URL
        } else {
            AppConfiguration.instance.BASE_URL = AppConfiguration.instance.BASE_DEV_URL
        }

        val _loading = loading
        if (_loading is RelativeLayout) {
            _loading.visibility = View.VISIBLE
        }
        if (_usernameET is EditText && _passwordET is EditText) {
            UserModel.instance.email = _usernameET.text.toString()
            val usr = _usernameET.text.toString()
            val pwd = _passwordET.text.toString()
            doLogin(usr,pwd)
        }
    }

}
