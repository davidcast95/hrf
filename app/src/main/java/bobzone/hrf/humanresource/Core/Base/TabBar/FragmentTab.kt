package bobzone.hrf.humanresource.Core.Base.TabBar

import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment


/**
 * Created by davidwibisono on 3/6/18.
 */
open class FragmentTab(fragment:Fragment, titleId:Int) {

    var fragment = Fragment()
    var titleId = 0
    var badge = ""
    var icon:Drawable? = null

    init {
        this.fragment = fragment
        this.titleId = titleId
    }
}