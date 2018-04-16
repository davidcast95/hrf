package bobzone.hrf.humanresource.Core.Base.SideMenu

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import bobzone.hrf.humanresource.Core.Base.OptionsMenu.OptionsMenuDataSource
import bobzone.hrf.humanresource.Core.Base.OptionsMenu.SearchOptionsMenuDelegate
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 12/24/17.
 */

open class MenuFragment : Fragment(), MenuFragmentDataSource, OptionsMenuDataSource {
    var itemId = 0
    var title = ""
    var menu:SideMenuHandler? = null
    var first = true

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Helper.instance.getLanguage(activity)

        setHasOptionsMenu(getOptionsMenu() != null)

        val menu = getSupportedMenu()
        if (menu is SideMenuHandler) {
            menu.title = getTitleMenu()
        }
        val _inflater = inflater
        val _container = container
        if (_inflater is LayoutInflater && _container is ViewGroup) {
            return getView(_inflater, _container)
        }
        else
            return null
    }

    override fun getView(inflater:LayoutInflater, container: ViewGroup):View {
        val v = inflater.inflate(R.layout.app_fragment_default, container,false)
        return v
    }

    override fun getTitleMenu(): String {
        return "Fragment"
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        val menuRes = getOptionsMenu()
        if (menuRes is Int) {
            val _inflater = inflater
            if (_inflater is MenuInflater)
                _inflater.inflate(menuRes,menu)
            val m = menu
            val sm = getSearchOptionsMenu()
            if (m is Menu && sm is SearchOptionsMenuDelegate)
                sm.setup(menu, context, activity)
        }
    }


    override fun getOptionsMenu(): Int? {
        return null
    }

    override fun getSearchOptionsMenu(): SearchOptionsMenuDelegate? {
        return SearchOptionsMenuDelegate()
    }


    fun getSupportedMenu(): SideMenuHandler? {
        return menu
    }






}