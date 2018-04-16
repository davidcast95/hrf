package bobzone.hrf.humanresource.Core.Base.Loading

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/9/18.
 */
open class LoadingDelegate {

    var loading:RelativeLayout? = null
    var loadingIndicator:TextView? = null
    var context:Context? = null

    fun setup(v: View, c: Context) {
        loading = v.findViewById<RelativeLayout>(R.id.loading_view)
        loadingIndicator = v.findViewById<TextView>(R.id.loading_indicator_text)
        context = c
    }
    fun setup(a: Activity, c: Context) {
        loading = a.findViewById<RelativeLayout>(R.id.loading_view)
        loadingIndicator = a.findViewById<TextView>(R.id.loading_indicator_text)
        context = c
    }

    fun setLoading(view: Boolean) {
        val _loading = loading
        if (_loading is RelativeLayout) {
            if (view) {
                _loading.alpha = 1.0F
                _loading.visibility = View.VISIBLE
            } else {
                _loading.alpha = 0.0F
                _loading.visibility = View.GONE
            }
        }
    }

    fun hasLoading(): Boolean {
        val _loading = loading
        if (_loading is RelativeLayout) {
            return _loading.visibility == View.VISIBLE
        }
        return false
    }

    fun startLoading() {
        val _loading = loading
        if (_loading is RelativeLayout) {
            _loading.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
            _loading.startAnimation(animation)

        }
    }
    fun stopLoading() {
        val _loading = loading
        if (_loading is RelativeLayout) {
            _loading.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {

                }

                override fun onAnimationStart(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    _loading.visibility = View.GONE
                }

            })
            _loading.startAnimation(animation)

        }
    }
}