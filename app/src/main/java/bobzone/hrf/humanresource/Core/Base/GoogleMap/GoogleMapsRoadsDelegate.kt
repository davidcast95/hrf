package bobzone.hrf.humanresource.Core.Base.GoogleMap

import android.content.Context
import bobzone.hrf.humanresource.Core.Global.AppConfiguration
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Core.Model.GoogleMap.Places.GoogleMapsPlacesResponse
import bobzone.hrf.humanresource.Core.Model.GoogleMap.Roads.GoogleMapsRoadsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by davidwibisono on 2/17/18.
 */

open class GoogleMapsRoadsDelegate {
    var context: Context? = null
    var listener:GoogleMapsRoadsListener? = null

    fun setup(context: Context) {
        this.context = context
    }

    fun requestNearestRoads(lat:Double,long:Double) {
        val c = context
        val l = listener
        if (c is Context && l is GoogleMapsRoadsListener) {
            val api = Helper.instance.getGoogleMapsRoadsAPI()
            val callNearestRoads = api.googgleMapsNearestRoads("$lat,$long",AppConfiguration.instance.GOOGLEMAP_API_KEY)
            callNearestRoads.enqueue(object: Callback<GoogleMapsRoadsResponse> {
                override fun onResponse(call: Call<GoogleMapsRoadsResponse>?, response: Response<GoogleMapsRoadsResponse>?) {
                    if (response != null) {
                        val googleResponse = response.body()
                        if (googleResponse is GoogleMapsRoadsResponse) {
                            l.onResultNearestRoads(googleResponse.snappedPoints)
                        }
                    }
                }

                override fun onFailure(call: Call<GoogleMapsRoadsResponse>?, t: Throwable?) {
                    Helper.instance.showConnectivityUnstable(c)
                }
            })
        }
    }
}