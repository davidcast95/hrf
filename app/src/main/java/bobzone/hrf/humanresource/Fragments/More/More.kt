package bobzone.hrf.humanresource.Fragments.More

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Base.SideMenu.MenuFragment
import bobzone.hrf.humanresource.Core.Global.AppConfiguration
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.Customer.Customer
import bobzone.hrf.humanresource.LoginActivity
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/16/18.
 */
open class More :MenuFragment() {

    override fun getView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.fragment_more,container,false)

        val customerButton = v.findViewById<LinearLayout>(R.id.customer_button)
        customerButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val customerIntent = Intent(context,Customer::class.java)
                startActivity(customerIntent)
            }
        })


        val name = v.findViewById<TextView>(R.id.name)
        val fullname = UserModel.instance.loadString(UserModel.instance.fullname, context)
        name.setText(fullname)
        val email = v.findViewById<TextView>(R.id.email)
        val emaill = UserModel.instance.loadString(UserModel.instance.email, context)
        email.setText(emaill)

        val logout = v.findViewById<Button>(R.id.logout)
        logout.setOnClickListener(object :View.OnClickListener {
            override fun onClick(p0: View?) {
                UserModel.instance.destroy(context)
                val loginIntent = Intent(context, LoginActivity::class.java)
                startActivity(loginIntent)
                activity.finish()
            }
        })

        if (AppConfiguration.instance.BASE_URL.equals(AppConfiguration.instance.BASE_PROD_URL)) {
            Helper.instance.setTextView(v,R.id.production_mode,"Production mode")
        } else {
            Helper.instance.setTextView(v,R.id.production_mode,"Development mode")
        }

        return v
    }

}