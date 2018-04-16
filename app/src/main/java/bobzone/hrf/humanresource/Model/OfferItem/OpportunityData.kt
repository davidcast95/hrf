package bobzone.hrf.humanresource.Model.OfferItem

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/17/18.
 */
open class OpportunityData {

    @SerializedName("customer_name")
    var customer_name = ""
    @SerializedName("lead")
    var lead = ""
    @SerializedName("transaction_date")
    var transaction_date = ""
    @SerializedName("with_items")
    var with_items = 0
    @SerializedName("name")
    var name = ""
    @SerializedName("opportunity_type")
    var opportunity_type = ""
    @SerializedName("status")
    var status = ""

}