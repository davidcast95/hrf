package bobzone.hrf.humanresource.Fragments.Invoice.Unpaid

import android.view.View
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Global.Constant
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.Invoice.Base.InvoiceBase
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.MetaData.MetaData
import bobzone.hrf.humanresource.Model.SalesOrder.InvoiceData
import bobzone.hrf.humanresource.Model.SalesOrder.InvoiceStatus
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderData
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderStatus
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import retrofit2.Call

/**
 * Created by davidwibisono on 3/6/18.
 */
open class InvoiceUnpaid : InvoiceBase() {


    override fun fetchDidSuccess(data: MutableList<InvoiceData>) {
        val v = generatedView
        if (v is View) {
            val overDueToggleView = v.findViewById<TextView>(R.id.overdue_toggle)
            val unpaidToggleView = v.findViewById<TextView>(R.id.unpaid_toggle)

            val metaData = MetaData.instance

            overDueToggleView.setText("Overdue (${metaData.invoice.count.overdue})")
            unpaidToggleView.setText("Unpaid (${metaData.invoice.count.unpaid})")
        }
    }

    override fun getAPIHandlingOnBaseMessage(query: String, page: Int): Call<BaseMessage<InvoiceData>>? {
        val cookieJar = UserModel.instance.loadCookieJar(context)
        val api = Helper.instance.getAPIWithCookie(context,cookieJar)
        val start = page * Constant.instance.LIMIT_MASTER_LIST
        return api.getSalesInvoice(status,query,"$start")
    }
}