package bobzone.hrf.humanresource.Core.Model.GoogleMap.Places

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 2/17/18.
 */
open class GoogleMapsPlacesAddressComponentData {
    @SerializedName("long_name")
    var long_name:String = ""
    @SerializedName("short_name")
    var short_name:String = ""
    @SerializedName("types")
    var types:List<String> = ArrayList<String>()
}