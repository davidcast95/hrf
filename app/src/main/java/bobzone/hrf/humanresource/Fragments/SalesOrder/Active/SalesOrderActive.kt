package bobzone.hrf.humanresource.Fragments.SalesOrder.Active

import android.view.View
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Global.Constant
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.SalesOrder.Base.SalesOrderBase
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.MetaData.MetaData
import bobzone.hrf.humanresource.Model.SalesOrder.InvoiceStatus
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderData
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderStatus
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import retrofit2.Call

/**
 * Created by davidwibisono on 3/6/18.
 */
open class SalesOrderActive : SalesOrderBase() {


    override fun fetchDidSuccess(data: MutableList<SalesOrderData>) {
        updateStatusToggle()
    }


    fun updateStatusToggle() {
        val v = generatedView
        if (v is View) {

            val toDeliverAndBillToggleView = v.findViewById<TextView>(R.id.to_deliver_and_bill_toggle)
            val toBillToggleView = v.findViewById<TextView>(R.id.to_bill_toggle)
            val toDeliverToggleView = v.findViewById<TextView>(R.id.to_deliver_toggle)

            val metaData = MetaData.instance

            toDeliverAndBillToggleView.setText("To Deliver and Bill (${metaData.sales_order.count.to_deliver_and_bill})")
            toBillToggleView.setText("To Bill (${metaData.sales_order.count.to_bill})")
            toDeliverToggleView.setText("To Deliver (${metaData.sales_order.count.to_deliver})")
        }
    }

    override fun getAPIHandlingOnBaseMessage(query: String, page: Int): Call<BaseMessage<SalesOrderData>>? {
        val cookieJar = UserModel.instance.loadCookieJar(context)
        val api = Helper.instance.getAPIWithCookie(context,cookieJar)
        val start = page * Constant.instance.LIMIT_MASTER_LIST
        return api.getSalesOrder(status,query,"$start")
    }



}