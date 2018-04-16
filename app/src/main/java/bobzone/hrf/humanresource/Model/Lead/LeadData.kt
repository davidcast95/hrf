package bobzone.hrf.humanresource.Model.Lead

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/14/18.
 */
open class LeadData {
    @SerializedName("company_name")
    var company_name = ""
    @SerializedName("contact_date")
    var contact_date = ""
    @SerializedName("lead_name")
    var lead_name = ""
    @SerializedName("request_type")
    var request_type = ""
    @SerializedName("type")
    var type = ""
    @SerializedName("status")
    var status = ""
    @SerializedName("owner")
    var owner = ""
    @SerializedName("name")
    var name = ""
    @SerializedName("email_id")
    var email = ""
    @SerializedName("market_segment")
    var market_segment = ""
}