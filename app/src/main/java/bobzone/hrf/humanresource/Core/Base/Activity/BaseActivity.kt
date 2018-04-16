package bobzone.hrf.humanresource.Core.Base.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import bobzone.hrf.humanresource.Core.Application.ActivityHandler
import bobzone.hrf.humanresource.Core.Base.OptionsMenu.OptionsMenuDataSource
import bobzone.hrf.humanresource.Core.Base.OptionsMenu.SearchOptionsMenuDelegate
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 12/24/17.
 */

open class BaseActivity: ActivityHandler(), ActivityEventListener, OptionsMenuDataSource {


    override fun onCreate(savedInstanceState: Bundle?) {
        Helper.instance.getLanguage(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuID = getOptionsMenu()
        if (menuID is Int) {
            menuInflater.inflate(menuID, menu)
            val m = menu
            val sm = getSearchOptionsMenu()
            if (m is Menu && sm is SearchOptionsMenuDelegate)
                sm.setup(menu, applicationContext, this)
            return true
        }
        return false
    }

    override fun getLayout(): Int {
        return R.layout.activity_base
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val _item = item
        if (_item is MenuItem) {
            val id = _item.itemId
            if (optionsMenuDidSelected(id))
                return true
            when (id) {
                android.R.id.home -> {
                    this.finish()
                    return true
                }
                else -> {
                    return false
                }
            }
        }
        return false
    }


    override fun getOptionsMenu(): Int? {
        return null
    }

    override fun getSearchOptionsMenu(): SearchOptionsMenuDelegate? {
        return SearchOptionsMenuDelegate()
    }

    override fun optionsMenuDidSelected(id: Int):Boolean {
        return false
    }
}