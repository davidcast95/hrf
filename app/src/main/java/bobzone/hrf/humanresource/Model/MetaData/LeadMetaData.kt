package bobzone.hrf.humanresource.Model.MetaData

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/14/18.
 */
open class LeadMetaData {
    @SerializedName("count")
    var count = LeadCount()
}