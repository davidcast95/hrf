package bobzone.hrf.humanresource.Fragments.Invoice

import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import bobzone.hrf.humanresource.Core.Base.OptionsMenu.SearchOptionsMenuDelegate
import bobzone.hrf.humanresource.Core.Base.SideMenu.MenuFragment
import bobzone.hrf.humanresource.Core.Base.TabBar.FragmentTab
import bobzone.hrf.humanresource.Core.Base.TabBar.TabBarDelegate
import bobzone.hrf.humanresource.Core.Base.TabBar.TabBarEventListener
import bobzone.hrf.humanresource.Fragments.Invoice.Base.InvoiceBase
import bobzone.hrf.humanresource.Fragments.Invoice.Paid.InvoicePaid
import bobzone.hrf.humanresource.Fragments.Invoice.Unpaid.InvoiceUnpaid
import bobzone.hrf.humanresource.Fragments.SalesOrder.Base.SalesOrderBase
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/6/18.
 */

open class Invoice : MenuFragment(), TabBarEventListener, SearchView.OnQueryTextListener {

    var searchOptinsMenuDelegate: SearchOptionsMenuDelegate = SearchOptionsMenuDelegate()
    val tabBarDelegate = TabBarDelegate()
    val paidINV = FragmentTab(InvoicePaid(),R.string.paid)
    val unpaidINV = FragmentTab(InvoiceUnpaid(),R.string.unpaid)
    var listFragment = arrayListOf<FragmentTab>(unpaidINV, paidINV)

    var selectedFragment = 0

    override fun getView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.fragment_tab_bar, container, false)

        searchOptinsMenuDelegate.listener = this

        tabBarDelegate.setup(context, R.id.view_pager, R.id.tabhost, v, listFragment, this)
        tabBarDelegate.listener = this
        setHasOptionsMenu(true)
        return v
    }

    override fun getTitleMenu(): String {
        return getString(R.string.sales_order)
    }

    override fun getOptionsMenu(): Int {
        return R.menu.menu_search
    }

    override fun getSearchOptionsMenu(): SearchOptionsMenuDelegate {
        return searchOptinsMenuDelegate
    }


    override fun pageChange(pos: Int) {
        selectedFragment = pos
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        val q = p0
        if (q is String)
            (listFragment[selectedFragment].fragment as InvoiceBase).masterListDelegate.searchQuery(q)
        return true
        return true
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

}