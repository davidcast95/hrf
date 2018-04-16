package bobzone.hrf.humanresource.Fragments.SalesOrder.ViewSalesOrder

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.*
import bobzone.hrf.humanresource.Core.Base.Activity.BaseActivity
import bobzone.hrf.humanresource.Core.Base.Callback.BaseResponseCallBack
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleCallBack
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Fragments.Invoice.ViewInvoice
import bobzone.hrf.humanresource.Model.InvoiceItem.InvoiceItemData
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderData
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderSingleData
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderStatus
import bobzone.hrf.humanresource.Model.SalesOrderItem.SalesOrderItemData
import bobzone.hrf.humanresource.Model.SalesTaxesAndCharge.SalesTaxesAndChargeData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_view_sales_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by davidwibisono on 3/7/18.
 */
open class ViewSalesOrder : BaseActivity() {

    val salesItems = mutableListOf<SalesOrderItemData>()
    var grandTotal = 0.0
    var salesOrder = SalesOrderSingleData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.view_sales_order)
        setContentView(R.layout.activity_view_sales_order)

        val sales_order_no = intent.getStringExtra("sales_order_no")
        if (sales_order_no != null) {
            fetchSalesOrder(sales_order_no)
        } else {
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return false
    }

    fun updateUI() {
        Helper.instance.setTextView(this, R.id.sales_no, salesOrder.name)
        Helper.instance.setTextView(this, R.id.customer, salesOrder.customer)
        Helper.instance.setTextView(this, R.id.status, salesOrder.status)
        val status = findViewById<TextView>(R.id.status)
        if (salesOrder.status.equals(SalesOrderStatus.instance.TO_DELIVER_AND_BILL))
            status.background = getDrawable(R.drawable.corner_button_danger)
        else if (salesOrder.status.equals(SalesOrderStatus.instance.TO_DELIVER))
            status.background = getDrawable(R.drawable.corner_button_success)
        else if (salesOrder.status.equals(SalesOrderStatus.instance.TO_BILL))
            status.background = getDrawable(R.drawable.corner_button_warning)
        else if (salesOrder.status.equals(SalesOrderStatus.instance.COMPLETED))
            status.background = getDrawable(R.drawable.corner_button_completed)


        var formatView = "EEE, d MMM yyyy"
        var formatField = "yyyy-MM-dd"
        val formatDate = Helper.instance.formatDateFromstring(formatField, formatView, salesOrder.transaction_date)
        val transactionOnDate = "${getString(R.string.transaction_on)} $formatDate"
        Helper.instance.setTextView(this, R.id.transaction_date, transactionOnDate)

        if (salesOrder.selling_price_list != null) {
            Helper.instance.setTextView(this,R.id.price_list,salesOrder.selling_price_list)
        } else {
            val priceList = findViewById<TextView>(R.id.price_list)
            priceList.visibility = View.GONE
        }


        val salesItemAdapter = SalesOrderItemAdapter(applicationContext, salesOrder.items)
        val lv = findViewById<ListView>(R.id.items)
        lv.adapter = salesItemAdapter
        lv.onItemClickListener = object:AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val item = salesItemAdapter.getItem(p2)
                if (item.invoice_no.equals("")) {
                    Toast.makeText(applicationContext,getString(R.string.no_link_to_any_invoice),Toast.LENGTH_SHORT).show()
                } else {
                    val viewINVItem = Intent(applicationContext, ViewInvoice::class.java)
                    viewINVItem.putExtra("sales_invoice_no",item.invoice_no)
                    startActivity(viewINVItem)
                }
            }
        }

        loading.visibility = View.VISIBLE
        updateTaxAndcharge(salesOrder.taxes)
    }

    fun updateTaxAndcharge(data:MutableList<SalesTaxesAndChargeData>) {
        val taxIncluded = findViewById<LinearLayout>(R.id.tax_included)
        if (data.size > 0) {
            val subtotal = Helper.instance.convertDecimal(grandTotal)
            Helper.instance.setTextView(this, R.id.subtotal,"${salesOrder.currency} $subtotal")
            Helper.instance.setHeight(subtotalholder, 80)

            if (salesOrder.additional_discount_percentage > 0 && salesOrder.apply_discount_on.equals("Grand Total")) {
                Helper.instance.setTextView(this, R.id.discount_percentage, "Disc (${salesOrder.additional_discount_percentage} %)")
                var discount_amount = Helper.instance.convertDecimal(salesOrder.discount_amount)
                Helper.instance.setTextView(this, R.id.discount_amount, "${salesOrder.currency} $discount_amount")
                Helper.instance.setHeight(discountholder, 80)

                grandTotal -= salesOrder.discount_amount
                val after_discount = Helper.instance.convertDecimal(salesOrder.rounded_total)
                Helper.instance.setTextView(this, R.id.after_discount, "${salesOrder.currency} $after_discount")
                Helper.instance.setHeight(afterdiscountholder, 80)
            }
            var included = false
            data.forEach { stc ->
                stc.currency = salesOrder.currency
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
        val total = Helper.instance.convertDecimal(salesOrder.rounded_total)
        grand_total.setText("${salesOrder.currency} $total")
        loading.visibility = View.GONE
    }

    fun fetchSalesOrder(name:String) {
        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext, cookieJar)
        val callSO = api.getSpecifiedSalesOrder(name)
        callSO.enqueue(object: BaseSingleCallBack<SalesOrderSingleData>(applicationContext) {
            override fun responseData(data: SalesOrderSingleData) {
                salesOrder = data
                updateUI()
            }

            override fun failed(error: Throwable) {

            }
        })
    }



}