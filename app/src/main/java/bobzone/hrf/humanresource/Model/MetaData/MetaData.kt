package bobzone.hrf.humanresource.Model.MetaData

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleMessageCallBack
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.User.UserModel
import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/10/18.
 */
open class MetaData {

    companion object {
        var instance: MetaData = MetaData()
    }



    @SerializedName("sales_order")
    var sales_order = SalesOrderMetaData()
    @SerializedName("invoice")
    var invoice = InvoiceMetaData()
    @SerializedName("lead")
    var lead = LeadMetaData()
    @SerializedName("daily_net_sales")
    var daily_net_sales = mutableListOf<NetSalesData>()

    fun refreshMetaData(context:Context, didSuccess:()-> Unit) {
        val cookieJar = UserModel.instance.loadCookieJar(context)
        val api = Helper.instance.getAPIWithCookie(context, cookieJar)
        val callMetaData = api.getMetaData()
        callMetaData.enqueue(object: BaseSingleMessageCallBack<MetaData>(context) {
            override fun responseData(data: MetaData) {
                instance = data

                didSuccess()
            }

            override fun failed(error: Throwable) {

            }
        })
    }
}