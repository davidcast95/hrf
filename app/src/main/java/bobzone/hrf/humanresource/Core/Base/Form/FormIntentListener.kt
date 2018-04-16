package bobzone.hrf.humanresource.Core.Base.Form

import android.content.Intent

/**
 * Created by davidwibisono on 2/16/18.
 */

open interface FormIntentListener {
    fun onIntentResult(data: Intent):String
    fun getField():String
    fun prepareIntent(i:Intent):Intent
}