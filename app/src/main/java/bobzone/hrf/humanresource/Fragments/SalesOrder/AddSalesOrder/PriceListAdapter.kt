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
import bobzone.hrf.humanresource.Model.PriceList.PriceListData
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/7/18.
 */
open class PriceListAdapter(context: Context, objects: List<PriceListData>) :
        ArrayAdapter<PriceListData>(context, R.layout.list_sales_item, objects) {
    var list: List<PriceListData>

    init {
        this.list = objects
    }

    override fun getCount(): Int {
        return this.list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_price_list, parent, false)
        val item = list[position]
        Helper.instance.setTextView(view,R.id.item_name,item.name)

        return view
    }

}