package bobzone.hrf.humanresource.Model.MetaData

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/10/18.
 */
open class SalesOrderMetaData {
    @SerializedName("count")
    var count = SalesOrderCount()
}