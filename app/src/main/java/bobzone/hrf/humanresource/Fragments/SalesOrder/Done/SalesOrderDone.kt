package bobzone.hrf.humanresource.Fragments.SalesOrder.Done

import bobzone.hrf.humanresource.Core.Global.Constant
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.SalesOrder.Base.SalesOrderBase
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderData
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderStatus
import bobzone.hrf.humanresource.Model.User.UserModel
import retrofit2.Call

/**
 * Created by davidwibisono on 3/6/18.
 */
open class SalesOrderDone : SalesOrderBase() {


    override fun getAPIHandlingOnBaseMessage(query: String, page: Int): Call<BaseMessage<SalesOrderData>>? {
        val cookieJar = UserModel.instance.loadCookieJar(context)
        val api = Helper.instance.getAPIWithCookie(context,cookieJar)
        val start = page * Constant.instance.LIMIT_MASTER_LIST
        return api.getSalesOrder(SalesOrderStatus.instance.COMPLETED,query,"$start")
    }
}