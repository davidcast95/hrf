package bobzone.hrf.humanresource.Model

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 12/23/17.
 */

open class BaseMessage<T : Any>  {
    @SerializedName("message")
    var data = mutableListOf<T>()
}