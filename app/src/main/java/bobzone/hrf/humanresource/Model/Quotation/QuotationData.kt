package bobzone.hrf.humanresource.Model.Quotation

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/17/18.
 */
open class QuotationData {
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
    @SerializedName("order_type")
    var order_type = ""
    @SerializedName("contact_email")
    var contact_email = ""
    @SerializedName("status")
    var status = ""
    @SerializedName("total_taxes_and_charges")
    var total_taxes_and_charges = 0.0
}