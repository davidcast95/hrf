package bobzone.hrf.humanresource.Fragments.Offer.Pending

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

open class OfferPending : OfferBase() {

    override fun fetchDidSuccess(data: OfferData) {
        val v = generatedView
        if (v is View) {
            val leadToggleView = v.findViewById<TextView>(R.id.lead_toggle)
            val openToggleView = v.findViewById<TextView>(R.id.open_toggle)
            val repliedToggleView = v.findViewById<TextView>(R.id.replied_toggle)
            val opportunityToggleView = v.findViewById<TextView>(R.id.opportunity_toggle)
            val interestedToggleView = v.findViewById<TextView>(R.id.interested_toggle)

            val metaData = MetaData.instance

            leadToggleView.setText("Lead (${metaData.lead.count.lead})")
            openToggleView.setText("Open (${metaData.lead.count.open})")
            repliedToggleView.setText("Replied (${metaData.lead.count.replied})")
            opportunityToggleView.setText("Opportunity (${metaData.lead.count.opportunity})")
            interestedToggleView.setText("Interested (${metaData.lead.count.interested})")
        }
    }

    override fun getAPI(query: String, page: Int): Call<BaseSingleMessage<OfferData>>? {
        val cookieJar = UserModel.instance.loadCookieJar(context)
        val api = Helper.instance.getAPIWithCookie(context,cookieJar)
        val start = page * Constant.instance.LIMIT_MASTER_LIST
        return api.getOffer(status,query,"$start")
    }


}