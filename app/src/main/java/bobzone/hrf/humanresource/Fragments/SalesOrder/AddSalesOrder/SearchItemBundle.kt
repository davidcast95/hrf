package bobzone.hrf.humanresource.Fragments.SalesOrder.AddSalesOrder

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import bobzone.hrf.humanresource.Core.Base.Activity.BaseActivity
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleCallBack
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleMessageCallBack
import bobzone.hrf.humanresource.Core.Base.List.MasterListDelegate
import bobzone.hrf.humanresource.Core.Base.List.MasterListEventListener
import bobzone.hrf.humanresource.Core.Base.OptionsMenu.SearchOptionsMenuDelegate
import bobzone.hrf.humanresource.Core.Global.Constant
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.BaseSingleMessage
import bobzone.hrf.humanresource.Model.Item.ItemArgs
import bobzone.hrf.humanresource.Model.SalesOrderItem.ItemData
import bobzone.hrf.humanresource.Model.SalesOrderItem.ItemDetailsData
import bobzone.hrf.humanresource.Model.SalesOrderItem.SalesOrderItemData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by davidwibisono on 3/7/18.
 */
open class SearchItemBundle : Fragment(), MasterListEventListener<ItemData> {

    var searchOptinsMenuDelegate: SearchOptionsMenuDelegate = SearchOptionsMenuDelegate()
    var masterListDelegate = MasterListDelegate<ItemData>()
    var items = mutableListOf<ItemData>()
    var choseItem = ItemData()
    var currency = "IDR"
    var company = ""
    var item:Item? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val infl = inflater
        if (infl is LayoutInflater) {
            val v = inflater.inflate(R.layout.activity_search_item, container, false)
            val listAdapter = ItemAdapter(activity, items)
            listAdapter.masterListDelegate = masterListDelegate
            masterListDelegate.setup(v,listAdapter, activity)
            masterListDelegate.listenerMaster = this
            searchOptinsMenuDelegate.listener = masterListDelegate


            masterListDelegate.onFetchData()
            return v
        }
        return view
    }


    override fun willRefreshItems() {

    }
    override fun fetchDidSuccess(data: MutableList<ItemData>) {

    }

    override fun getAPIHandlingOnBaseResponse(query: String, page: Int): Call<BaseResponse<ItemData>>? {
        return null
    }

    override fun getAPIHandlingOnBaseMessage(query: String, page: Int): Call<BaseMessage<ItemData>>? {
        val cookieJar = UserModel.instance.loadCookieJar(activity)
        val api = Helper.instance.getAPIWithCookie(activity,cookieJar)
        val start = page * Constant.instance.LIMIT_MASTER_LIST
        return api.getItem("1", "0",query,"$start")
    }


    override fun onListTapped(item: ItemData, position: Int) {
//        val intent = Intent()
//        val json = Gson().toJson(item)
//        intent.putExtra("json", json)
//        setResult(Activity.RESULT_OK,intent)
//        finish()
        choseItem = item
        confirmItem(item)
    }

    fun confirmItem(item:ItemData) {

        AlertDialog.Builder(activity)
        val li = LayoutInflater.from(activity)
        val promptsView = li.inflate(R.layout.dialog_confirm_item, null)

        val loading = promptsView.findViewById<RelativeLayout>(R.id.loading_view)
        if (loading is RelativeLayout)
            loading.visibility = View.VISIBLE


        val cookieJar = UserModel.instance.loadCookieJar(activity)
        val api = Helper.instance.getAPIWithCookie(activity, cookieJar)
        val args = ItemArgs()
        val parentItem = this.item
        if (parentItem is Item)
            args.price_list = parentItem.customerPriceList
        args.company = company
        args.item_code = item.item_code
        args.price_list_currency = currency
        val json = Gson().toJson(args)
        val callItemDetails = api.getItemDetails(json)
        var itemDetails = ItemDetailsData()

        callItemDetails.enqueue(object : BaseSingleMessageCallBack<ItemDetailsData>(context) {
            override fun responseData(data: ItemDetailsData) {
                itemDetails = data
                Helper.instance.setTextView(promptsView,R.id.item_code,item.item_code)
                Helper.instance.setTextView(promptsView,R.id.item_name,item.item_name)
                Helper.instance.setTextView(promptsView, R.id.uom, itemDetails.uom)
                val price = Helper.instance.convertDecimal(itemDetails.rate)
                Helper.instance.setTextView(promptsView, R.id.price, "${price}")
                Helper.instance.setHint(promptsView,R.id.price,"$price")
                Helper.instance.setEditTextToMoneyInput(promptsView,R.id.price)
                Helper.instance.setTextView(promptsView,R.id.currency,currency)

                if (itemDetails.rate > 0.0) {
                    Helper.instance.setReadOnly(promptsView,R.id.price)
                }
                loading.visibility = View.GONE
                return
            }

            override fun failed(error: Throwable) {

            }
        })
        val alertDialogBuilder = AlertDialog.Builder(
                activity)
        alertDialogBuilder.setView(promptsView)
        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("Add"
                ) { dialog, id ->
                    if (loading.visibility == View.GONE) {
                        val qty = promptsView.findViewById<EditText>(R.id.qty).text.toString()
                        val price = promptsView.findViewById<EditText>(R.id.price).text.toString()
                        if (qty.equals("") || price.equals("")) {
                            Toast.makeText(activity,getString(R.string.field_required),Toast.LENGTH_SHORT).show()
                            confirmItem(item)
                        } else if (qty.equals("0")) {
                            Toast.makeText(activity,"kuantitas harus diisi",Toast.LENGTH_SHORT).show()
                            confirmItem(item)
                        } else {
                            val i = Intent()
                            val soi = SalesOrderItemData()
                            soi.item_name = item.item_name
                            soi.item_code = item.item_code
                            soi.uom = itemDetails.uom
                            soi.rate = Helper.instance.fromStringToDecimal(price)
                            soi.price_list_rate = Helper.instance.fromStringToDecimal(price)
                            soi.qty = Helper.instance.fromStringToInt(qty)
                            soi.amount = soi.rate * soi.qty
                            soi.currency = currency
                            val json = Gson().toJson(soi)
                            i.putExtra("json",json)
                            activity.setResult(Activity.RESULT_OK,i)
                            activity.finish()
                        }
                    }
                }

        // create alert dialog
        val alertDialog = alertDialogBuilder.create()

        alertDialog.show()

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            masterListDelegate.refreshItems()
        }
    }


}