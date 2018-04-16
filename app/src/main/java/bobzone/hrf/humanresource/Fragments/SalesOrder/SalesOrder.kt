package bobzone.hrf.humanresource.Fragments.SalesOrder

import android.app.Activity
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
import bobzone.hrf.humanresource.Fragments.Home.Home
import bobzone.hrf.humanresource.Fragments.Invoice.Base.InvoiceBase
import bobzone.hrf.humanresource.Fragments.SalesOrder.Active.SalesOrderActive
import bobzone.hrf.humanresource.Fragments.SalesOrder.AddSalesOrder.AddSalesOrder
import bobzone.hrf.humanresource.Fragments.SalesOrder.Base.SalesOrderBase
import bobzone.hrf.humanresource.Fragments.SalesOrder.Done.SalesOrderDone
import bobzone.hrf.humanresource.Fragments.SalesOrder.Return.SalesOrderReturn
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/6/18.
 */

open class SalesOrder : MenuFragment(), TabBarEventListener, SearchView.OnQueryTextListener {


    var home:Home? = null
    var searchOptinsMenuDelegate: SearchOptionsMenuDelegate = SearchOptionsMenuDelegate()
    val tabBarDelegate = TabBarDelegate()
    val activeSO = FragmentTab(SalesOrderActive(),R.string.active)
    val doneSO = FragmentTab(SalesOrderDone(),R.string.done)
    val returnSO = FragmentTab(SalesOrderReturn(),R.string.returned)
    var listFragment = arrayListOf<FragmentTab>(activeSO,doneSO,returnSO)

    var selectedFragment = 0

    override fun getView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.fragment_sales, container, false)

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
        return R.menu.menu_search_add
    }

    override fun getSearchOptionsMenu(): SearchOptionsMenuDelegate {
        return searchOptinsMenuDelegate
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val _item = item
        if (_item is MenuItem) {
            val id = _item.itemId
            when (id) {
                R.id.add -> {
                    val intent = Intent(context, AddSalesOrder::class.java)
                    startActivityForResult(intent, 100)
                    return true
                }
                else -> {
                    return false
                }
            }
        }
        return false
    }

    override fun pageChange(pos: Int) {
        selectedFragment = pos
        (listFragment[selectedFragment].fragment as SalesOrderBase).masterListDelegate.refreshItems()
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        val q = p0
        if (q is String)
            (listFragment[selectedFragment].fragment as SalesOrderBase).masterListDelegate.searchQuery(q)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            (listFragment[selectedFragment].fragment as SalesOrderBase).refreshItems()
            val h = home
            if (h is Home) {
                h.refresh()
            }

        }
    }
}