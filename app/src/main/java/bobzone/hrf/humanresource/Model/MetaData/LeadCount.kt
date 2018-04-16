package bobzone.hrf.humanresource.Model.MetaData

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/14/18.
 */
open class LeadCount {
    @SerializedName("Lead")
    var lead = 0
    @SerializedName("Open")
    var open = 0
    @SerializedName("Replied")
    var replied = 0
    @SerializedName("Opportunity")
    var opportunity = 0
    @SerializedName("Interested")
    var interested = 0
    @SerializedName("Quotation")
    var quotation = 0
    @SerializedName("Lost Quotation")
    var lost_quotation = 0
    @SerializedName("Converted")
    var converted = 0
    @SerializedName("Do Not Contact")
    var do_not_contact = 0
}