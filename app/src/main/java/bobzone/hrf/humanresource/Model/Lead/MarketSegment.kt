package bobzone.hrf.humanresource.Model.Lead


/**
 * Created by davidwibisono on 3/14/18.
 */
open class MarketSegment {
    private object Holder { val INSTANCE = MarketSegment() }

    companion object {
        val instance: MarketSegment by lazy { Holder.INSTANCE }
    }
    val LOWER_INCOME = "Lower Income"
    val MIDDLE_INCOME = "Middle Income"
    val UPPER_INCOME = "Upper Income"
}