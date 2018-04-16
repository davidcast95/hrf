package bobzone.hrf.humanresource.Fragments.Employee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.Customer.CustomerData
import bobzone.hrf.humanresource.Model.Customer.EmployeeData
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 2/13/18.
 */

open class EmployeeAdapter(context: Context, objects: List<EmployeeData>) :
        ArrayAdapter<EmployeeData>(context, R.layout.list_employee, objects) {
    var list: List<EmployeeData>

    init {
        this.list = objects
    }

    override fun getCount(): Int {
        return this.list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_employee, parent, false)
        val employee = list.get(position)

        Helper.instance.setTextView(view,R.id.status,employee.status)
        Helper.instance.setTextView(view,R.id.fullname,employee.employee_name)
        Helper.instance.setTextView(view,R.id.name,employee.name)
        var formatView = "EEE, d MMM yyyy"
        var formatField = "yyyy-MM-dd"
        val formatDate = Helper.instance.formatDateFromstring(formatField, formatView, employee.date_of_joining)
        val joinDate = "${context.getString(R.string.joining_date_on)} $formatDate"
        Helper.instance.setTextView(view,R.id.join_date,joinDate)
        return view
    }

}