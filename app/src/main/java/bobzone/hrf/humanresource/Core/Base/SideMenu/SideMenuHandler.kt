package bobzone.hrf.humanresource.Core.Base.SideMenu

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import bobzone.hrf.humanresource.Core.Base.Loading.LoadingHandler
import bobzone.hrf.humanresource.LoginActivity
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 12/24/17.
 */

open class SideMenuHandler : LoadingHandler(), NavigationView.OnNavigationItemSelectedListener, SideMenuDataSource {

    var fragments:MutableList<MenuFragment> = mutableListOf()
    var navigationView:NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationView = findViewById<NavigationView>(R.id.nav_view)

        handler()
        setupMenu()
    }

    fun handler() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        val _navView = navigationView
        if (_navView is NavigationView) {
            _navView.setNavigationItemSelectedListener(this)
        }
    }

    fun setupMenu() {
        val count = getCount()
        for (i in 0 until count) {
            fragments.add(getFragment(i))
            if (i==0) {
                val activeFragment = fragments[i]
                title = activeFragment.title
                supportFragmentManager.beginTransaction().replace(R.id.contentLayout,activeFragment).commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    //NAVIGATION VIEW LISTENER
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val count = fragments.size
        for (i in 0 until count) {
            if (id == R.id.nav_logout) {
                UserModel.instance.destroy(applicationContext)
                val loginIntent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()

            }
            if (id == fragments[i].itemId) {
                val activeFragment = fragments[i]
                title = activeFragment.title
                supportFragmentManager.beginTransaction().replace(R.id.contentLayout,activeFragment).commit()

                val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
                drawer.closeDrawer(GravityCompat.START)

            }

        }
        return true
    }



    //SIDE MENU DATA SOURCE
    override fun getCount(): Int {
        return 0
    }

    override fun getFragment(index:Int): MenuFragment {
        return MenuFragment()
    }
}