package bobzone.hrf.humanresource.Core.Base.Activity

import android.os.Bundle
import bobzone.hrf.humanresource.Core.Base.Location.LocationServicesDelegate
import bobzone.hrf.humanresource.Core.Base.Maps.MapDelegate
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 2/16/18.
 */

open class MapActivity : BaseActivity() {

    var locationServicesDelegate = LocationServicesDelegate()
    var mapDelegate = MapDelegate()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_default)
        mapDelegate.setup(R.id.map,this,applicationContext)
        locationServicesDelegate.setup(mapDelegate, applicationContext,this)

    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        locationServicesDelegate.permissionResult(requestCode,grantResults)
    }
}