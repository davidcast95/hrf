package bobzone.hrf.humanresource.Core.Model.GoogleMap.Roads

import bobzone.hrf.humanresource.Core.Model.GoogleMap.GoogleMapsLocation
import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 2/17/18.
 */

open class GoogleMapRoadsData {
    @SerializedName("location")
    var location:GoogleMapsLocation = GoogleMapsLocation()
    @SerializedName("originalIndex")
    var originalIndex:Int = 0
    @SerializedName("placeId")
    var placeId:String = ""
}