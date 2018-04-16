package bobzone.hrf.humanresource.Fragments.Customer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.Customer.CustomerData
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 2/13/18.
 */

open class CustomerAdapter(context: Context, objects: List<CustomerData>) :
        ArrayAdapter<CustomerData>(context, R.layout.list_customer, objects) {
    var list: List<CustomerData>

    init {
        this.list = objects
    }

    override fun getCount(): Int {
        return this.list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_customer, parent, false)
        val customer = list.get(position)

        Helper.instance.setTextView(view,R.id.customer_group,customer.customer_group)
        Helper.instance.setTextView(view,R.id.fullname,customer.name)
        Helper.instance.setTextView(view,R.id.territory,customer.territory)
        if (customer.default_price_list != null) {
            Helper.instance.setTextView(view, R.id.price_list, customer.default_price_list)
        } else {
            Helper.instance.setTextView(view,R.id.price_list,"")
        }
        return view
    }

}