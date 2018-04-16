package bobzone.hrf.humanresource.Model.InvoiceItem

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/9/18.
 */
open class InvoiceItemData {
    @SerializedName("name")
    var name = ""
    @SerializedName("item_name")
    var item_name = ""
    @SerializedName("item_code")
    var item_code = ""

    @SerializedName("qty")
    var qty = 0
    @SerializedName("amount")
    var amount = 0.0
    @SerializedName("price_list_rate")
    var price_list_rate = 0.0
    @SerializedName("rate")
    var rate = 0.0
    @SerializedName("uom")
    var uom = ""

    var currency = ""

    @SerializedName("discount_percentage")
    var discount_percentage = 0.0

    @SerializedName("sales_order")
    var sales_order = ""
    @SerializedName("parent")
    var parent = ""

}