package bobzone.hrf.humanresource.Fragments.Offer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.RelativeLayout
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.OfferItem.OfferItemData
import bobzone.hrf.humanresource.Model.SalesOrderItem.SalesOrderItemData
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/9/18.
 */
open class OfferItemAdapter(context: Context, item:OfferItemData) :
        ArrayAdapter<OfferItemData>(context,R.layout.list_offer_item) {
    var offerItemData = OfferItemData()

    init {
        this.offerItemData = item
    }

    override fun getCount(): Int {
        return offerItemData.opportunity.size + offerItemData.quotation.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_offer_item, parent, false)

        if (position < offerItemData.opportunity.size) {
            //opportunity
            val cell = view.findViewById<RelativeLayout>(R.id.cell)
            Helper.instance.setHeight(cell, 440)

            val opportunity = offerItemData.opportunity[position]
            Helper.instance.setTextView(view, R.id.item_code, opportunity.name)
            Helper.instance.setTextView(view, R.id.item_name, opportunity.customer_name)
            Helper.instance.setTextView(view, R.id.type, opportunity.opportunity_type)

            if (!opportunity.transaction_date.equals("")) {
                var formatView = "EEE, d MMM yyyy"
                var formatField = "yyyy-MM-dd"
                val formatDate = Helper.instance.formatDateFromstring(formatField, formatView, opportunity.transaction_date)
                val transactionOnDate = "${context.getString(R.string.transaction_on)} $formatDate"
                Helper.instance.setTextView(view, R.id.transaction_date, transactionOnDate)
            }
            Helper.instance.setTextView(view,R.id.status,"Opportunity - ${opportunity.status}")

            if (opportunity.with_items == 0) {
                Helper.instance.setTextView(view,R.id.info,context.getString(R.string.no_items_included))
            }
        } else {
            val holder = view.findViewById<LinearLayout>(R.id.holder)
            holder.background = context.getDrawable(R.drawable.corner_button_completed)

            val quotation = offerItemData.quotation[position - offerItemData.opportunity.size]
            Helper.instance.setTextView(view, R.id.item_code, quotation.name)
            Helper.instance.setTextView(view, R.id.item_name, quotation.customer_name)
            Helper.instance.setTextView(view, R.id.type, quotation.order_type)

            if (!quotation.transaction_date.equals("")) {
                var formatView = "EEE, d MMM yyyy"
                var formatField = "yyyy-MM-dd"
                val formatDate = Helper.instance.formatDateFromstring(formatField, formatView, quotation.transaction_date)
                val transactionOnDate = "${context.getString(R.string.transaction_on)} $formatDate"
                Helper.instance.setTextView(view, R.id.transaction_date, transactionOnDate)
            }
            Helper.instance.setTextView(view,R.id.status,"Quotation - ${quotation.status}")
            var price = Helper.instance.convertDecimal(quotation.total_taxes_and_charges)
            Helper.instance.setTextView(view,R.id.amount,"IDR $price")
            Helper.instance.setTextView(view,R.id.info,"")


        }

        return view
    }

    fun setOfferItem(item:OfferItemData) {
        offerItemData = item
        notifyDataSetChanged()
    }


}