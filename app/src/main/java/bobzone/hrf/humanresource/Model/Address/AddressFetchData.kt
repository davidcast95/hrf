package bobzone.hrf.humanresource.Model.Address

import bobzone.hrf.humanresource.Model.DynamicLink.DynamicLinkData
import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/7/18.
 */
open class AddressFetchData {

    @SerializedName("phone")
    var phone = ""
    @SerializedName("address_line1")
    var address_line1 = ""
    @SerializedName("address_line2")
    var address_line2 = ""
    @SerializedName("address_type")
    var address_type = "Billing"
    @SerializedName("city")
    var city = ""
    @SerializedName("state")
    var state = ""
    @SerializedName("country")
    var country = ""
    @SerializedName("pincode")
    var pincode = ""

    @SerializedName("latitude")
    var latitude = ""
    @SerializedName("longitude")
    var longitude = ""

}