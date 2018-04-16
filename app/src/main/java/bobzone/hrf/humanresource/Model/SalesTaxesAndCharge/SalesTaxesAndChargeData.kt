package bobzone.hrf.humanresource.Model.SalesTaxesAndCharge

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/12/18.
 */
open class SalesTaxesAndChargeData {
    @SerializedName("included_in_print_rate")
    var included_in_print_rate = 0
    @SerializedName("description")
    var description = ""
    @SerializedName("tax_amount")
    var tax_amount = 0.0

    var currency = "IDR"
}