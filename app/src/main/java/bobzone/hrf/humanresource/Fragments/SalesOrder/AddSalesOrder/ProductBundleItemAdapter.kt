package bobzone.hrf.humanresource.Fragments.SalesOrder.AddSalesOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.ProductBundleItem.ProductBundleItemData
import bobzone.hrf.humanresource.Model.SalesOrderItem.ItemData
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/16/18.
 */
open class ProductBundleItemAdapter(context: Context, objects: List<ProductBundleItemData>) :
        ArrayAdapter<ProductBundleItemData>(context, R.layout.list_sales_item, objects) {
    var list: List<ProductBundleItemData>

    init {
        this.list = objects
    }

    override fun getCount(): Int {
        return this.list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_product_bundle_item, parent, false)
        val item = list[position]
        Helper.instance.setTextView(view, R.id.item_name,item.description)
        Helper.instance.setTextView(view, R.id.item_code,item.item_code)
        val rate = Helper.instance.convertDecimal(item.rate)
        Helper.instance.setTextView(view, R.id.uom,"${item.qty} ${item.uom}")
        Helper.instance.setTextView(view, R.id.rate,"")


        return view
    }

}