package bobzone.hrf.humanresource.Model.Offer

import bobzone.hrf.humanresource.Model.Lead.LeadData
import bobzone.hrf.humanresource.Model.OfferItem.OpportunityData
import bobzone.hrf.humanresource.Model.Quotation.QuotationData
import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/22/18.
 */
open class OfferData {
    @SerializedName("leads")
    var leads = mutableListOf<LeadData>()
    @SerializedName("quotations")
    var quotations = mutableListOf<QuotationData>()
    @SerializedName("opportunities")
    var opportunities = mutableListOf<OpportunityData>()
}