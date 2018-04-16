package bobzone.hrf.humanresource.Fragments.Offer

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ListView
import android.widget.TextView
import bobzone.hrf.humanresource.Core.Base.Activity.BaseActivity
import bobzone.hrf.humanresource.Core.Base.Callback.BaseMessageCallBack
import bobzone.hrf.humanresource.Core.Base.Callback.BaseResponseCallBack
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleMessageCallBack
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.Invoice.InvoiceItemAdapter
import bobzone.hrf.humanresource.Fragments.SalesOrder.ViewSalesOrder.SalesTaxesAndChargesAdapter
import bobzone.hrf.humanresource.Model.InvoiceItem.InvoiceItemData
import bobzone.hrf.humanresource.Model.Lead.LeadData
import bobzone.hrf.humanresource.Model.Lead.LeadStatus
import bobzone.hrf.humanresource.Model.OfferItem.OfferItemData
import bobzone.hrf.humanresource.Model.SalesOrder.InvoiceData
import bobzone.hrf.humanresource.Model.SalesOrder.InvoiceStatus
import bobzone.hrf.humanresource.Model.SalesTaxesAndCharge.SalesTaxesAndChargeData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_view_sales_order.*

/**
 * Created by davidwibisono on 3/14/18.
 */
open class ViewOffer : BaseActivity() {

    val offerItem = OfferItemData()
    var grandTotal = 0.0
    var lead = LeadData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.view_offer)
        setContentView(R.layout.activity_view_offer)

        val json = intent.getStringExtra("offer")
        lead = Gson().fromJson(json, LeadData::class.java)


        Helper.instance.setTextView(this, R.id.lead_no, lead.name)
        Helper.instance.setTextView(this, R.id.lead_name, lead.lead_name)
        Helper.instance.setTextView(this, R.id.company_name, lead.company_name)
        Helper.instance.setTextView(this, R.id.email, lead.email)


        if (lead.contact_date.equals(""))
            Helper.instance.setTextView(this, R.id.contact_date,"")
        else {
            var formatView = "EEE, d MMM yyyy"
            var formatField = "yyyy-MM-dd"
            val formatDate = Helper.instance.formatDateFromstring(formatField, formatView, lead.contact_date)
            val contactDate = "${getString(R.string.last_contact_on)} $formatDate"
            Helper.instance.setTextView(this, R.id.contact_date, contactDate)
        }
        Helper.instance.setTextView(this, R.id.status, lead.status)
        val status = findViewById<TextView>(R.id.status)
        if (lead.status.equals(LeadStatus.instance.LEAD))
            status.background = getDrawable(R.drawable.corner_button_completed)
        else if (lead.status.equals(LeadStatus.instance.OPEN))
            status.background = getDrawable(R.drawable.corner_button_warning)
        else if (lead.status.equals(LeadStatus.instance.REPLIED)) {
            status.background = getDrawable(R.drawable.corner_button_warning)
            status.setTextColor(resources.getColor(R.color.DangerColor))
        } else if (lead.status.equals(LeadStatus.instance.OPPORTUNITY))
            status.background = getDrawable(R.drawable.corner_button_off)
        else if (lead.status.equals(LeadStatus.instance.INTERESTED))
            status.background = getDrawable(R.drawable.corner_button_success)
        else if (lead.status.equals(LeadStatus.instance.QUOTATION))
            status.background = getDrawable(R.drawable.corner_button_completed)
        else if (lead.status.equals(LeadStatus.instance.LOST_QUOTATION))
            status.background = getDrawable(R.drawable.corner_button_danger)
        else if (lead.status.equals(LeadStatus.instance.CONVERTED))
            status.background = getDrawable(R.drawable.corner_button_success)
        else if (lead.status.equals(LeadStatus.instance.DO_NOT_CONTACT))
            status.background = getDrawable(R.drawable.corner_button_off)


        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext, cookieJar)
        val call = api.getLeadItem(lead.name)
        val offerItemAdapter = OfferItemAdapter(applicationContext, offerItem)
        val lv = findViewById<ListView>(R.id.items)
        lv.adapter = offerItemAdapter
        loading.visibility = View.VISIBLE
        call.enqueue(object :BaseSingleMessageCallBack<OfferItemData>(applicationContext) {
            override fun responseData(data: OfferItemData) {
                loading.visibility = View.GONE
                offerItemAdapter.setOfferItem(data)
            }

            override fun failed(error: Throwable) {
                loading.visibility = View.GONE
            }
        })



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return false
    }


}