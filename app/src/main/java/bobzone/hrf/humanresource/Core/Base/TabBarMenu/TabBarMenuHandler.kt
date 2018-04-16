package bobzone.hrf.humanresource.Core.Base.TabBarMenu

import android.app.Fragment
import android.os.Bundle
import bobzone.hrf.humanresource.Core.Base.Loading.LoadingHandler
import bobzone.hrf.humanresource.Core.Base.TabBar.FragmentTab
import bobzone.hrf.humanresource.Core.Base.TabBar.TabBarDelegate
import bobzone.hrf.humanresource.Core.Base.TabBar.TabBarEventListener
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/8/18.
 */
open class TabBarMenuHandler : LoadingHandler(), TabBarMenuDataSource, TabBarEventListener {


    val tabBarDelegate = TabBarDelegate()

    var listFragment = arrayListOf<FragmentTab>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_bar_menu_default)

        for (i in 0 until getMenuFragmentCount()) {
            val ft = getFragmentTab(i)
            if (ft is FragmentTab)
                listFragment.add(ft)
        }


        tabBarDelegate.setup(applicationContext, R.id.view_pager, R.id.tabhost, this, listFragment, supportFragmentManager)

        tabBarDelegate.listener = this
    }

    override fun getMenuFragmentCount(): Int {
        return 0
    }

    override fun getFragmentTab(position: Int): FragmentTab? {
        return null
    }

    override fun pageChange(pos: Int) {
        setTitle(listFragment[pos].titleId)
    }
}
