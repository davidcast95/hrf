package bobzone.hrf.humanresource.Core.Global


/**
 * Created by davidwibisono on 12/23/17.
 */

class AppConfiguration private constructor() {
    private object Holder { val INSTANCE = AppConfiguration() }

    companion object {
        val instance: AppConfiguration by lazy { Holder.INSTANCE }
    }

    val EXPIRY_APP = "2018-06-19"

    //GOOGLE_MAP
    val GOOGLEMAP_API_KEY = "AIzaSyANB6Qnc8JSkOmpUIXrYU2j7S2DqVNU-Bc"
    val GOOGLEMAP_URL = "https://maps.googleapis.com"
    val GOOGLEMAP_ROADS_URL = "https://roads.googleapis.com"

    //BASEURL
//    val BASE_PROD_URL = "http://172.104.44.22"
//    val BASE_PROD_URL = "http://inviplus.invifo.com"

    //Mirage
//    val BASE_DEV_URL = "http://139.162.32.245"
//    val BASE_PROD_URL = "http://139.162.35.24"


//    val BASE_DEV_URL = "http://inviplus.invifo.com"
//    val BASE_PROD_URL = "http://inviplus.invifo.com"

    val BASE_DEV_URL = "http://172.104.44.22"
    val BASE_PROD_URL = "http://172.104.44.22"


    var BASE_URL = BASE_DEV_URL


    //INTERVAL TIME
    val SPLASH_SCREEN_INTERVAL_TIME:Long = 1000
    val TOAST_INTERVAL_TIME:Int = 3000
}