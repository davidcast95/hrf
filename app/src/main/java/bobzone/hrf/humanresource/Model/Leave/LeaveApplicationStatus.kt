package bobzone.hrf.humanresource.Model.Leave

/**
 * Created by davidwibisono on 4/16/18.
 */
open class LeaveApplicationStatus {
    private object Holder { val INSTANCE = LeaveApplicationStatus() }

    companion object {
        val instance: LeaveApplicationStatus by lazy { Holder.INSTANCE }
    }
    val OPEN = "Open"
    val APPROVED = "Approved"
    val REJECTED = "Rejected"
}