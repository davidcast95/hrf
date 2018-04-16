package bobzone.hrf.humanresource.Core.Base.SideMenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by davidwibisono on 12/24/17.
 */

interface MenuFragmentDataSource {
    fun getView(inflater: LayoutInflater, container: ViewGroup): View
    fun getTitleMenu():String
}