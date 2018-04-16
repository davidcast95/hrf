package bobzone.hrf.humanresource.Core.Base.OptionsMenu

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.support.v4.view.MenuItemCompat
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 2/13/18.
 */
class SearchOptionsMenuDelegate {
    var searchItem:MenuItem? = null
    var searchView:SearchView? = null
    lateinit var listener:SearchView.OnQueryTextListener

    fun setup(menu:Menu, context: Context, activity: Activity) {
        searchItem = menu.findItem(R.id.search)
        val si = searchItem
        if (si is MenuItem)
            searchView = si.actionView as SearchView

        val sv = searchView
        if (sv is SearchView) {
            sv.setIconifiedByDefault(true)

            val searchManager = context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            sv.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))
            sv.setOnQueryTextListener(listener)
        }
    }


}