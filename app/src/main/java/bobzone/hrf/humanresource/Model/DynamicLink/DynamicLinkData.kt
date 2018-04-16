package bobzone.hrf.humanresource.Model.DynamicLink

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/7/18.
 */
open class DynamicLinkData {
    @SerializedName("link_doctype")
    var link_doctype = ""
    @SerializedName("link_name")
    var link_name = ""
}