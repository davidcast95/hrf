package bobzone.hrf.humanresource.Core.Model.GoogleMap.Places

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 2/17/18.
 */

open class GoogleMapsPlacesData {
    @SerializedName("geometry")
    var geometry:GoogleMapsPlacesGeometryData = GoogleMapsPlacesGeometryData()
    @SerializedName("icon")
    var icon:String = ""
    @SerializedName("id")
    var id:String = ""
    @SerializedName("name")
    var name:String = ""
    @SerializedName("place_id")
    var place_id:String = ""
    @SerializedName("rating")
    var rating:Float = 0.0f
    @SerializedName("reference")
    var reference:String = ""
    @SerializedName("scope")
    var scope:String = ""
    @SerializedName("types")
    var types:List<String> = ArrayList<String>()
    @SerializedName("vicinity")
    var vicinity:String = ""
}