package bobzone.hrf.humanresource.Core.Model.GoogleMap.Places

import bobzone.hrf.humanresource.Core.Model.GoogleMap.GoogleMapsLocation
import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 2/17/18.
 */

open class GoogleMapsPlacesGeometryData {
    @SerializedName("location")
    var location:GoogleMapsLocation = GoogleMapsLocation()
}