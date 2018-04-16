package bobzone.hrf.humanresource.Core.Global

/**
 * Created by davidwibisono on 12/23/17.
 */

class Constant private constructor() {
    private object Holder { val INSTANCE = Constant() }

    companion object {
        val instance: Constant by lazy { Holder.INSTANCE }
    }

    //Global Model
    var SHARED_PREF_GLOBAL_MODEL = "global"
    var SHARED_USER_PREFERENCES = "user_pref"
    var GLOBAL_MODEL_COOKIE_JAR = "mycookiejar"
    var LIMIT_MASTER_LIST = 20
}