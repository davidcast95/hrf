package bobzone.hrf.humanresource.Core.Base.Activity

/**
 * Created by davidwibisono on 2/15/18.
 */

open interface ActivityEventListener {
    fun optionsMenuDidSelected(id:Int):Boolean
    fun getLayout():Int
}