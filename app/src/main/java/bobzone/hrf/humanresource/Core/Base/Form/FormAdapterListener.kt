package bobzone.hrf.humanresource.Core.Base.Form

import android.widget.ListView

/**
 * Created by davidwibisono on 2/15/18.
 */

open interface FormAdapterListener {
    fun getField(position:Int, lv:ListView):String
    fun setField(position:Int,text:String, lv:ListView)
}