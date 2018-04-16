package bobzone.hrf.humanresource.Fragments.SalesOrder.AddSalesOrder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import bobzone.hrf.humanresource.Core.Base.Activity.BaseActivity
import bobzone.hrf.humanresource.Core.Base.Callback.BaseResponseCallBack
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleMessageCallBack
import bobzone.hrf.humanresource.Core.Base.OptionsMenu.SearchOptionsMenuDelegate
import bobzone.hrf.humanresource.Core.Base.TabBar.FragmentTab
import bobzone.hrf.humanresource.Core.Base.TabBar.TabBarDelegate
import bobzone.hrf.humanresource.Core.Base.TabBar.TabBarEventListener
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.Offer.Active.OfferActive
import bobzone.hrf.humanresource.Fragments.Offer.Base.OfferBase
import bobzone.hrf.humanresource.Fragments.Offer.Done.OfferDone
import bobzone.hrf.humanresource.Fragments.Offer.Pending.OfferPending
import bobzone.hrf.humanresource.Model.Item.ItemArgs
import bobzone.hrf.humanresource.Model.PriceList.PriceListData
import bobzone.hrf.humanresource.Model.SalesOrderItem.ItemData
import bobzone.hrf.humanresource.Model.SalesOrderItem.ItemDetailsData
import bobzone.hrf.humanresource.Model.SalesOrderItem.SalesOrderItemData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import com.google.gson.Gson

/**
 * Created by davidwibisono on 3/15/18.
 */
open class Item : BaseActivity(), TabBarEventListener, SearchView.OnQueryTextListener {

    var searchOptinsMenuDelegate: SearchOptionsMenuDelegate = SearchOptionsMenuDelegate()
    val tabBarDelegate = TabBarDelegate()
    val searchItem = SearchItem()
    val searchItemBundle = SearchItemBundle()
    var listFragment = arrayListOf<FragmentTab>()

    var selectedFragment = 0
    var currency = "IDR"
    var company = ""
    var customerPriceList = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.add_item)
        searchOptinsMenuDelegate.listener = this

        setContentView(R.layout.activity_item)

        val cur = intent.getStringExtra("currency")
        if (cur is String) currency = cur
        val com = intent.getStringExtra("company")
        if (com is String) company = com

        searchItem.currency = currency
        searchItem.company = company
        searchItem.item = this

        searchItemBundle.currency = currency
        searchItemBundle.company = company
        searchItemBundle.item = this
        listFragment = arrayListOf<FragmentTab>(FragmentTab(searchItem, R.string.product), FragmentTab(searchItemBundle, R.string.product_bundle))

        tabBarDelegate.setup(applicationContext, R.id.view_pager, R.id.tabhost, this, listFragment, supportFragmentManager)
        tabBarDelegate.listener = this

        customerPriceList = intent.getStringExtra("customer_default_price_list")
        if (customerPriceList.equals("")) {
            showDialogPriceList()
        } else {
            Helper.instance.setTextView(this,R.id.price_list,customerPriceList)
        }
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
        if (q is String) {
            val currentFragment = listFragment[selectedFragment].fragment
            if (currentFragment is SearchItem)
                currentFragment.masterListDelegate.searchQuery(q)
            if (currentFragment is SearchItemBundle)
                currentFragment.masterListDelegate.searchQuery(q)
        }
        return true
        return true
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    fun showDialogPriceList() {

        AlertDialog.Builder(this)
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.dialog_price_list, null)

        val loading = promptsView.findViewById<RelativeLayout>(R.id.loading_view)
        if (loading is RelativeLayout)
            loading.visibility = View.VISIBLE

        val alertDialogBuilder = AlertDialog.Builder(
                this)
        alertDialogBuilder.setView(promptsView)
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
        // create alert dialog
        val alertDialog = alertDialogBuilder.create()

        val cookieJar = UserModel.instance.loadCookieJar(this)
        val api = Helper.instance.getAPIWithCookie(this, cookieJar)

        val callPriceList = api.getPriceList()
        val activity = this

        callPriceList.enqueue(object : BaseResponseCallBack<PriceListData>(applicationContext) {
            override fun responseData(data: MutableList<PriceListData>) {
                val lv = promptsView.findViewById<ListView>(R.id.price_list_list_view)
                val priceListAdapter = PriceListAdapter(applicationContext,data)
                lv.adapter = priceListAdapter

                lv.onItemClickListener = object:AdapterView.OnItemClickListener {
                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        customerPriceList = priceListAdapter.getItem(p2).name
                        Helper.instance.setTextView(activity,R.id.price_list,customerPriceList)
                        alertDialog.dismiss()
                    }
                }

                if (loading is RelativeLayout)
                    loading.visibility = View.GONE

            }
            override fun failed(error: Throwable) {
                if (loading is RelativeLayout)
                    loading.visibility = View.GONE
                Helper.instance.showConnectivityUnstable(applicationContext)
            }
        })

        alertDialog.show()

    }


}