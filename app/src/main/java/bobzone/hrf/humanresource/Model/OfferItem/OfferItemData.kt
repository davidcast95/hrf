package bobzone.hrf.humanresource.Model.OfferItem

import bobzone.hrf.humanresource.Model.Quotation.QuotationData
import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/17/18.
 */

open class OfferItemData {
    @SerializedName("opportunity")
    var opportunity = mutableListOf<OpportunityData>()
    @SerializedName("quotation")
    var quotation = mutableListOf<QuotationData>()
}