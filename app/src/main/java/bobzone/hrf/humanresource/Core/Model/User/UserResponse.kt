package bobzone.hrf.humanresource.Model.User

import bobzone.hrf.humanresource.Model.BaseResponse
import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 12/23/17.
 */

class UserResponse {

    @SerializedName("home_page")
    var home_page = ""
    @SerializedName("full_name")
    var full_name = ""
    @SerializedName("message")
    var message:String = ""
}
