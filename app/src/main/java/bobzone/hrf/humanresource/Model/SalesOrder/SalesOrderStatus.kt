package bobzone.hrf.humanresource.Model.SalesOrder

import bobzone.hrf.humanresource.Core.Global.Constant

/**
 * Created by davidwibisono on 3/6/18.
 */
open class SalesOrderStatus private constructor() {
    private object Holder { val INSTANCE = SalesOrderStatus() }

    companion object {
        val instance: SalesOrderStatus by lazy { Holder.INSTANCE }
    }
    val TO_DELIVER_AND_BILL = "To Deliver and Bill"
    val TO_DELIVER = "To Deliver"
    val TO_BILL = "To Bill"
    val COMPLETED = "Completed"
    val CANCELLED = "Cancelled"
}