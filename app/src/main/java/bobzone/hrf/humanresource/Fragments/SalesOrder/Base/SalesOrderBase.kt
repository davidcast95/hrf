package bobzone.hrf.humanresource.Fragments.SalesOrder.Base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Base.List.MasterListDelegate
import bobzone.hrf.humanresource.Core.Base.List.MasterListEventListener
import bobzone.hrf.humanresource.Fragments.SalesOrder.Active.SalesOrderActive
import bobzone.hrf.humanresource.Fragments.SalesOrder.ViewSalesOrder.ViewSalesOrder
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderData
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderStatus
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.Model.UserSettings
import bobzone.hrf.humanresource.R
import com.google.gson.Gson
import retrofit2.Call

/**
 * Created by davidwibisono on 3/6/18.
 */
open class SalesOrderBase : Fragment(), MasterListEventListener<SalesOrderData> {

    var generatedView:View? = null
    var masterListDelegate: MasterListDelegate<SalesOrderData> = MasterListDelegate()
    var salesOrderList: List<SalesOrderData> = ArrayList<SalesOrderData>()
    var status = ""
    var toDeliverAndBillToggle = true
    var toBillToggle = true
    var toDeliverToggle = true

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val infl = inflater
        if (infl is LayoutInflater) {
            val v = inflater.inflate(R.layout.fragment_sales_order_base, container, false)
            generatedView = v
            setupStatusToggle(v, context)

            val salesOrderAdapter = SalesOrderAdapter(context, salesOrderList)
            masterListDelegate.setup(v, salesOrderAdapter, context)
            masterListDelegate.listenerMaster = this



            masterListDelegate.onFetchData()
            return v
        }
        return view
    }

    fun setupStatusToggle(v:View,c:Context) {
        val statusToggle = v.findViewById<LinearLayout>(R.id.status_toggle)
        if (this is SalesOrderActive) {
            statusToggle.visibility = View.VISIBLE

            toDeliverAndBillToggle = UserModel.instance.getUserPreferencesAsBoolean(c,UserSettings.instance.SALES_ORDER_ACTIVE_TO_DELIVER_AND_BILL_TOGGLE,toDeliverAndBillToggle)
            toBillToggle = UserModel.instance.getUserPreferencesAsBoolean(c,UserSettings.instance.SALES_ORDER_ACTIVE_TO_BILL_TOGGLE,toBillToggle)
            toDeliverToggle = UserModel.instance.getUserPreferencesAsBoolean(c,UserSettings.instance.SALES_ORDER_ACTIVE_TO_DELIVER_TOGGLE,toDeliverToggle)

            val toDeliverAndBillToggleView = v.findViewById<TextView>(R.id.to_deliver_and_bill_toggle)
            val toBillToggleView = v.findViewById<TextView>(R.id.to_bill_toggle)
            val toDeliverToggleView = v.findViewById<TextView>(R.id.to_deliver_toggle)

            setupAlpha(toDeliverAndBillToggleView,toDeliverAndBillToggle)
            setupAlpha(toBillToggleView,toBillToggle)
            setupAlpha(toDeliverToggleView,toDeliverToggle)

            toDeliverAndBillToggleView.setOnClickListener(object:View.OnClickListener {
                override fun onClick(p0: View?) {
                    toDeliverAndBillToggle = !toDeliverAndBillToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c,UserSettings.instance.SALES_ORDER_ACTIVE_TO_DELIVER_AND_BILL_TOGGLE,toDeliverAndBillToggle)
                    setupAlpha(toDeliverAndBillToggleView,toDeliverAndBillToggle)
                    updateStatus()
                }
            })
            toBillToggleView.setOnClickListener(object:View.OnClickListener {
                override fun onClick(p0: View?) {
                    toBillToggle = !toBillToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c,UserSettings.instance.SALES_ORDER_ACTIVE_TO_BILL_TOGGLE,toBillToggle)
                    setupAlpha(toBillToggleView,toBillToggle)
                    updateStatus()
                }
            })
            toDeliverToggleView.setOnClickListener(object:View.OnClickListener {
                override fun onClick(p0: View?) {
                    toDeliverToggle = !toDeliverToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c,UserSettings.instance.SALES_ORDER_ACTIVE_TO_DELIVER_TOGGLE,toDeliverToggle)
                    setupAlpha(toDeliverToggleView,toDeliverToggle)
                    updateStatus()
                }
            })
            updateStatus()

        } else {
            statusToggle.visibility = View.GONE
        }
    }

    fun setupAlpha(v:TextView,toggle:Boolean) {
        if (toggle) {
            v.alpha = 1f
        } else {
            v.alpha = 0.5f
        }
    }

    fun updateStatus() {
        if (this is SalesOrderActive) {
            status = ""
            if (toDeliverAndBillToggle) status += "${SalesOrderStatus.instance.TO_DELIVER_AND_BILL},"
            if (toBillToggle) status += "${SalesOrderStatus.instance.TO_BILL},"
            if (toDeliverToggle) status += SalesOrderStatus.instance.TO_DELIVER
            refreshItems()
        }
    }

    fun refreshItems() {
        if (this is SalesOrderActive) {
            this.updateStatusToggle()
        }
        masterListDelegate.refreshItems()
    }

    override fun willRefreshItems() {

    }

    override fun fetchDidSuccess(data: MutableList<SalesOrderData>) {

    }


    override fun getAPIHandlingOnBaseResponse(query: String, page: Int): Call<BaseResponse<SalesOrderData>>? {
        return null
    }

    override fun getAPIHandlingOnBaseMessage(query: String, page: Int): Call<BaseMessage<SalesOrderData>>? {
        return null
    }

    override fun onListTapped(item: SalesOrderData, position: Int) {
        val viewSOItem = Intent(context, ViewSalesOrder::class.java)
        viewSOItem.putExtra("sales_order_no",item.name)
        startActivity(viewSOItem)
    }


}