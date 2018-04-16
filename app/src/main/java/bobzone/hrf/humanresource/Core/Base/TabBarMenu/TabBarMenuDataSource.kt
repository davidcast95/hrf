package bobzone.hrf.humanresource.Core.Base.TabBarMenu

import android.app.Fragment
import android.graphics.drawable.Drawable
import bobzone.hrf.humanresource.Core.Base.TabBar.FragmentTab

/**
 * Created by davidwibisono on 3/8/18.
 */

open interface TabBarMenuDataSource {
    fun getMenuFragmentCount():Int
    fun getFragmentTab(position:Int):FragmentTab?
}