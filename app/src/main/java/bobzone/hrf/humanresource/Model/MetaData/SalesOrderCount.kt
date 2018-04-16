package bobzone.hrf.humanresource.Model.MetaData

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/10/18.
 */

open class SalesOrderCount {
    @SerializedName("Canceled")
    var canceled = 0
    @SerializedName("Closed")
    var closed = 0
    @SerializedName("Completed")
    var completed = 0
    @SerializedName("Draft")
    var draft = 0
    @SerializedName("To Bill")
    var to_bill = 0
    @SerializedName("To Deliver")
    var to_deliver = 0
    @SerializedName("To Deliver and Bill")
    var to_deliver_and_bill = 0

}