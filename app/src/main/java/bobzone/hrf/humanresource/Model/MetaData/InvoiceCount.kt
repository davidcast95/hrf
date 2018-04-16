package bobzone.hrf.humanresource.Model.MetaData

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/10/18.
 */

open class InvoiceCount {
    @SerializedName("Overdue")
    var overdue = 0
    @SerializedName("Unpaid")
    var unpaid = 0
    @SerializedName("Paid")
    var paid = 0

}