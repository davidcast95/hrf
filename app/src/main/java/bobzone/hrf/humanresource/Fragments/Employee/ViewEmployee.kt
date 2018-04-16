package bobzone.hrf.humanresource.Fragments.Employee

import android.os.Bundle
import android.view.Menu
import android.view.View
import bobzone.hrf.humanresource.Core.Base.Activity.BaseActivity
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleCallBack
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.Customer.EmployeeSingleData
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderSingleData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import kotlinx.android.synthetic.main.activity_view_sales_order.*

/**
 * Created by davidwibisono on 3/7/18.
 */
open class ViewEmployee : BaseActivity() {

    var grandTotal = 0.0
    var employee = EmployeeSingleData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.view_sales_order)
        setContentView(R.layout.activity_view_employee)

        val employee_no = intent.getStringExtra("employee_no")
        if (employee_no != null) {
            fetchEmployee(employee_no)
        } else {
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return false
    }

    fun updateUI() {
        Helper.instance.setTextView(this, R.id.employee_name, employee.employee_name)
        Helper.instance.setTextView(this, R.id.employee_no, employee.name)
        Helper.instance.setTextView(this, R.id.status, employee.status)

        var formatView = "EEE, d MMM yyyy"
        var formatField = "yyyy-MM-dd"
        val formatDate = Helper.instance.formatDateFromstring(formatField, formatView, employee.date_of_joining)
        val joinDate = "${getString(R.string.joining_date_on)} $formatDate"
        Helper.instance.setTextView(this, R.id.join_date, joinDate)



//        val salesItemAdapter = SalesOrderItemAdapter(applicationContext, employee.items)
//        val lv = findViewById<ListView>(R.id.items)
//        lv.adapter = salesItemAdapter
//        lv.onItemClickListener = object:AdapterView.OnItemClickListener {
//            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                val item = salesItemAdapter.getItem(p2)
//                if (item.invoice_no.equals("")) {
//                    Toast.makeText(applicationContext,getString(R.string.no_link_to_any_invoice),Toast.LENGTH_SHORT).show()
//                } else {
//                    val viewINVItem = Intent(applicationContext, ViewInvoice::class.java)
//                    viewINVItem.putExtra("sales_invoice_no",item.invoice_no)
//                    startActivity(viewINVItem)
//                }
//            }
//        }

        loading.visibility = View.GONE
    }

    fun fetchEmployee(name:String) {
        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext, cookieJar)
        val callSO = api.getSpecifiedEmployee(name)
        callSO.enqueue(object: BaseSingleCallBack<EmployeeSingleData>(applicationContext) {
            override fun responseData(data: EmployeeSingleData) {
                employee = data
                updateUI()
            }

            override fun failed(error: Throwable) {

            }
        })
    }



}