package bobzone.hrf.humanresource.Fragments.Customer

import android.app.Activity
import android.content.Intent
import android.view.*
import bobzone.hrf.humanresource.Core.Base.List.MasterListDelegate
import bobzone.hrf.humanresource.Core.Base.List.MasterListEventListener
import bobzone.hrf.humanresource.Core.Base.OptionsMenu.SearchOptionsMenuDelegate
import bobzone.hrf.humanresource.Core.Base.SideMenu.MenuFragment
import bobzone.hrf.humanresource.Core.Global.Constant
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.Customer.CustomerData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import retrofit2.Call

/**
 * Created by davidwibisono on 12/24/17.
 */

class CustomerFragment : MenuFragment(), MasterListEventListener<CustomerData> {

    var searchOptinsMenuDelegate: SearchOptionsMenuDelegate = SearchOptionsMenuDelegate()
    var masterListDelegate: MasterListDelegate<CustomerData> = MasterListDelegate()
    var customerList: List<CustomerData> = ArrayList<CustomerData>()

    override fun getView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.fragment_customer,container,false)

        val customerAdapter = CustomerAdapter(context, customerList)
        masterListDelegate.setup(v, customerAdapter, context)
        masterListDelegate.listenerMaster = this

        searchOptinsMenuDelegate.listener = masterListDelegate

        masterListDelegate.onFetchData()
        return v
    }

    override fun getOptionsMenu(): Int {
        return R.menu.menu_search_add
    }

    override fun getSearchOptionsMenu(): SearchOptionsMenuDelegate {
        return searchOptinsMenuDelegate
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
            masterListDelegate.refreshItems()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val _item = item
        if (_item is MenuItem) {
            val id = _item.itemId
            when (id) {
                R.id.add -> {
                    val intent = Intent(context, AddCustomer::class.java)
                    startActivityForResult(intent,100)
                    return true
                }
                else -> {
                    return false
                }
            }
        }
        return false
    }

    override fun getTitleMenu(): String {
        return getString(R.string.customer)
    }

    override fun willRefreshItems() {

    }

    override fun fetchDidSuccess(data: MutableList<CustomerData>) {

    }

    override fun getAPIHandlingOnBaseResponse(query:String, page: Int): Call<BaseResponse<CustomerData>>? {
        val cookieJar = UserModel.instance.loadCookieJar(context)
        val api = Helper.instance.getAPIWithCookie(context,cookieJar)
        val start = page * Constant.instance.LIMIT_MASTER_LIST
        return api.getCustomer("[[\"Customer\",\"customer_name\",\"LIKE\",\"%$query%\"]]", "$start")
    }

    override fun getAPIHandlingOnBaseMessage(query: String, page: Int): Call<BaseMessage<CustomerData>>? {
        return null
    }

    override fun onListTapped(item: CustomerData, position: Int) {
        val viewCustomer = Intent(context, ViewCustomer::class.java)
        viewCustomer.putExtra("customer_name",item.name)
        startActivity(viewCustomer)
    }
}