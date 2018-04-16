package bobzone.hrf.humanresource.Fragments.Offer.Base

import bobzone.hrf.humanresource.Model.BaseSingleMessage
import bobzone.hrf.humanresource.Model.Offer.OfferData
import retrofit2.Call

/**
 * Created by davidwibisono on 3/22/18.
 */
open interface OfferBaseListener {
    fun fetchDidSuccess(data: OfferData)
    fun getAPI(query:String, page:Int): Call<BaseSingleMessage<OfferData>>?
}