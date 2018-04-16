package bobzone.hrf.humanresource.Core.Base.GoogleMap

import bobzone.hrf.humanresource.Core.Model.GoogleMap.Places.GoogleMapsPlaceDetailsData
import bobzone.hrf.humanresource.Core.Model.GoogleMap.Places.GoogleMapsPlacesData
import bobzone.hrf.humanresource.Core.Model.GoogleMap.Places.GoogleMapsPlacesResponse
import bobzone.hrf.humanresource.Core.Model.GoogleMap.Roads.GoogleMapRoadsData

/**
 * Created by davidwibisono on 2/17/18.
 */

open interface GoogleMapsRoadsListener {
    fun onResultNearestRoads(data:List<GoogleMapRoadsData>)
}