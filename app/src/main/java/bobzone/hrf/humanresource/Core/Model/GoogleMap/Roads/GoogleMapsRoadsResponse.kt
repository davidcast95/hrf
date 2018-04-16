package bobzone.hrf.humanresource.Core.Model.GoogleMap.Roads

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 2/17/18.
 */

open class GoogleMapsRoadsResponse {
    @SerializedName("snappedPoints")
    var snappedPoints:List<GoogleMapRoadsData> = ArrayList<GoogleMapRoadsData>()
}