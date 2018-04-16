package bobzone.hrf.humanresource.Model.Item

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/8/18.
 */
open class ItemArgs {
    @SerializedName("item_code")
    var item_code = ""
    @SerializedName("company")
    var company = ""
    @SerializedName("price_list")
    var price_list = "Standard Selling"
    @SerializedName("transaction_date")
    var transaction_date = ""
    @SerializedName("ignore_pricing_rule")
    var ignore_pricing_rule = 0
    @SerializedName("doctype")
    var doctype = "Sales Order"
    @SerializedName("conversion_rate")
    var conversion_rate = 1
    @SerializedName("currency")
    var currency = "IDR"
    @SerializedName("plc_conversion_rate")
    var plc_conversion_rate = 1
    @SerializedName("price_list_currency")
    var price_list_currency = "IDR"
}