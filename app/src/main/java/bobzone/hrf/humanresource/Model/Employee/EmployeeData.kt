package bobzone.hrf.humanresource.Model.Customer

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 2/13/18.
 */


open class EmployeeData {
    @SerializedName("name")
    var name = ""
    @SerializedName("employee_name")
    var employee_name:String = ""

    @SerializedName("date_of_joining")
    var date_of_joining:String = ""
    @SerializedName("status")
    var status:String = ""


    @SerializedName("company")
    var company:String = ""

    @SerializedName("holiday_list")
    var holiday_list = "Libur Minggu"
}