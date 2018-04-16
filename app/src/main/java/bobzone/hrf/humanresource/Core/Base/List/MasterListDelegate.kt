package bobzone.hrf.humanresource.Core.Base.List

import android.app.Activity
import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.*
import bobzone.hrf.humanresource.Core.Base.Callback.BaseMessageCallBack
import bobzone.hrf.humanresource.Core.Base.Callback.BaseResponseCallBack
import bobzone.hrf.humanresource.R
import com.paging.listview.PagingListView

/**
 * Created by davidwibisono on 2/13/18.
 */

open class MasterListDelegate<T: Any> : PagingListView.Pagingable, SearchView.OnQueryTextListener {
    var listenerMaster: MasterListEventListener<T>? = null
    var loading:RelativeLayout? = null
    var listView:PagingListView? = null
    var swipeRefresh:SwipeRefreshLayout? = null
    var listAdapter:ArrayAdapter<T>? = null
    var noDataTV:TextView? = null
    var context:Context? = null
    var pagerResponse = 0
    var pagerMessage = 0
    var query = ""


    fun setup(v:View, listAdapter:ArrayAdapter<T>, context: Context) {
        loading = v.findViewById<RelativeLayout>(R.id.loading)
        listView = v.findViewById<PagingListView>(R.id.master_list)
        swipeRefresh = v.findViewById(R.id.swipe_refresh)
        noDataTV = v.findViewById<TextView>(R.id.no_data)
        this.listAdapter = listAdapter
        this.context = context;

        //set swipe refresh
        val _swipeRefresh = swipeRefresh
        if (_swipeRefresh is SwipeRefreshLayout) {
            _swipeRefresh.setOnRefreshListener {
                refreshItems()
            }
        }

        //set adapter
        val adapter = this.listAdapter
        val lv = listView

        if (lv != null) {
            //set listview
            lv.setHasMoreItems(false)
            lv.setPagingableListener(this)


        }
        if (adapter != null && lv != null) {
            lv.adapter = adapter

            lv.setOnItemClickListener(object :AdapterView.OnItemClickListener {
                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val l = listenerMaster
                    if (l is MasterListEventListener) {
                        l.onListTapped(adapter.getItem(p2), p2)
                    }
                }

            })

        }
    }

    fun setup(a:Activity, listAdapter:ArrayAdapter<T>, context: Context) {
        loading = a.findViewById<RelativeLayout>(R.id.loading)
        listView = a.findViewById<PagingListView>(R.id.master_list)
        swipeRefresh = a.findViewById(R.id.swipe_refresh)
        noDataTV = a.findViewById<TextView>(R.id.no_data)
        this.listAdapter = listAdapter
        this.context = context;

        //set swipe refresh
        val _swipeRefresh = swipeRefresh
        if (_swipeRefresh is SwipeRefreshLayout) {
            _swipeRefresh.setOnRefreshListener {
                refreshItems()
            }
        }

        //set adapter
        val adapter = this.listAdapter
        val lv = listView

        if (lv != null) {
            //set listview
            lv.setHasMoreItems(false)
            lv.setPagingableListener(this)


        }
        if (adapter != null && lv != null) {
            lv.adapter = adapter

            lv.setOnItemClickListener(object :AdapterView.OnItemClickListener {
                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val l = listenerMaster
                    if (l is MasterListEventListener) {
                        l.onListTapped(adapter.getItem(p2), p2)
                    }
                }

            })

        }
    }


    override fun onLoadMoreItems() {
        onFetchData()
    }

    fun refreshItems() {
        val el = listenerMaster
        if (el is MasterListEventListener<T>) {
            el.willRefreshItems()
        }
        val adapter = listAdapter
        if (adapter is ArrayAdapter<T>) {
            adapter.clear()
            pagerResponse = 0
            pagerMessage = 0
            onFetchData()
        }
        val _refresh = swipeRefresh
        if (_refresh is SwipeRefreshLayout) {
            _refresh.isRefreshing = false
        }
    }

    fun addItems(newList:List<T>) {
        val adapter = listAdapter
        val lv = listView
        if (adapter is ArrayAdapter<T> && lv is ListView) {
            adapter.addAll(newList)
            if (newList.size > 0) {
                lv.onFinishLoading(true, newList)
            } else {
                lv.onFinishLoading(false,null)
            }

            val _noDataTV = noDataTV
            if (_noDataTV is TextView) {
                if (adapter.count == 0) _noDataTV.visibility = View.VISIBLE
                else _noDataTV.visibility = View.GONE
            }
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
        val _loading = loading
        if (_loading is RelativeLayout && (pagerResponse == 0 || pagerMessage == 0)) _loading.visibility = View.VISIBLE
        val _eventListener = listenerMaster
        if (_eventListener is MasterListEventListener<T>) {
            val c = context
            if (c is Context) {
                val apiHandlingBaseResponse = _eventListener.getAPIHandlingOnBaseResponse(query, pagerResponse++)
                if (apiHandlingBaseResponse != null) {
                    apiHandlingBaseResponse.enqueue(object : BaseResponseCallBack<T>(c) {
                        override fun responseData(data: MutableList<T>) {
                            addItems(data)
                            _eventListener.fetchDidSuccess(data)
                            if (_loading is RelativeLayout) _loading.visibility = View.GONE
                        }

                        override fun failed(error: Throwable) {
                            if (_loading is RelativeLayout) _loading.visibility = View.GONE
                        }
                    })
                }
                val apiHandlingBaseMessage = _eventListener.getAPIHandlingOnBaseMessage(query, pagerMessage++)
                if (apiHandlingBaseMessage != null) {
                    apiHandlingBaseMessage.enqueue(object : BaseMessageCallBack<T>(c) {
                        override fun responseData(data: MutableList<T>) {
                            addItems(data)
                            _eventListener.fetchDidSuccess(data)
                            if (_loading is RelativeLayout) _loading.visibility = View.GONE
                        }

                        override fun failed(error: Throwable) {
                            if (_loading is RelativeLayout) _loading.visibility = View.GONE
                        }
                    })
                }
            }
        }
    }

    fun searchQuery(query:String) {
        this.query = query
        refreshItems()
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        val q = p0
        if (q is String)
            query = q
        refreshItems()
        return true
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }
}