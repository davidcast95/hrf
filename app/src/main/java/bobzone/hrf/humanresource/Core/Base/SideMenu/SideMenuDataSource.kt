package bobzone.hrf.humanresource.Core.Base.SideMenu

/**
 * Created by davidwibisono on 12/24/17.
 */

interface SideMenuDataSource {
    fun getCount() : Int
    fun getFragment(index:Int) : MenuFragment
}