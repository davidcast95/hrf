package bobzone.hrf.humanresource.Fragments.SalesOrder.ViewSalesOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.SalesOrderItem.SalesOrderItemData
import bobzone.hrf.humanresource.Model.SalesTaxesAndCharge.SalesTaxesAndChargeData
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/12/18.
 */
open class SalesTaxesAndChargesAdapter(context: Context, objects: MutableList<SalesTaxesAndChargeData>) :
        ArrayAdapter<SalesTaxesAndChargeData>(context, R.layout.list_sales_item, objects) {
    var list = mutableListOf<SalesTaxesAndChargeData>()

    init {
        this.list = objects
    }

    override fun getCount(): Int {
        return this.list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_sales_taxes_and_charge, parent, false)
        val item = list[position]
        Helper.instance.setTextView(view,R.id.description,item.description)
        val tax = Helper.instance.convertDecimal(item.tax_amount)
        Helper.instance.setTextView(view,R.id.tax_amount,"${item.currency} $tax")
        return view
    }


}