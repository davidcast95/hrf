package bobzone.hrf.humanresource.Core.Base.List

import android.content.Context
import android.view.View
import android.widget.*
import bobzone.hrf.humanresource.Core.Base.Callback.BaseResponseCallBack
import bobzone.hrf.humanresource.Model.BaseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by davidwibisono on 2/13/18.
 */

open class BasicListDelegate<T: Any> {
    var listenerMaster: BasicListEventListener<T>? = null
    var listView:ListView? = null
    var listAdapter:ArrayAdapter<T>? = null
    var context:Context? = null


    fun setup(v:View, listLayout:Int, listAdapter:ArrayAdapter<T>, context: Context) {
        listView = v.findViewById<ListView>(listLayout)
        this.listAdapter = listAdapter
        this.context = context;

        //set adapter
        val adapter = this.listAdapter
        val lv = listView

        if (adapter != null && lv != null) {
            lv.adapter = adapter
        }
    }


    fun addItems(newList:List<T>) {
        val adapter = listAdapter
        val lv = listView
        if (adapter is ArrayAdapter<T> && lv is ListView) {
            adapter.addAll(newList)
        }

    }

    fun populateList(list:List<T>) {
        val adapter = listAdapter
        val lv = listView
        if (adapter is ArrayAdapter<T> && lv is ListView) {
            adapter.clear()
            adapter.addAll(list)
        }
    }



    fun onFetchData() {
        val _eventListener = listenerMaster
        if (_eventListener is BasicListEventListener<T>) {
            val apiHandling = _eventListener.getAPIHandlingOn()
            if (apiHandling is Call<BaseResponse<T>>) {
                val c = context
                if (c is Context) {
                    apiHandling.enqueue(object : BaseResponseCallBack<T>(c) {
                        override fun responseData(data: MutableList<T>) {
                            addItems(data)
                        }

                        override fun failed(error: Throwable) {

                        }
                    })
                }

            }
        }
    }

}