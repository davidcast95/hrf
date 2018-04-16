package bobzone.hrf.humanresource.Model.Leave

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 4/16/18.
 */
open class LeaveApplicationData {
    @SerializedName("name")
    var leave_no = ""

    @SerializedName("employee_name")
    var employee_name = ""

    @SerializedName("status")
    var status = ""

    @SerializedName("leave_type")
    var leave_type = ""

    @SerializedName("from_date")
    var from_date = ""
    @SerializedName("to_date")
    var to_date = ""

    @SerializedName("leave_approver")
    var leave_approver = ""

    @SerializedName("description")
    var description = ""

    @SerializedName("half_day")
    var half_day = 0
    @SerializedName("total_leave_days")
    var total_leave_days = 0


}