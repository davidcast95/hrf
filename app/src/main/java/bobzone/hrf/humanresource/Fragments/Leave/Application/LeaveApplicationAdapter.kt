package bobzone.hrf.humanresource.Fragments.Leave.Application

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.Leave.LeaveApplicationData
import bobzone.hrf.humanresource.Model.Leave.LeaveApplicationStatus
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/6/18.
 */
open class LeaveApplicationAdapter(context: Context, objects: List<LeaveApplicationData>) :
        ArrayAdapter<LeaveApplicationData>(context, R.layout.list_sales_order, objects) {
    var list: List<LeaveApplicationData>

    init {
        this.list = objects
    }

    override fun getCount(): Int {
        return this.list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_leave_application, parent, false)
        val item = list[position]

        Helper.instance.setTextView(view,R.id.leave_no,item.leave_no)
        Helper.instance.setTextView(view,R.id.employee_name, item.employee_name)

        var formatView = "EEE, d MMM yyyy"
        var formatField = "yyyy-MM-dd"
        val fromDate = Helper.instance.formatDateFromstring(formatField, formatView, item.from_date)

        val toDate = Helper.instance.formatDateFromstring(formatField, formatView, item.to_date)
        Helper.instance.setTextView(view,R.id.interval_date, "${item.leave_type}\n$fromDate - $toDate")

        if (item.description != null)
            Helper.instance.setTextView(view,R.id.description, item.description)

        var days = context.getString(R.string.day)
        if (days.equals("day")) {
            if (item.total_leave_days > 1) {
                days = "days"
            }
        }
        Helper.instance.setTextView(view,R.id.total_leaves,"${item.total_leave_days} $days")

        Helper.instance.setTextView(view,R.id.status,item.status)
        val status = view.findViewById<TextView>(R.id.status)
        if (item.status.equals(LeaveApplicationStatus.instance.OPEN))
            status.background = context.getDrawable(R.drawable.corner_button_off)
        else if (item.status.equals(LeaveApplicationStatus.instance.APPROVED))
            status.background = context.getDrawable(R.drawable.corner_button_success)
        else if (item.status.equals(LeaveApplicationStatus.instance.REJECTED))
            status.background = context.getDrawable(R.drawable.corner_button_danger)
        return view
    }

}