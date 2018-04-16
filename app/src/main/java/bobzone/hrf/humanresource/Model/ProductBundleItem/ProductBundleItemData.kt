package bobzone.hrf.humanresource.Model.ProductBundleItem

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/16/18.
 */
open class ProductBundleItemData {
    @SerializedName("description")
    var description = ""
    @SerializedName("item_code")
    var item_code = ""
    @SerializedName("qty")
    var qty = 1
    @SerializedName("uom")
    var uom = ""
    @SerializedName("standard_rate")
    var rate = 0.0
}