package bobzone.hrf.humanresource.Core.Base.Location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import bobzone.hrf.humanresource.Core.Base.Maps.MapDelegate
import bobzone.hrf.humanresource.Core.Base.Maps.MapEventListener
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

/**
 * Created by davidwibisono on 2/16/18.
 */

open class LocationServicesDelegate :
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    var mapDelegate:MapDelegate? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var mLocationRequest: LocationRequest? = null
    var context:Context? = null
    var activity:Activity? = null
    val UPDATE_INTERVAL = (1000).toLong()
    val FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2
    val MAX_WAIT_TIME = UPDATE_INTERVAL


    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    var lat:Double = 0.0
    var long:Double = 0.0
    var isMyLocationEnabled = true
    var listener:LocationServicesListener? = null
    var navigationMode = true


    fun setup(mapDelegate: MapDelegate, context: Context, activity:Activity) {
        this.context = context
        this.activity = activity
        mapDelegate.mapEventListener = object :MapEventListener {
            override fun googleMapReady(googleMap: GoogleMap) {
                val l = listener
                if (l is LocationServicesListener) {
                    l.googleMapReady(googleMap)
                }
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkLocationPermission()
                }
            }
        }
        this.mapDelegate = mapDelegate

    }



    fun checkLocationPermission(): Boolean {
        val c = context
        val a = activity
        if (c is Context && a is Activity) {
            if (ContextCompat.checkSelfPermission(c,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Asking user if explanation is needed
                if (ActivityCompat.shouldShowRequestPermissionRationale(a,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    //Prompt the user once explanation has been shown
                    ActivityCompat.requestPermissions(a,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION)


                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(a,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION)
                }
                return false
            } else {
                buildGoogleApiClient()

                val md = mapDelegate
                if (md is MapDelegate) {
                    val googleMap = md.gMap
                    if (googleMap is GoogleMap) {
                        googleMap.isMyLocationEnabled = isMyLocationEnabled
                    }
                }
                return true
            }
        }
        return false
    }

    fun permissionResult(requestCode:Int, grantResults:IntArray) {
        val c = context
        if (c is Context) {
            when (requestCode) {
                MY_PERMISSIONS_REQUEST_LOCATION -> {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // Permission was granted.
                        if (ContextCompat.checkSelfPermission(c,
                                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            buildGoogleApiClient()

                            val md = mapDelegate
                            if (md is MapDelegate) {
                                val googleMap = md.gMap
                                if (googleMap is GoogleMap) {
                                    googleMap.isMyLocationEnabled = isMyLocationEnabled
                                }
                            }
                        }

                    } else {

                        // Permission denied, Disable the functionality that depends on this permission.
                    }
                    return
                }
            }
        }

    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        val c = context
        if (c is Context && mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(c)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
            val googleApiClient = mGoogleApiClient
            if (googleApiClient is GoogleApiClient) {
                googleApiClient.connect()
                createLocationRequest()
            }
        }
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnected(p0: Bundle?) {
        requestLocationUpdate()
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        val locationRequest = mLocationRequest
        if (locationRequest is LocationRequest) {
            locationRequest.setInterval(UPDATE_INTERVAL)

            // Sets the fastest standard_rate for active location updates. This interval is exact, and your
            // application will never receive updates faster than this value.
            locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL)
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

            // Sets the maximum time when batched location updates are delivered. Updates may be
            // delivered sooner than this interval.
            locationRequest.setMaxWaitTime(MAX_WAIT_TIME)
        }
    }

    fun requestLocationUpdate() {
        val googleApiClient = mGoogleApiClient
        val locationRequest = mLocationRequest
        if (googleApiClient is GoogleApiClient && locationRequest is LocationRequest) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }


    }

    override fun onLocationChanged(p0: Location?) {
        val location = p0
        val md = mapDelegate
        val l = listener
        if (location is Location && md is MapDelegate && l is LocationServicesListener) {
            long = location.longitude
            lat = location.latitude
            val gmap = md.gMap
            if (gmap is GoogleMap) {
                l.onLocationChange(lat,long,gmap)
                if (navigationMode)
                    gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat,long),18f))
            }
        }
    }
}