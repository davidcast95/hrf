package bobzone.hrf.humanresource.Model.SalesOrderItem

import bobzone.hrf.humanresource.Model.ProductBundleItem.ProductBundleItemData
import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/7/18.
 */
open class ItemData {
    @SerializedName("name")
    var name = ""
    @SerializedName("item_name")
    var item_name = ""
    @SerializedName("item_code")
    var item_code = ""

    @SerializedName("standard_rate")
    var standard_rate = 0.0
    @SerializedName("uom")
    var uom = ""

    @SerializedName("product_bundle_item")
    var product_bundle_item = mutableListOf<ProductBundleItemData>()
}