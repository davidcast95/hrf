package bobzone.hrf.humanresource.Fragments.SalesOrder.AddSalesOrder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import bobzone.hrf.humanresource.Core.Base.Activity.BaseActivity
import bobzone.hrf.humanresource.Core.Base.List.MasterListDelegate
import bobzone.hrf.humanresource.Core.Base.List.MasterListEventListener
import bobzone.hrf.humanresource.Core.Base.OptionsMenu.SearchOptionsMenuDelegate
import bobzone.hrf.humanresource.Core.Global.Constant
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.Customer.AddCustomer
import bobzone.hrf.humanresource.Fragments.Customer.CustomerAdapter
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.BaseSingleMessage
import bobzone.hrf.humanresource.Model.Customer.CustomerData
import bobzone.hrf.humanresource.Model.Item.ItemArgs
import bobzone.hrf.humanresource.Model.SalesOrderItem.ItemData
import bobzone.hrf.humanresource.Model.SalesOrderItem.ItemDetailsData
import bobzone.hrf.humanresource.Model.SalesOrderItem.SalesOrderItemData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by davidwibisono on 3/7/18.
 */
open class SearchCustomer : BaseActivity(), MasterListEventListener<CustomerData> {

    var searchOptinsMenuDelegate: SearchOptionsMenuDelegate = SearchOptionsMenuDelegate()
    var masterListDelegate = MasterListDelegate<CustomerData>()
    var customers = mutableListOf<CustomerData>()
    var chosseCustomer = CustomerData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search_item)
        setTitle(R.string.add_customer)
        val listAdapter = CustomerAdapter(applicationContext, customers)
        masterListDelegate.setup(this,listAdapter, applicationContext)
        masterListDelegate.listenerMaster = this
        searchOptinsMenuDelegate.listener = masterListDelegate

        masterListDelegate.onFetchData()

    }

    override fun willRefreshItems() {

    }

    override fun fetchDidSuccess(data: MutableList<CustomerData>) {

    }

    override fun getAPIHandlingOnBaseResponse(query: String, page: Int): Call<BaseResponse<CustomerData>>? {
        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext,cookieJar)
        val start = page * Constant.instance.LIMIT_MASTER_LIST
        return api.getCustomer("[[\"Customer\",\"customer_name\",\"LIKE\",\"%$query%\"]]", "$start")
    }

    override fun getAPIHandlingOnBaseMessage(query: String, page: Int): Call<BaseMessage<CustomerData>>? {
        return null
    }


    override fun onListTapped(item: CustomerData, position: Int) {
        val intent = Intent()
        chosseCustomer = item
        var name = chosseCustomer.name
        var group = chosseCustomer.customer_group
        var pricelist = chosseCustomer.default_price_list

        if (name == null)  name = ""
        intent.putExtra("customer",name)
        if (group == null) group = ""
        intent.putExtra("customer_group",group)
        if (pricelist == null) pricelist = ""
        intent.putExtra("customer_default_price_list", pricelist)
        setResult(Activity.RESULT_OK,intent)
        finish()
    }



    override fun getOptionsMenu(): Int {
        return R.menu.menu_search_add
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val _item = item
        if (_item is MenuItem) {
            val id = _item.itemId
            when (id) {
                R.id.add -> {
                    val intent = Intent(applicationContext, AddCustomer::class.java)
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

    override fun getSearchOptionsMenu(): SearchOptionsMenuDelegate {
        return searchOptinsMenuDelegate
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            masterListDelegate.refreshItems()
        }
    }


}