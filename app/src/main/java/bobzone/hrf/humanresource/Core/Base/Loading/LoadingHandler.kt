package bobzone.hrf.humanresource.Core.Base.Loading

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Base.Activity.BaseActivity
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.R
import retrofit2.Call
import retrofit2.Callback

/**
 * Created by davidwibisono on 12/24/17.
 */

open class LoadingHandler : BaseActivity(), LoadingListener {

    var callhandling:Call<Any>? = null
    var callback:Callback<Any>? = null
    var loading:RelativeLayout? = null
    var loadingIndicator:TextView? = null
    var loadingEventListener:LoadingEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Helper.instance.getLanguage(this)
        super.onCreate(savedInstanceState)
        loading = findViewById<RelativeLayout>(R.id.loading_view)
        loadingIndicator = findViewById<TextView>(R.id.loading_indicator_text)
        setLoading(false)
    }

    override fun setLoadingText(text:String) {
        val _loadingIndicator = loadingIndicator
        if (_loadingIndicator is TextView) {
            _loadingIndicator.text = text
        }
    }



    override fun setLoading(view: Boolean) {
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

    override fun hasLoading(): Boolean {
        val _loading = loading
        if (_loading is RelativeLayout) {
            return _loading.visibility == View.VISIBLE
        }
        return false
    }

    override fun startLoading() {
        val _loading = loading
        if (_loading is RelativeLayout) {
            _loading.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(this@LoadingHandler, R.anim.fade_in)
            _loading.startAnimation(animation)
            val l = loadingEventListener
            if (l is LoadingEventListener) {
                l.loadingDidStart()
            }
        }
    }

    override fun stopLoading() {
        val _loading = loading
        if (_loading is RelativeLayout) {
            _loading.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
            animation.setAnimationListener(object :Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {

                }

                override fun onAnimationStart(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    _loading.visibility = View.GONE
                    val l = loadingEventListener
                    if (l is LoadingEventListener) {
                        l.loadingDidStop()
                    }
                }

            })
            _loading.startAnimation(animation)

        }
    }


}