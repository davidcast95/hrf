package bobzone.hrf.humanresource.Core.Base.Location

import com.google.android.gms.maps.GoogleMap

/**
 * Created by davidwibisono on 2/17/18.
 */

open interface LocationServicesListener {
    fun onLocationChange(lat:Double,lng:Double, gm:GoogleMap)
    fun googleMapReady(gm:GoogleMap)
}