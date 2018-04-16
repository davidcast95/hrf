package bobzone.hrf.humanresource.Fragments.SalesOrder.Base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderData
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderStatus
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/6/18.
 */
open class SalesOrderAdapter(context: Context, objects: List<SalesOrderData>) :
        ArrayAdapter<SalesOrderData>(context, R.layout.list_sales_order, objects) {
    var list: List<SalesOrderData>

    init {
        this.list = objects
    }

    override fun getCount(): Int {
        return this.list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_sales_order, parent, false)
        val item = list[position]
        Helper.instance.setTextView(view,R.id.sales_no,item.name)
        Helper.instance.setTextView(view,R.id.customer,item.customer)
        val amount = Helper.instance.convertDecimal(item.grand_total)
        Helper.instance.setTextView(view,R.id.amount,"${item.currency} $amount")

        var formatView = "EEE, d MMM yyyy"
        var formatField = "yyyy-MM-dd"
        val formatDate = Helper.instance.formatDateFromstring(formatField,formatView,item.transaction_date)
        val transactionOnDate = "${context.getString(R.string.transaction_on)} $formatDate"
        Helper.instance.setTextView(view,R.id.transaction_date,transactionOnDate)

        if (item.selling_price_list != null) {
            Helper.instance.setTextView(view,R.id.price_list,item.selling_price_list)
        } else {
            val priceList = view.findViewById<TextView>(R.id.price_list)
            priceList.visibility = View.GONE
        }

        Helper.instance.setTextView(view,R.id.status,item.status)
        val status = view.findViewById<TextView>(R.id.status)
        if (item.status.equals(SalesOrderStatus.instance.TO_DELIVER_AND_BILL))
            status.background = context.getDrawable(R.drawable.corner_button_danger)
        else if (item.status.equals(SalesOrderStatus.instance.TO_DELIVER))
            status.background = context.getDrawable(R.drawable.corner_button_success)
        else if (item.status.equals(SalesOrderStatus.instance.TO_BILL))
            status.background = context.getDrawable(R.drawable.corner_button_warning)
        else if (item.status.equals(SalesOrderStatus.instance.COMPLETED))
            status.background = context.getDrawable(R.drawable.corner_button_completed)
        return view
    }

}