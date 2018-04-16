package bobzone.hrf.humanresource.Model.SalesOrderItem

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/7/18.
 */
open class ItemDetailsData {
    @SerializedName("name")
    var name = ""
    @SerializedName("item_name")
    var item_name = ""
    @SerializedName("item_code")
    var item_code = ""

    @SerializedName("stock_qty")
    var stock_qty = 0
    @SerializedName("price_list_rate")
    var rate = 0.0
    @SerializedName("uom")
    var uom = ""
}