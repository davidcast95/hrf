package bobzone.hrf.humanresource.Core.Model.GoogleMap.Places

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 2/17/18.
 */

open class GoogleMapsPlacesResponse {
    @SerializedName("next_page_token")
    var next_page_token:String = ""

    @SerializedName("results")
    var results:List<GoogleMapsPlacesData> = ArrayList<GoogleMapsPlacesData>()
}