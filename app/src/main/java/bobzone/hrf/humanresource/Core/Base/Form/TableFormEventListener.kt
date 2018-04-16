package bobzone.hrf.humanresource.Core.Base.Form

import android.content.Intent
import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**
 * Created by davidwibisono on 3/7/18.
 */
open interface TableFormEventListener<T:Any> {
    fun getItem(i:Intent):T
    fun getField(jsonArray: JsonArray)
    fun prepareIntent(i:Intent):Intent
    fun onListTapped(item:T,positing:Int)
    fun itemDidUpdate()
    fun shouldAddItem():Boolean
}