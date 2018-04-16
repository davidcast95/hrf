package bobzone.hrf.humanresource.Model.Customer

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 2/13/18.
 */


open class CustomerData {
    @SerializedName("customer_name")
    var name:String = ""

    @SerializedName("customer_group")
    var customer_group:String = ""
    @SerializedName("customer_type")
    var customer_type:String = ""


    @SerializedName("territory")
    var territory:String = ""

    @SerializedName("default_price_list")
    var default_price_list = "Standard Selling"
}