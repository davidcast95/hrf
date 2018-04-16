package bobzone.hrf.humanresource.Core.Base.Maps

import com.google.android.gms.maps.GoogleMap

/**
 * Created by davidwibisono on 2/16/18.
 */
open interface MapEventListener {
    fun googleMapReady(googleMap: GoogleMap)
}