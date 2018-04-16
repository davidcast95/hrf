package bobzone.hrf.humanresource.Fragments.Offer.Done

import android.view.View
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Global.Constant
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.Offer.Base.OfferBase
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseSingleMessage
import bobzone.hrf.humanresource.Model.Lead.LeadData
import bobzone.hrf.humanresource.Model.MetaData.MetaData
import bobzone.hrf.humanresource.Model.Offer.OfferData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import retrofit2.Call

/**
 * Created by davidwibisono on 3/14/18.
 */
open class OfferDone : OfferBase() {

    override fun fetchDidSuccess(data: OfferData) {
        val v = generatedView
        if (v is View) {
            val convertedToggleView = v.findViewById<TextView>(R.id.converted_toggle)
            val doNotContactToggleView = v.findViewById<TextView>(R.id.do_not_contact_toggle)

            val metaData = MetaData.instance

            convertedToggleView.setText("Converted (${metaData.lead.count.converted})")
            doNotContactToggleView.setText("Do Not Contact (${metaData.lead.count.do_not_contact})")
        }
    }

    override fun getAPI(query: String, page: Int): Call<BaseSingleMessage<OfferData>>? {
        val cookieJar = UserModel.instance.loadCookieJar(context)
        val api = Helper.instance.getAPIWithCookie(context,cookieJar)
        val start = page * Constant.instance.LIMIT_MASTER_LIST
        return api.getOffer(status,query,"$start")
    }



}