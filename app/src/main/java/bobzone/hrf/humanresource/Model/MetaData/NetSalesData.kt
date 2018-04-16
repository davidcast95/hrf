package bobzone.hrf.humanresource.Model.MetaData

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/10/18.
 */
open class NetSalesData {
    @SerializedName("net_sales")
    var net_sales = 0.0
    @SerializedName("posting_date")
    var posting_date = ""
}