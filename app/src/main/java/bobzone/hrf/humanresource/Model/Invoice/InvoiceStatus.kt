package bobzone.hrf.humanresource.Model.SalesOrder

import bobzone.hrf.humanresource.Core.Global.Constant

/**
 * Created by davidwibisono on 3/6/18.
 */
open class InvoiceStatus private constructor() {
    private object Holder { val INSTANCE = InvoiceStatus() }

    companion object {
        val instance: InvoiceStatus by lazy { Holder.INSTANCE }
    }
    val PAID = "Paid"
    val UNPAID = "Unpaid"
    val OVERDUE = "Overdue"
}