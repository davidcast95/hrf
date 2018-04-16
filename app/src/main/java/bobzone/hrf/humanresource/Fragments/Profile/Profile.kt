package bobzone.hrf.humanresource.Fragments.Profile

import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Base.SideMenu.MenuFragment
import bobzone.hrf.humanresource.LoginActivity
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.rgb
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import java.util.ArrayList
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.components.XAxis



/**
 * Created by davidwibisono on 12/24/17.
 */

open class Profile : MenuFragment() {


    override fun getView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.fragment_profile,container,false)
        val name = v.findViewById<TextView>(R.id.name)
        name.setText(UserModel.instance.fullname)
        val email = v.findViewById<TextView>(R.id.email)
        email.setText(UserModel.instance.email)

        val logout = v.findViewById<Button>(R.id.logout)
        logout.setOnClickListener(object :View.OnClickListener {
            override fun onClick(p0: View?) {
                UserModel.instance.destroy(context)
                val loginIntent = Intent(context, LoginActivity::class.java)
                startActivity(loginIntent)
                activity.finish()
            }
        })
        return v
    }


    override fun getTitleMenu(): String {
        return getString(R.string.profile)
    }

}