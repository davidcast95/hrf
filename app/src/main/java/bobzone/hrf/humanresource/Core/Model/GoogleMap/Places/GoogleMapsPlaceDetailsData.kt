package bobzone.hrf.humanresource.Core.Model.GoogleMap.Places

import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 2/17/18.
 */

open class GoogleMapsPlaceDetailsData {
    @SerializedName("address_components")
    var address_components:List<GoogleMapsPlacesAddressComponentData> = ArrayList<GoogleMapsPlacesAddressComponentData>()
    @SerializedName("geometry")
    var geometry:GoogleMapsPlacesGeometryData = GoogleMapsPlacesGeometryData()
    @SerializedName("formatted_address")
    var formatted_address:String = ""
    @SerializedName("icon")
    var icon:String = ""
    @SerializedName("id")
    var id:String = ""
    @SerializedName("name")
    var name:String = ""
    @SerializedName("place_id")
    var place_id:String = ""
    @SerializedName("reference")
    var reference:String = ""
    @SerializedName("scope")
    var scope:String = ""
    @SerializedName("types")
    var types:List<String> = ArrayList<String>()
    @SerializedName("url")
    var url:String = ""
}