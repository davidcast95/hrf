package bobzone.hrf.humanresource.Model.Lead


/**
 * Created by davidwibisono on 3/14/18.
 */
open class LeadStatus {
    private object Holder { val INSTANCE = LeadStatus() }

    companion object {
        val instance: LeadStatus by lazy { Holder.INSTANCE }
    }
    val LEAD = "Lead"
    val OPEN = "Open"
    val REPLIED = "Replied"
    val OPPORTUNITY = "Opportunity"
    val QUOTATION = "Quotation"
    val LOST_QUOTATION = "Lost Quotation"
    val INTERESTED = "Interested"
    val CONVERTED = "Converted"
    val DO_NOT_CONTACT = "Do Not Contact"
}