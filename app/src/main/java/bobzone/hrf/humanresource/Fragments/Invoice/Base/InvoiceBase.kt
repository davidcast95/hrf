package bobzone.hrf.humanresource.Fragments.Invoice.Base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Base.List.MasterListDelegate
import bobzone.hrf.humanresource.Core.Base.List.MasterListEventListener
import bobzone.hrf.humanresource.Fragments.Invoice.Unpaid.InvoiceUnpaid
import bobzone.hrf.humanresource.Fragments.Invoice.ViewInvoice
import bobzone.hrf.humanresource.Fragments.SalesOrder.Active.SalesOrderActive
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.SalesOrder.InvoiceData
import bobzone.hrf.humanresource.Model.SalesOrder.InvoiceStatus
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.Model.UserSettings
import bobzone.hrf.humanresource.R
import com.google.gson.Gson
import retrofit2.Call

/**
 * Created by davidwibisono on 3/6/18.
 */
open class InvoiceBase : Fragment(), MasterListEventListener<InvoiceData> {

    var generatedView:View? = null
    var masterListDelegate: MasterListDelegate<InvoiceData> = MasterListDelegate()
    var invoiceList: List<InvoiceData> = ArrayList<InvoiceData>()

    var status = ""
    var overdueToggle = true
    var unpaidToggle = true

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val infl = inflater
        if (infl is LayoutInflater) {
            val v = inflater.inflate(R.layout.fragment_invoice_base, container, false)
            generatedView = v
            setupStatusToggle(v, context)

            val invoiceAdapter = InvoiceAdapter(context, invoiceList)
            masterListDelegate.setup(v, invoiceAdapter, context)
            masterListDelegate.listenerMaster = this


            masterListDelegate.onFetchData()
            return v
        }
        return view
    }

    fun setupStatusToggle(v:View, c:Context) {
        val statusToggle = v.findViewById<LinearLayout>(R.id.status_toggle)
        if (this is InvoiceUnpaid) {
            statusToggle.visibility = View.VISIBLE


            overdueToggle = UserModel.instance.getUserPreferencesAsBoolean(c, UserSettings.instance.INVOICE_OVERDUE, overdueToggle)
            unpaidToggle = UserModel.instance.getUserPreferencesAsBoolean(c, UserSettings.instance.INVOICE_UNPAID, unpaidToggle)

            val overDueToggleView = v.findViewById<TextView>(R.id.overdue_toggle)
            val unpaidToggleView = v.findViewById<TextView>(R.id.unpaid_toggle)

            setupAlpha(overDueToggleView,overdueToggle)
            setupAlpha(unpaidToggleView,unpaidToggle)

            overDueToggleView.setOnClickListener(object:View.OnClickListener {
                override fun onClick(p0: View?) {
                    overdueToggle = !overdueToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c, UserSettings.instance.INVOICE_OVERDUE,overdueToggle)
                    setupAlpha(overDueToggleView,overdueToggle)
                    updateStatus()
                }
            })
            unpaidToggleView.setOnClickListener(object:View.OnClickListener {
                override fun onClick(p0: View?) {
                    unpaidToggle = !unpaidToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c, UserSettings.instance.INVOICE_UNPAID,unpaidToggle)
                    setupAlpha(unpaidToggleView,unpaidToggle)
                    updateStatus()
                }
            })
            updateStatus()

        } else {
            statusToggle.visibility = View.GONE
        }
    }

    fun setupAlpha(v: TextView, toggle:Boolean) {
        if (toggle) {
            v.alpha = 1f
        } else {
            v.alpha = 0.5f
        }
    }

    fun updateStatus() {
        if (this is InvoiceUnpaid) {
            status = ""
            if (overdueToggle) status += "${InvoiceStatus.instance.OVERDUE},"
            if (unpaidToggle) status += InvoiceStatus.instance.UNPAID
            refreshItems()
        }
    }

    fun refreshItems() {
        masterListDelegate.refreshItems()
    }

    override fun willRefreshItems() {

    }

    override fun fetchDidSuccess(data: MutableList<InvoiceData>) {

    }

    override fun getAPIHandlingOnBaseResponse(query: String, page: Int): Call<BaseResponse<InvoiceData>>? {
        return null
    }

    override fun getAPIHandlingOnBaseMessage(query: String, page: Int): Call<BaseMessage<InvoiceData>>? {
        return null
    }

    override fun onListTapped(item: InvoiceData, position: Int) {
        val viewINVItem = Intent(context, ViewInvoice::class.java)
        viewINVItem.putExtra("sales_invoice_no",item.name)
        startActivity(viewINVItem)
    }

}