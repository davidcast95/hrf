package bobzone.hrf.humanresource.Core.Model.GoogleMap

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 2/17/18.
 */

open class GoogleMapsLocation {
    @SerializedName("latitude")
    var lat:Double = 0.0
    @SerializedName("longitude")
    var lng:Double = 0.0
}