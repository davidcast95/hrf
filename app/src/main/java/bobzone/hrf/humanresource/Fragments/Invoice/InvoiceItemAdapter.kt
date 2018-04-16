package bobzone.hrf.humanresource.Fragments.Invoice

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.SalesOrder.ViewSalesOrder.ViewSalesOrder
import bobzone.hrf.humanresource.Model.InvoiceItem.InvoiceItemData
import bobzone.hrf.humanresource.Model.SalesOrderItem.SalesOrderItemData
import bobzone.hrf.humanresource.R
import com.google.gson.Gson

/**
 * Created by davidwibisono on 3/9/18.
 */
open class InvoiceItemAdapter(context: Context, objects: MutableList<InvoiceItemData>) :
        ArrayAdapter<InvoiceItemData>(context, R.layout.list_sales_item, objects) {
    var list = mutableListOf<InvoiceItemData>()

    init {
        this.list = objects
    }

    override fun getCount(): Int {
        return this.list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_sales_item, parent, false)
        val item = list[position]
        Helper.instance.setTextView(view, R.id.item_name,item.item_name)
        Helper.instance.setTextView(view, R.id.item_code,item.item_code)
        val qty = Helper.instance.convertInt(item.qty)
        val price_rate = Helper.instance.convertDecimal(item.price_list_rate)
        Helper.instance.setTextView(view,R.id.qty,"${qty} ${item.uom}")
        Helper.instance.setTextView(view,R.id.rate,"@ ${item.currency} ${price_rate}")

        if (item.discount_percentage > 0) {
            Helper.instance.setTextView(view,R.id.discount,"Disc ${item.discount_percentage} %")

            val after_discount = Helper.instance.convertDecimal(item.rate)
            Helper.instance.setTextView(view,R.id.afterdiscount, "@ ${item.currency} $after_discount")

            val amount = Helper.instance.convertDecimal(item.amount)
            Helper.instance.setTextView(view,R.id.amount,"${item.currency} ${amount}")
        } else {
            val amount = Helper.instance.convertDecimal(item.amount)
            Helper.instance.setTextView(view,R.id.amount,"${item.currency} ${amount}")
        }

        if (!item.sales_order.equals("")) {
            Helper.instance.setTextView(view,R.id.link_so, "link to ${item.sales_order}")
        }

        return view
    }

    fun editItem(item: InvoiceItemData, position: Int) {
        if (position < list.size)
            this.list[position] = item
    }

}