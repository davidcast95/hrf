package bobzone.hrf.humanresource.Fragments.Invoice

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import bobzone.hrf.humanresource.Core.Base.Activity.BaseActivity
import bobzone.hrf.humanresource.Core.Base.Callback.BaseResponseCallBack
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleCallBack
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.SalesOrder.ViewSalesOrder.SalesTaxesAndChargesAdapter
import bobzone.hrf.humanresource.Fragments.SalesOrder.ViewSalesOrder.ViewSalesOrder
import bobzone.hrf.humanresource.Model.InvoiceItem.InvoiceItemData
import bobzone.hrf.humanresource.Model.SalesOrder.*
import bobzone.hrf.humanresource.Model.SalesTaxesAndCharge.SalesTaxesAndChargeData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_view_sales_order.*

/**
 * Created by davidwibisono on 3/7/18.
 */
open class ViewInvoice : BaseActivity() {

    val invoiceItems = mutableListOf<InvoiceItemData>()
    var grandTotal = 0.0
    var invoice = InvoiceSingleData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.view_invoice)
        setContentView(R.layout.activity_view_sales_invoice)

        val sales_invoice_no = intent.getStringExtra("sales_invoice_no")
        if (sales_invoice_no != null) {
            fetchSalesInvoice(sales_invoice_no)
        } else {
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return false
    }

    fun updateUI() {
        Helper.instance.setTextView(this,R.id.sales_no,invoice.name)
        Helper.instance.setTextView(this,R.id.customer,invoice.customer)


        if (invoice.selling_price_list != null) {
            Helper.instance.setTextView(this,R.id.price_list,invoice.selling_price_list)
        } else {
            val priceList = findViewById<TextView>(R.id.price_list)
            priceList.visibility = View.GONE
        }

        var formatView = "EEE, d MMM yyyy"
        var formatField = "yyyy-MM-dd"
        val formatDate = Helper.instance.formatDateFromstring(formatField,formatView,invoice.due_date)
        val transactionOnDate = "${getString(R.string.due_date_on)} $formatDate"
        Helper.instance.setTextView(this,R.id.transaction_date,transactionOnDate)
        Helper.instance.setTextView(this,R.id.note,"\"${invoice.note}\"")



        Helper.instance.setTextView(this,R.id.status,invoice.status)
        val status = findViewById<TextView>(R.id.status)
        if (invoice.status.equals(InvoiceStatus.instance.UNPAID))
            status.background = getDrawable(R.drawable.corner_button_warning)
        else if (invoice.status.equals(InvoiceStatus.instance.OVERDUE))
            status.background = getDrawable(R.drawable.corner_button_danger)
        else if (invoice.status.equals(InvoiceStatus.instance.PAID))
            status.background = getDrawable(R.drawable.corner_button_success)


        val salesItemAdapter = InvoiceItemAdapter(applicationContext, invoice.items)
        val lv = findViewById<ListView>(R.id.items)
        lv.adapter = salesItemAdapter
        lv.onItemClickListener = object:AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val item = salesItemAdapter.getItem(p2)
                if (item.sales_order.equals("")) {
                    Toast.makeText(applicationContext,getString(R.string.no_link_to_sales_order), Toast.LENGTH_SHORT).show()
                } else {
                    val viewSOItem = Intent(applicationContext, ViewSalesOrder::class.java)
                    viewSOItem.putExtra("sales_order_no", salesItemAdapter.getItem(p2).sales_order)
                    startActivity(viewSOItem)
                }
            }

        }

        loading.visibility = View.VISIBLE
        
        getTaxAndcharge(invoice.taxes)
        
    }

    fun getTaxAndcharge(data:MutableList<SalesTaxesAndChargeData>) {
        val taxIncluded = findViewById<LinearLayout>(R.id.tax_included)
        if (data.size > 0) {
            val subtotal = Helper.instance.convertDecimal(grandTotal)
            Helper.instance.setTextView(this, R.id.subtotal,"${invoice.currency} $subtotal")
            Helper.instance.setHeight(subtotalholder, 80)

            if (invoice.additional_discount_percentage > 0 && invoice.apply_discount_on.equals("Grand Total")) {
                Helper.instance.setTextView(this, R.id.discount_percentage, "Disc (${invoice.additional_discount_percentage} %)")
                var discount_amount = Helper.instance.convertDecimal(invoice.discount_amount)
                Helper.instance.setTextView(this, R.id.discount_amount, "${invoice.currency} $discount_amount")
                Helper.instance.setHeight(discountholder, 80)

                grandTotal -= invoice.discount_amount
                val after_discount = Helper.instance.convertDecimal(invoice.rounded_total)
                Helper.instance.setTextView(this, R.id.after_discount, "${invoice.currency} $after_discount")
                Helper.instance.setHeight(afterdiscountholder, 80)
            }
            var included = false
            data.forEach { stc ->
                stc.currency = invoice.currency
                grandTotal += stc.tax_amount
                if (stc.included_in_print_rate == 1) {
                    included = true
                }
            }
            val adapter = SalesTaxesAndChargesAdapter(applicationContext, data)
            taxcharges.adapter = adapter

            if (included) {
                val subtotalHolder = findViewById<LinearLayout>(R.id.subtotalholder)
                subtotalHolder.visibility = View.GONE
                taxIncluded.setOnClickListener(object:View.OnClickListener {
                    override fun onClick(p0: View?) {
                        val taxHeight = Helper.instance.setAndGetListViewHeightBasedOnChildren(taxcharges) + 80
                        Helper.instance.setHeight(taxchargesholder, taxHeight)
                        taxIncluded.visibility = View.GONE
                    }
                })
            } else {
                taxIncluded.visibility = View.GONE
                val taxHeight = Helper.instance.setAndGetListViewHeightBasedOnChildren(taxcharges) + 80
                Helper.instance.setHeight(taxchargesholder, taxHeight)
            }
        } else {
            taxIncluded.visibility = View.GONE
        }

        val grand_total = findViewById<TextView>(R.id.grand_total)
        val total = Helper.instance.convertDecimal(invoice.rounded_total)
        grand_total.setText("${invoice.currency} $total")
        loading.visibility = View.GONE
    }

    fun fetchSalesInvoice(name:String) {
        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext, cookieJar)
        val callSO = api.getSpecifiedSalesInvoice(name)
        callSO.enqueue(object: BaseSingleCallBack<InvoiceSingleData>(applicationContext) {
            override fun responseData(data: InvoiceSingleData) {
                invoice = data
                updateUI()
            }

            override fun failed(error: Throwable) {

            }
        })
    }

}