package bobzone.hrf.humanresource.Model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

/**
 * Created by davidwibisono on 12/23/17.
 */

open class BaseSingleResponse<T : Any> {
    @SerializedName("data")
    var data:T? = null
}