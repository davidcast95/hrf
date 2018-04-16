package bobzone.hrf.humanresource.Core.Base.TabBarMenu

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent


/**
 * Created by davidwibisono on 3/10/18.
 */
open class UnswipeableViewPager(context: Context,attrs:AttributeSet) : ViewPager(context, attrs) {

    var isSwipeable = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return this.isSwipeable && super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return this.isSwipeable && super.onInterceptTouchEvent(event)
    }



}
