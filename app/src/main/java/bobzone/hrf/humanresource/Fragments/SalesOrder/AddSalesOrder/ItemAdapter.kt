package bobzone.hrf.humanresource.Fragments.SalesOrder.AddSalesOrder

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Base.List.MasterListDelegate
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.SalesOrderItem.ItemData
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/7/18.
 */
open class ItemAdapter(context: Context, objects: List<ItemData>) :
        ArrayAdapter<ItemData>(context, R.layout.list_sales_item, objects) {
    var list: List<ItemData>
    var currency = "IDR"
    var masterListDelegate:MasterListDelegate<ItemData>? = null

    init {
        this.list = objects
    }

    override fun getCount(): Int {
        return this.list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_item, parent, false)
        val item = list[position]
        Helper.instance.setTextView(view,R.id.item_name,item.item_name)
        Helper.instance.setTextView(view,R.id.item_code,item.item_code)

        Helper.instance.setTextView(view,R.id.uom,"${item.uom}")
        Helper.instance.setTextView(view,R.id.rate,"")

        if (item.product_bundle_item.size > 0) {
            val priceDecimal = Helper.instance.convertDecimal(item.standard_rate)

            val price = view.findViewById<TextView>(R.id.rate)
            price.visibility = View.VISIBLE
            Helper.instance.setTextView(view,R.id.rate, "$currency $priceDecimal")
            val bundleItemAdapter = ProductBundleItemAdapter(context, item.product_bundle_item)
            val productBundleItemListView = view.findViewById<ListView>(R.id.product_bundle_item)

            productBundleItemListView.onItemClickListener = object : AdapterView.OnItemClickListener {
                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val mlDelegate = masterListDelegate
                    if (mlDelegate != null) {
                        val l = mlDelegate.listenerMaster
                        if (l != null)
                            l.onListTapped(item,position)
                    }
                }

            }
            productBundleItemListView.adapter = bundleItemAdapter

            val productBundleItemHeight = Helper.instance.setAndGetListViewHeightBasedOnChildren(productBundleItemListView)
            Helper.instance.setHeight(view, productBundleItemHeight - 100)
        }



        return view
    }

}