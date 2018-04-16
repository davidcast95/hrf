package bobzone.hrf.humanresource.Model.Leave

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 4/16/18.
 */
open class RequestLeaveApplicationData {
    @SerializedName("result")
    var result = ""
    @SerializedName("warning_message")
    var warning_message = mutableListOf<String>()
    @SerializedName("error_message")
    var error_message = mutableListOf<String>()
}