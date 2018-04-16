package bobzone.hrf.humanresource.Model

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 12/23/17.
 */

open class BaseResponse<T : Any> {
    @SerializedName("data")
    var data:MutableList<T> = mutableListOf<T>()
}