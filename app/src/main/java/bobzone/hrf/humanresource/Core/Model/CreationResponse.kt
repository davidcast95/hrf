package bobzone.hrf.humanresource.Core.Model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

/**
 * Created by davidwibisono on 2/15/18.
 */

open class CreationResponse {
    @SerializedName("data")
    var data = JsonObject()
}