package bobzone.hrf.humanresource.Core.Base.GoogleMap

import android.content.Context
import bobzone.hrf.humanresource.Core.Global.AppConfiguration
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Core.Model.GoogleMap.Places.GoogleMapsPlaceDetailsResponse
import bobzone.hrf.humanresource.Core.Model.GoogleMap.Places.GoogleMapsPlacesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by davidwibisono on 2/17/18.
 */
open class GoogleMapsPlacesDelegate {
    val RADIUS = 500
    var context:Context? = null
    var listener:GoogleMapsPlacesListener? = null

    fun setup(context: Context) {
        this.context = context
    }

    fun requestNearbySearch(lat:Double,long:Double,type:String,keyword:String) {
        val c = context
        val l = listener
        if (c is Context && l is GoogleMapsPlacesListener) {
            val api = Helper.instance.getGoogleMapsAPI()
            val callNearbySearch = api.googleMapsNearbySearch("$lat,$long","$RADIUS",type,keyword,AppConfiguration.instance.GOOGLEMAP_API_KEY)
            callNearbySearch.enqueue(object : Callback<GoogleMapsPlacesResponse> {
                override fun onResponse(call: Call<GoogleMapsPlacesResponse>?, response: Response<GoogleMapsPlacesResponse>?) {
                    if (response != null) {
                        val googleResponse = response.body()
                        if (googleResponse is GoogleMapsPlacesResponse) {
                            l.onResultNearbySearch(googleResponse.results)
                        }
                    }
                }

                override fun onFailure(call: Call<GoogleMapsPlacesResponse>?, t: Throwable?) {
                    Helper.instance.showConnectivityUnstable(c)
                }
            })
        }

    }

    fun requestPlaceDetails(placeId:String) {
        val c = context
        val l = listener
        if (c is Context && l is GoogleMapsPlacesListener) {
            val api = Helper.instance.getGoogleMapsAPI()
            val callPlaceDetails = api.googgleMapsPlaceDetails(placeId,AppConfiguration.instance.GOOGLEMAP_API_KEY)
            callPlaceDetails.enqueue(object :Callback<GoogleMapsPlaceDetailsResponse> {
                override fun onResponse(call: Call<GoogleMapsPlaceDetailsResponse>?, response: Response<GoogleMapsPlaceDetailsResponse>?) {
                    if (response != null) {
                        val googleResponse = response.body()
                        if (googleResponse is GoogleMapsPlaceDetailsResponse) {
                            l.onResultPlaceDetails(googleResponse.result)
                        }
                    }
                }

                override fun onFailure(call: Call<GoogleMapsPlaceDetailsResponse>?, t: Throwable?) {
                    Helper.instance.showConnectivityUnstable(c)
                }
            })
        }
    }
}