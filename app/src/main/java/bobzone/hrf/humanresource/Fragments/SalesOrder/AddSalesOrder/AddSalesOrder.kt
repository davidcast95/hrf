package bobzone.hrf.humanresource.Fragments.SalesOrder.AddSalesOrder

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import bobzone.hrf.humanresource.Core.Base.Activity.FormActivity
import bobzone.hrf.humanresource.Core.Base.Form.*
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Core.Model.CreationResponse
import bobzone.hrf.humanresource.Fragments.SalesOrder.ViewSalesOrder.SalesOrderItemAdapter
import bobzone.hrf.humanresource.Model.Company.CompanyData
import bobzone.hrf.humanresource.Model.Currency.CurrencyData
import bobzone.hrf.humanresource.Model.MetaData.MetaData
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderStatus
import bobzone.hrf.humanresource.Model.SalesOrderItem.SalesOrderItemData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import java.util.*

/**
 * Created by davidwibisono on 3/7/18.
 */
open class AddSalesOrder : FormActivity() {

    val seriesForm = BasicSpinner("Series","naming_series", listOf("SO-"))
    val companyForm = APIBasicSpinner<CompanyData>("Company","company")
    val currencyForm = APIBasicSpinner<CurrencyData>("Currency","currency")
    val customerForm = IntentBasicInput("Customer","customer")
    val deliveryForm = BasicDatePicker("Delivery Date","delivery_date")
    val itemsTableForm = TableForm<SalesOrderItemData>("Items","items")

    var customer = ""
    var customerPriceList = ""
    var customerGroup = ""
    val salesItems = mutableListOf<SalesOrderItemData>()
    var salesItemAdapter: SalesOrderItemAdapter? = null
    var items = JsonArray()
    var grandTotal = 0.0

    var today = Date()
    var calendar = Calendar.getInstance()



    override fun getLayout(): Int {
        return R.layout.activity_add_sales_order
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.add_sales_order)
        formBuilder.formSheets.add(seriesForm)

        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext, cookieJar)
        val companyCall = api.getCompany()
        companyForm.call = companyCall
        companyForm.listener = object :APIBasicSpinnerEventListener<CompanyData> {
            override fun getItem(item: CompanyData): String {
                return item.name
            }

            override fun getField(item: CompanyData): String {
                return item.name
            }

            override fun fetchSuccess(objects: List<CompanyData>) {

            }
        }
        formBuilder.formSheets.add(companyForm)

        customerForm.intent = Intent(applicationContext, SearchCustomer::class.java)
        customerForm.activity = this
        customerForm.intentListener = object: FormIntentListener {
            override fun onIntentResult(data: Intent): String {
                customer = data.getStringExtra("customer")
                customerGroup = data.getStringExtra("customer_group")
                customerPriceList = data.getStringExtra("customer_default_price_list")
                return "$customer ($customerGroup)"
            }

            override fun getField(): String {
                return customer
            }

            override fun prepareIntent(i: Intent): Intent {
                return i
            }

        }
        customerForm.isRequired = true
        formBuilder.formSheets.add(customerForm)

        val currencyCall = api.getCurrency()
        currencyForm.call = currencyCall
        currencyForm.listener = object :APIBasicSpinnerEventListener<CurrencyData> {
            override fun getItem(item: CurrencyData): String {
                return item.name
            }

            override fun getField(item: CurrencyData): String {
                return item.name
            }

            override fun fetchSuccess(objects: List<CurrencyData>) {

            }

        }
        formBuilder.formSheets.add(currencyForm)

        deliveryForm.activity = this
        deliveryForm.isRequired = true
        formBuilder.formSheets.add(deliveryForm)
        salesItemAdapter = SalesOrderItemAdapter(applicationContext, salesItems)
        itemsTableForm.adapter = salesItemAdapter
        itemsTableForm.intent = Intent(applicationContext, Item::class.java)
        itemsTableForm.activity = this
        itemsTableForm.listener = object :TableFormEventListener<SalesOrderItemData> {
            override fun getItem(i: Intent): SalesOrderItemData {
                val json = i.getStringExtra("json")
                val item = Gson().fromJson<SalesOrderItemData>(json, SalesOrderItemData::class.java)
                return item
            }

            override fun prepareIntent(i: Intent): Intent {
                i.putExtra("company",companyForm.field)
                i.putExtra("currency",currencyForm.field)
                i.putExtra("customer",customer)
                i.putExtra("customer_default_price_list",customerPriceList)
                return i
            }

            override fun getField(jsonArray: JsonArray) {

                items = jsonArray
            }

            override fun onListTapped(item: SalesOrderItemData, position: Int) {
                editItem(item, position)
            }

            override fun itemDidUpdate() {
                updateGrandTotal()
            }

            override fun shouldAddItem(): Boolean {
                if (customer.equals("")) {
                    Toast.makeText(applicationContext,getString(R.string.please_fill_the_customer_first),Toast.LENGTH_SHORT).show()
                    return false
                } else {
                    return true
                }
            }
        }
        itemsTableForm.isRequired = true

        formBuilder.formSheets.add(itemsTableForm)

        formBuilder.build()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        formBuilder.onResult(resultCode,requestCode,data)
    }


    override fun requestDidResponseError(errorCode: Int) {
        Log.e("asd", "$errorCode")
    }

    override fun submitDidSuccess(creationResponse: CreationResponse) {
        MetaData.instance.sales_order.count.to_deliver_and_bill++
        super.submitDidSuccess(creationResponse)
    }

    override fun getCallSubmit(jsonObject: JsonObject): Call<CreationResponse> {
        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext,cookieJar)
        jsonObject.addProperty("customer_group",customerGroup)
        jsonObject.add("items",items)
        jsonObject.addProperty("docstatus",1)
        jsonObject.addProperty("status",SalesOrderStatus.instance.TO_DELIVER_AND_BILL)
        return api.submitSalesOrder(jsonObject)
    }


    fun editItem(item: SalesOrderItemData, position:Int) {

        AlertDialog.Builder(this)
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.dialog_confirm_item, null)

        val loading = promptsView.findViewById<RelativeLayout>(R.id.loading_view)
        loading.visibility = View.GONE

        Helper.instance.setTextView(promptsView,R.id.qty,"${item.qty}")
        Helper.instance.setTextView(promptsView,R.id.item_code,item.item_code)
        Helper.instance.setTextView(promptsView,R.id.item_name,item.item_name)
        Helper.instance.setTextView(promptsView, R.id.uom, item.uom)
        val price = Helper.instance.convertDecimal(item.rate)
        Helper.instance.setTextView(promptsView,R.id.currency,currencyForm.field)
        Helper.instance.setTextView(promptsView, R.id.price, "${price}")
        Helper.instance.setEditTextToMoneyInput(promptsView,R.id.price)
        Helper.instance.setHint(promptsView,R.id.price,"${item.rate}")


        val alertDialogBuilder = AlertDialog.Builder(
                this)
        alertDialogBuilder.setView(promptsView)
        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("Edit"
                ) { dialog, id ->
                    if (loading.visibility == View.GONE) {
                        val qty = promptsView.findViewById<EditText>(R.id.qty).text.toString()
                        val price = promptsView.findViewById<EditText>(R.id.price).text.toString()
                        if (qty.equals("") || price.equals("")) {
                            Toast.makeText(applicationContext,getString(R.string.field_required), Toast.LENGTH_SHORT).show()
                            editItem(item, position)
                        } else if (qty.equals("0")) {
                            Toast.makeText(applicationContext,"kuantitas harus diisi", Toast.LENGTH_SHORT).show()
                            editItem(item, position)
                        } else {
                            val sid = salesItemAdapter
                            if (sid is SalesOrderItemAdapter) {
                                item.qty = Helper.instance.fromStringToInt(qty)
                                item.rate = Helper.instance.fromStringToDecimal(price)
                                item.amount = item.qty * item.rate
                                grandTotal += item.amount
                                sid.editItem(item, position)
                                itemsTableForm.refreshItems()

                            }
                        }
                    }
                }

        // create alert dialog
        val alertDialog = alertDialogBuilder.create()

        // show it
        alertDialog.show()


    }

    fun updateGrandTotal() {
        val grand_total = findViewById<TextView>(R.id.grand_total)

        grandTotal = 0.0
        salesItems.forEach { si ->
            grandTotal += si.amount
        }
        val total = Helper.instance.convertDecimal(grandTotal)
        val currency = currencyForm.field
        grand_total.setText("$currency $total")
    }

    override fun validating():Boolean {
        return super.validating()
    }
}