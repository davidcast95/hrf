package bobzone.hrf.humanresource.Core.Base.TabBar

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by davidwibisono on 3/6/18.
 */
open class ViewPagerFragmentAdapter(fm:FragmentManager, list:List<FragmentTab>) : FragmentPagerAdapter(fm) {

    var listFragment:List<FragmentTab> = arrayListOf()

    init {
        listFragment = list
    }

    override fun getItem(position: Int): Fragment {
        if (listFragment.size > position)
            return listFragment[position].fragment
        else
            return Fragment()
    }

    override fun getCount(): Int {
        return listFragment.size
    }

}
