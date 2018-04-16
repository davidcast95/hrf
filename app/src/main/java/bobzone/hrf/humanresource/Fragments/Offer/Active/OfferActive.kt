package bobzone.hrf.humanresource.Fragments.Offer.Active

import android.view.View
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Global.Constant
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.Offer.Base.OfferBase
import bobzone.hrf.humanresource.Model.BaseSingleMessage
import bobzone.hrf.humanresource.Model.MetaData.MetaData
import bobzone.hrf.humanresource.Model.Offer.OfferData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import retrofit2.Call

/**
 * Created by davidwibisono on 3/14/18.
 */
open class OfferActive : OfferBase() {

    override fun fetchDidSuccess(data: OfferData) {
        val v = generatedView
        if (v is View) {
            val quotationToggleView = v.findViewById<TextView>(R.id.quotation_toggle)
            val lostQuotationToggleView = v.findViewById<TextView>(R.id.lost_quotation_toggle)

            val metaData = MetaData.instance

            quotationToggleView.setText("Quotation (${metaData.lead.count.quotation})")
            lostQuotationToggleView.setText("Lost Quotation (${metaData.lead.count.lost_quotation})")
        }
    }

    override fun getAPI(query: String, page: Int): Call<BaseSingleMessage<OfferData>>? {
        val cookieJar = UserModel.instance.loadCookieJar(context)
        val api = Helper.instance.getAPIWithCookie(context,cookieJar)
        val start = page * Constant.instance.LIMIT_MASTER_LIST
        return api.getOffer(status,query,"$start")
    }




}