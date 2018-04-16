package bobzone.hrf.humanresource.Model.Leave

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 4/16/18.
 */
open class LeaveTypeData {
    @SerializedName("name")
    var name = ""
    @SerializedName("is_lwp")
    var is_lwp = 0
}