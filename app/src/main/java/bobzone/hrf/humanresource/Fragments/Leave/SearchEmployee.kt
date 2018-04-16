package bobzone.hrf.humanresource.Fragments.Leave

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
import bobzone.hrf.humanresource.Fragments.Employee.EmployeeAdapter
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.BaseSingleMessage
import bobzone.hrf.humanresource.Model.Customer.EmployeeData
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
open class SearchEmployee : BaseActivity(), MasterListEventListener<EmployeeData> {

    var searchOptinsMenuDelegate: SearchOptionsMenuDelegate = SearchOptionsMenuDelegate()
    var masterListDelegate = MasterListDelegate<EmployeeData>()
    var employees = mutableListOf<EmployeeData>()
    var chosseEmployee = EmployeeData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search_item)
        setTitle(R.string.add_customer)
        val listAdapter = EmployeeAdapter(applicationContext, employees)
        masterListDelegate.setup(this,listAdapter, applicationContext)
        masterListDelegate.listenerMaster = this
        searchOptinsMenuDelegate.listener = masterListDelegate

        masterListDelegate.onFetchData()

    }

    override fun willRefreshItems() {

    }

    override fun fetchDidSuccess(data: MutableList<EmployeeData>) {

    }

    override fun getAPIHandlingOnBaseResponse(query:String, page: Int): Call<BaseResponse<EmployeeData>>? {
        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext,cookieJar)
        val start = page * Constant.instance.LIMIT_MASTER_LIST
        return api.getEmployee("[[\"Employee\",\"employee_name\",\"LIKE\",\"%$query%\"]]", "$start")
    }

    override fun getAPIHandlingOnBaseMessage(query: String, page: Int): Call<BaseMessage<EmployeeData>>? {
        return null
    }


    override fun onListTapped(item: EmployeeData, position: Int) {
        val intent = Intent()
        chosseEmployee = item
        var name = chosseEmployee.name
        var employeeName = chosseEmployee.employee_name

        if (name == null)  name = ""
        intent.putExtra("employee",name)
        if (employeeName == null) employeeName = ""
        intent.putExtra("employee_name",employeeName)
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