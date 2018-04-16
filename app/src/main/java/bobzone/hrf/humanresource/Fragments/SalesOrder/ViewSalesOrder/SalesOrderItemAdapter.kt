package bobzone.hrf.humanresource.Fragments.SalesOrder.ViewSalesOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import bobzone.hrf.humanresource.Core.Base.Callback.BaseResponseCallBack
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.InvoiceItem.InvoiceItemData
import bobzone.hrf.humanresource.Model.SalesOrderItem.SalesOrderItemData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R

/**
 * Created by davidwibisono on 3/7/18.
 */
open class SalesOrderItemAdapter(context: Context, objects: MutableList<SalesOrderItemData>) :
        ArrayAdapter<SalesOrderItemData>(context, R.layout.list_sales_item, objects) {
    var list = mutableListOf<SalesOrderItemData>()

    init {
        this.list = objects
    }

    override fun getCount(): Int {
        return this.list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.list_sales_item, parent, false)
        val item = list[position]
        Helper.instance.setTextView(view,R.id.item_name,item.item_name)
        Helper.instance.setTextView(view,R.id.item_code,item.item_code)
        val qty = Helper.instance.convertInt(item.qty)
        val price_rate = Helper.instance.convertDecimal(item.price_list_rate)
        Helper.instance.setTextView(view,R.id.qty,"${qty} ${item.uom}")
        Helper.instance.setTextView(view,R.id.rate,"@ ${item.currency} ${price_rate}")

        if (item.discount_percentage > 0) {
            Helper.instance.setTextView(view,R.id.discount,"Disc ${item.discount_percentage} %")

            val after_discount = Helper.instance.convertDecimal(item.rate)
            Helper.instance.setTextView(view,R.id.afterdiscount, "@ ${item.currency} $after_discount")

            val amount = Helper.instance.convertDecimal(item.amount)
            Helper.instance.setTextView(view,R.id.amount,"${item.currency} ${amount}")
        } else {
            val amount = Helper.instance.convertDecimal(item.amount)
            Helper.instance.setTextView(view,R.id.amount,"${item.currency} ${amount}")
        }


        val cookieJar = UserModel.instance.loadCookieJar(context)
        val api = Helper.instance.getAPIWithCookie(context, cookieJar)
        val callLinkInvoice = api.getSalesInvoiceFromItem("[[\"Sales Invoice Item\",\"so_detail\",\"=\",\"${item.name}\"]]")
        callLinkInvoice.enqueue(object: BaseResponseCallBack<InvoiceItemData>(context) {
            override fun responseData(data: MutableList<InvoiceItemData>) {
                if (data.size > 0) {
                    val invoiceItem = data[0]
                    Helper.instance.setTextView(view,R.id.link_so,"link to ${invoiceItem.parent}")
                    item.invoice_no = invoiceItem.parent
                }
            }

            override fun failed(error: Throwable) {

            }
        })

        return view

    }

    fun editItem(item:SalesOrderItemData, position: Int) {
        if (position < list.size)
            this.list[position] = item
    }

}