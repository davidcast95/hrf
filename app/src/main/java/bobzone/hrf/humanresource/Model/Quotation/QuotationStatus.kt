package bobzone.hrf.humanresource.Model.Quotation


/**
 * Created by davidwibisono on 3/22/18.
 */
open class QuotationStatus {
    private object Holder { val INSTANCE = QuotationStatus() }

    companion object {
        val instance: QuotationStatus by lazy { Holder.INSTANCE }
    }
    val SUBMITTED = "Submitted"
    val ORDERED = "Ordered"
}