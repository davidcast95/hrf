package bobzone.hrf.humanresource.Fragments.Invoice.Paid

import bobzone.hrf.humanresource.Core.Global.Constant
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.Invoice.Base.InvoiceBase
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.SalesOrder.InvoiceData
import bobzone.hrf.humanresource.Model.SalesOrder.InvoiceStatus
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderData
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderStatus
import bobzone.hrf.humanresource.Model.User.UserModel
import retrofit2.Call

/**
 * Created by davidwibisono on 3/6/18.
 */
open class InvoicePaid : InvoiceBase() {

    override fun getAPIHandlingOnBaseMessage(query: String, page: Int): Call<BaseMessage<InvoiceData>>? {
        val cookieJar = UserModel.instance.loadCookieJar(context)
        val api = Helper.instance.getAPIWithCookie(context,cookieJar)
        val start = page * Constant.instance.LIMIT_MASTER_LIST
        return api.getSalesInvoice(InvoiceStatus.instance.PAID,query,"$start")
    }




}