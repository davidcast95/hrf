package bobzone.hrf.humanresource.Core.Base.TabBar

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import android.R.attr.label
import android.text.Layout
import android.view.LayoutInflater
import android.R.attr.tag
import android.app.Activity
import android.support.v4.app.FragmentManager
import android.widget.*
import bobzone.hrf.humanresource.R


/**
 * Created by davidwibisono on 3/6/18.
 */
open class TabBarDelegate : TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {


    var viewPager:ViewPager? = null
    var tabHost:TabHost? = null
    var listener:TabBarEventListener? = null

    fun setup(context: Context, viewPagerId:Int,tabHostId:Int, view:View, listFragment:List<FragmentTab>, fragment: Fragment) {
        viewPager = view.findViewById(viewPagerId)
        val viewPagerAdapter = ViewPagerFragmentAdapter(fragment.childFragmentManager,listFragment)
        val vp = viewPager
        if (vp is ViewPager) {
            vp.adapter = viewPagerAdapter
            vp.setOnPageChangeListener(this)
        }
        tabHost = view.findViewById(tabHostId)
        val th = tabHost
        if (th is TabHost) {
            th.setup()
            listFragment.forEach { ft ->
                var title = ""
                if (ft.titleId != 0) title = fragment.getString(ft.titleId)
                title += " ${ft.badge}"
                val icon = ft.icon
                if (icon is Drawable) {
                    val spec = th.newTabSpec(title)
                    val tabIndicator = LayoutInflater.from(context).inflate(R.layout.tab_bar_default, th.tabWidget, false)
                    val img = tabIndicator.findViewById<ImageView>(R.id.icon)
                    val tabTitle = tabIndicator.findViewById<TextView>(R.id.tab_title)
                    tabTitle.setText(title)
                    img.setImageDrawable(icon)
                    spec.setIndicator(tabIndicator)
                    spec.setContent(FakeContent(context))
                    th.addTab(spec)
                } else {
                    val spec = th.newTabSpec(title)
                    spec.setIndicator(title)
                    spec.setContent(FakeContent(context))
                    th.addTab(spec)
                }

            }

            th.setOnTabChangedListener(this)
        }

    }

    fun setup(context: Context, viewPagerId:Int,tabHostId:Int, view:Activity, listFragment:List<FragmentTab>, fm: FragmentManager) {
        viewPager = view.findViewById(viewPagerId)
        val viewPagerAdapter = ViewPagerFragmentAdapter(fm,listFragment)
        val vp = viewPager
        if (vp is ViewPager) {
            vp.adapter = viewPagerAdapter
            vp.setOnPageChangeListener(this)
        }
        tabHost = view.findViewById(tabHostId)
        val th = tabHost
        if (th is TabHost) {
            th.setup()
            listFragment.forEach { ft ->
                var title = ""
                if (ft.titleId != 0) title = view.getString(ft.titleId)
                title += " ${ft.badge}"
                val icon = ft.icon
                if (icon is Drawable) {
                    val spec = th.newTabSpec(title)
                    val tabIndicator = LayoutInflater.from(context).inflate(R.layout.tab_bar_default, th.tabWidget, false)

                    val tabItem = tabIndicator.findViewById<LinearLayout>(R.id.tab_item)
                    val w = view.window.windowManager.defaultDisplay.width
                    tabItem.layoutParams.width = w  / listFragment.size
                    val img = tabIndicator.findViewById<ImageView>(R.id.icon)
                    val tabTitle = tabIndicator.findViewById<TextView>(R.id.tab_title)
                    tabTitle.setText(title)
                    img.setImageDrawable(icon)
                    spec.setIndicator(tabIndicator)
                    spec.setContent(FakeContent(context))
                    th.addTab(spec)
                } else {
                    val spec = th.newTabSpec(title)
                    spec.setIndicator(title)
                    spec.setContent(FakeContent(context))
                    th.addTab(spec)
                }

            }

            th.setOnTabChangedListener(this)
        }

    }

    internal inner class FakeContent(var context: Context) : TabHost.TabContentFactory {
        override fun createTabContent(s: String): View {
            val fakeView = View(context)
            fakeView.minimumHeight = 0
            fakeView.minimumHeight = 0
            return fakeView
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        val th = tabHost
        if (th is TabHost) {
            th.currentTab = position
        }
    }

    override fun onTabChanged(p0: String?) {
        val th = tabHost
        val vp = viewPager
        if (th is TabHost && vp is ViewPager) {
            val pos = th.currentTab
            vp.setCurrentItem(pos,false)
            val l = listener
            if (l is TabBarEventListener) {
                l.pageChange(pos)
            }
        }
    }

}