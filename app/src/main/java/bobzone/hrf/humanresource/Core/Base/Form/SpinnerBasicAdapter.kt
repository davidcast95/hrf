package bobzone.hrf.humanresource.Core.Base.Form

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 2/14/18.
 */
open class SpinnerBasicAdapter(context: Context, list: List<String>) :
        ArrayAdapter<String>(context, R.layout.spinner_basic, list) {
    var list:List<String> = ArrayList<String>()
    init {
        this.list = list
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val current = list[position]
        val view = inflater.inflate(R.layout.spinner_basic, parent, false)
        val text = view.findViewById<TextView>(R.id.text)
        text.text = current
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val current = list[position]
        val view = inflater.inflate(R.layout.spinner_basic, parent, false)
        val text = view.findViewById<TextView>(R.id.text)
        text.text = current
        return view
    }
}