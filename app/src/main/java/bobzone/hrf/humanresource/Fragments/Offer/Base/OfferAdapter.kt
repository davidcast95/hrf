package bobzone.hrf.humanresource.Fragments.Offer.Base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.Lead.LeadData
import bobzone.hrf.humanresource.Model.Lead.LeadStatus
import bobzone.hrf.humanresource.Model.Lead.MarketSegment
import bobzone.hrf.humanresource.Model.Offer.OfferData
import bobzone.hrf.humanresource.Model.OfferItem.OpportunityData
import bobzone.hrf.humanresource.Model.Quotation.QuotationData
import bobzone.hrf.humanresource.Model.Quotation.QuotationStatus
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/14/18.
 */
open class OfferAdapter(context: Context) :
        ArrayAdapter<OfferData>(context, R.layout.list_sales_order) {
    var leads = mutableListOf<LeadData>()
    var quotations = mutableListOf<QuotationData>()
    var opportunities = mutableListOf<OpportunityData>()


    override fun getCount(): Int {
        return leads.size + quotations.size + opportunities.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_offer, parent, false)
        if (position < leads.size) {
            val item = leads[position]
            Helper.instance.setTextView(view, R.id.lead_no,item.name)

            val dollar1 = view.findViewById<ImageView>(R.id.dollar1)
            val dollar2 = view.findViewById<ImageView>(R.id.dollar2)
            val dollar3 = view.findViewById<ImageView>(R.id.dollar3)
            dollar1.visibility = View.GONE
            dollar2.visibility = View.GONE
            dollar3.visibility = View.GONE
            if (item.market_segment.equals(MarketSegment.instance.LOWER_INCOME)) {
                dollar1.visibility = View.VISIBLE
            } else if (item.market_segment.equals(MarketSegment.instance.MIDDLE_INCOME)) {
                dollar1.visibility = View.VISIBLE
                dollar2.visibility = View.VISIBLE
            } else if (item.market_segment.equals(MarketSegment.instance.UPPER_INCOME)) {
                dollar1.visibility = View.VISIBLE
                dollar2.visibility = View.VISIBLE
                dollar3.visibility = View.VISIBLE

            }

            Helper.instance.setTextView(view,R.id.lead_name,item.lead_name)
            Helper.instance.setTextView(view,R.id.company_name,item.company_name)
            if (item.email != null)
                Helper.instance.setTextView(view,R.id.email,item.email)
            else
                Helper.instance.setTextView(view,R.id.email,"")

            var formatView = "EEE, d MMM yyyy"
            var formatField = "yyyy-MM-dd"
            val formatDate = Helper.instance.formatDateFromstring(formatField,formatView,item.contact_date)
            if (formatDate.equals(""))
                Helper.instance.setTextView(view,R.id.contact_date,"")
            else {
                val contactDate = "${context.getString(R.string.last_contact_on)} $formatDate"
                Helper.instance.setTextView(view, R.id.contact_date, contactDate)
            }

            Helper.instance.setTextView(view,R.id.status,item.status)
            val status = view.findViewById<TextView>(R.id.status)
            if (item.status.equals(LeadStatus.instance.LEAD)) {
                status.background = context.getDrawable(R.drawable.corner_button_completed)
            } else if (item.status.equals(LeadStatus.instance.OPEN)) {
                status.background = context.getDrawable(R.drawable.corner_button_warning)
            } else if (item.status.equals(LeadStatus.instance.REPLIED)) {
                status.background = context.getDrawable(R.drawable.corner_button_warning)
                status.setTextColor(context.resources.getColor(R.color.DangerColor))
            } else if (item.status.equals(LeadStatus.instance.OPPORTUNITY)) {
                status.background = context.getDrawable(R.drawable.corner_button_off)
            } else if (item.status.equals(LeadStatus.instance.INTERESTED)) {
                status.background = context.getDrawable(R.drawable.corner_button_success)
            } else if (item.status.equals(LeadStatus.instance.QUOTATION)) {
                status.background = context.getDrawable(R.drawable.corner_button_completed)
            } else if (item.status.equals(LeadStatus.instance.LOST_QUOTATION)) {
                status.background = context.getDrawable(R.drawable.corner_button_danger)
            } else if (item.status.equals(LeadStatus.instance.CONVERTED)) {
                status.background = context.getDrawable(R.drawable.corner_button_success)
            } else if (item.status.equals(LeadStatus.instance.DO_NOT_CONTACT)) {
                status.background = context.getDrawable(R.drawable.corner_button_off)
            }
        } else if (position - leads.size < quotations.size) {
            val item = quotations[position - leads.size]
            Helper.instance.setTextView(view, R.id.lead_no,item.name)

            val dollar1 = view.findViewById<ImageView>(R.id.dollar1)
            val dollar2 = view.findViewById<ImageView>(R.id.dollar2)
            val dollar3 = view.findViewById<ImageView>(R.id.dollar3)
            dollar1.visibility = View.GONE
            dollar2.visibility = View.GONE
            dollar3.visibility = View.GONE

            Helper.instance.setTextView(view,R.id.lead_name,item.customer_name)
            Helper.instance.setTextView(view,R.id.company_name,item.order_type)
            if (item.contact_email != null)
                Helper.instance.setTextView(view,R.id.email,item.contact_email)
            else
                Helper.instance.setTextView(view,R.id.email,"")

            var formatView = "EEE, d MMM yyyy"
            var formatField = "yyyy-MM-dd"
            val formatDate = Helper.instance.formatDateFromstring(formatField,formatView,item.transaction_date)
            if (formatDate.equals(""))
                Helper.instance.setTextView(view,R.id.contact_date,"")
            else {
                val contactDate = "${context.getString(R.string.transaction_on)} $formatDate"
                Helper.instance.setTextView(view, R.id.contact_date, contactDate)
            }

            Helper.instance.setTextView(view,R.id.status,"Quotation - ${item.status}")
            val status = view.findViewById<TextView>(R.id.status)
            if (item.status.equals(QuotationStatus.instance.ORDERED))
                status.background = context.getDrawable(R.drawable.corner_button_success)
            else
                status.background = context.getDrawable(R.drawable.corner_button_completed)
        } else if (position - leads.size - quotations.size < opportunities.size) {

        }

//


        return view
    }

    fun cleared() {
        leads.clear()
        quotations.clear()
        opportunities.clear()
    }

}