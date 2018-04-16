package bobzone.hrf.humanresource

import android.os.Bundle
import bobzone.hrf.humanresource.Core.Base.TabBar.FragmentTab
import bobzone.hrf.humanresource.Core.Base.TabBarMenu.TabBarMenuHandler
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.Home.Home
import bobzone.hrf.humanresource.Fragments.Invoice.Invoice
import bobzone.hrf.humanresource.Fragments.SalesOrder.SalesOrder
import android.support.v7.app.WindowDecorActionBar
import bobzone.hrf.humanresource.Fragments.Customer.CustomerFragment
import bobzone.hrf.humanresource.Fragments.Employee.EmployeeFragment
import bobzone.hrf.humanresource.Fragments.Leave.Leave
import bobzone.hrf.humanresource.Fragments.More.More
import bobzone.hrf.humanresource.Fragments.Offer.Offer


class MainActivity : TabBarMenuHandler() {

    val home = Home()
    val leave = Leave()
    val invoice = Invoice()
    val offer = Offer()
    val employee = EmployeeFragment()
    val more = More()

    override fun onCreate(savedInstanceState: Bundle?) {
        Helper.instance.getLanguage(this)
        setContentView(R.layout.activity_main)

        leave.home = home

        val sab = supportActionBar
        if (sab is WindowDecorActionBar) {
            sab.setShowHideAnimationEnabled(false)
            sab.hide()
        }
        setTitle(R.string.home)
        super.onCreate(savedInstanceState)

    }



    override fun getMenuFragmentCount(): Int {
        return 5
    }

    override fun getFragmentTab(position: Int): FragmentTab? {
        if (position == 0) {
            val fragmentTab = FragmentTab(home,R.string.home)
            fragmentTab.icon = getDrawable(R.drawable.home_button)
            return fragmentTab
        } else if (position == 1) {
            val fragmentTab = FragmentTab(leave,R.string.leave)
            fragmentTab.icon = getDrawable(R.drawable.sales_order_button)
            return fragmentTab
        } else if (position == 2) {
            val fragmentTab = FragmentTab(invoice,R.string.invoice)
            fragmentTab.icon = getDrawable(R.drawable.invoice_button)
            return fragmentTab
        } else if (position == 3) {
//            val fragmentTab = FragmentTab(offer,R.string.offer)
            val fragmentTab = FragmentTab(employee,R.string.employee)
            fragmentTab.icon = getDrawable(R.drawable.star_button)
            return fragmentTab
        } else {
            val fragmentTab = FragmentTab(more,R.string.more)
            fragmentTab.icon = getDrawable(R.drawable.more_button)
            return fragmentTab
        }
    }

    override fun pageChange(pos: Int) {
        super.pageChange(pos)
        val sab = supportActionBar
        if (sab is WindowDecorActionBar) {
            sab.setShowHideAnimationEnabled(false)
            if (pos == 0) {
                sab.hide()
            } else {
                sab.show()
            }
        }

    }



}
