package bobzone.hrf.humanresource.Fragments.Offer.Base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleMessageCallBack
import bobzone.hrf.humanresource.Core.Base.List.MasterListDelegate
import bobzone.hrf.humanresource.Core.Base.List.MasterListEventListener
import bobzone.hrf.humanresource.Core.Global.Constant
import bobzone.hrf.humanresource.Fragments.Invoice.ViewInvoice
import bobzone.hrf.humanresource.Fragments.Offer.Active.OfferActive
import bobzone.hrf.humanresource.Fragments.Offer.Done.OfferDone
import bobzone.hrf.humanresource.Fragments.Offer.Pending.OfferPending
import bobzone.hrf.humanresource.Fragments.Offer.ViewOffer
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.BaseSingleMessage
import bobzone.hrf.humanresource.Model.Lead.LeadStatus
import bobzone.hrf.humanresource.Model.Offer.OfferData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.Model.UserSettings
import bobzone.hrf.humanresource.R
import com.google.gson.Gson
import com.paging.listview.PagingListView
import retrofit2.Call

/**
 * Created by davidwibisono on 3/14/18.
 */
open class OfferBase : Fragment(), PagingListView.Pagingable, SearchView.OnQueryTextListener, OfferBaseListener {

    var generatedView: View? = null
    var offerData: OfferData = OfferData()
    var offerAdapter:OfferAdapter? = null

    var loading: RelativeLayout? = null
    var listView: PagingListView? = null
    var swipeRefresh: SwipeRefreshLayout? = null
    var noDataTV:TextView? = null
    
    var status = ""
    var leadToggle = true
    var openToggle = true
    var repliedToggle = true
    var opportunityToggle = true
    var interestedToggle = true
    var quotationToggle = true
    var lostQuotationToggle = true
    var convertedToggle = true
    var doNotContactToggle = true
    var query = ""
    var page = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val infl = inflater
        if (infl is LayoutInflater) {
            val v = inflater.inflate(R.layout.fragment_lead_base, container, false)
            generatedView = v
            setupStatusToggle(v, context)

            loading = v.findViewById<RelativeLayout>(R.id.loading)
            listView = v.findViewById<PagingListView>(R.id.master_list)
            swipeRefresh = v.findViewById(R.id.swipe_refresh)
            noDataTV = v.findViewById<TextView>(R.id.no_data)
            
            offerAdapter = OfferAdapter(context)
            val lv = listView
            if (lv is ListView) {
                lv.adapter = offerAdapter
                lv.setHasMoreItems(false)
                lv.setPagingableListener(this)

                lv.setOnItemClickListener(object :AdapterView.OnItemClickListener {
                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        onListTapped(p2)
                    }

                })
            }

            val _swipeRefresh = swipeRefresh
            if (_swipeRefresh is SwipeRefreshLayout) {
                _swipeRefresh.setOnRefreshListener {
                    refreshItems()
                }
            }

            fetchOfferData()
            
            return v
        }
        return view
    }

    fun setupStatusToggle(v: View, c: Context) {
        val statusToggle = v.findViewById<LinearLayout>(R.id.status_toggle)
        statusToggle.visibility = View.VISIBLE
        val leadToggleView = v.findViewById<TextView>(R.id.lead_toggle)
        val openToggleView = v.findViewById<TextView>(R.id.open_toggle)
        val repliedToggleView = v.findViewById<TextView>(R.id.replied_toggle)
        val opportunityToggleView = v.findViewById<TextView>(R.id.opportunity_toggle)
        val interestedToggleView = v.findViewById<TextView>(R.id.interested_toggle)
        val quotationToggleView = v.findViewById<TextView>(R.id.quotation_toggle)
        val lostQuotationToggleView = v.findViewById<TextView>(R.id.lost_quotation_toggle)
        val convertedToggleView = v.findViewById<TextView>(R.id.converted_toggle)
        val doNotContactToggleView = v.findViewById<TextView>(R.id.do_not_contact_toggle)

        if (this is OfferPending) {

            quotationToggleView.visibility = View.GONE
            lostQuotationToggleView.visibility = View.GONE
            convertedToggleView.visibility = View.GONE
            doNotContactToggleView.visibility = View.GONE


            leadToggle = UserModel.instance.getUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_LEAD, leadToggle)
            openToggle = UserModel.instance.getUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_OPEN, openToggle)
            repliedToggle = UserModel.instance.getUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_REPLIED, repliedToggle)
            opportunityToggle = UserModel.instance.getUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_OPPORTUNITY, opportunityToggle)
            interestedToggle = UserModel.instance.getUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_INTERESTED, interestedToggle)


            setupAlpha(leadToggleView,leadToggle)
            setupAlpha(openToggleView,openToggle)
            setupAlpha(repliedToggleView,repliedToggle)
            setupAlpha(opportunityToggleView,opportunityToggle)
            setupAlpha(interestedToggleView,interestedToggle)

            leadToggleView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    leadToggle = !leadToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_LEAD,leadToggle)
                    setupAlpha(leadToggleView,leadToggle)
                    updateStatus()
                }
            })
            openToggleView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    openToggle = !openToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_OPEN,openToggle)
                    setupAlpha(openToggleView,openToggle)
                    updateStatus()
                }
            })
            repliedToggleView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    repliedToggle = !repliedToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_REPLIED,repliedToggle)
                    setupAlpha(repliedToggleView,repliedToggle)
                    updateStatus()
                }
            })
            opportunityToggleView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    opportunityToggle = !opportunityToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_OPPORTUNITY,opportunityToggle)
                    setupAlpha(opportunityToggleView,opportunityToggle)
                    updateStatus()
                }
            })
            interestedToggleView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    interestedToggle = !interestedToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_INTERESTED,interestedToggle)
                    setupAlpha(interestedToggleView,interestedToggle)
                    updateStatus()
                }
            })


            updateStatus()

        } else if (this is OfferActive) {

            leadToggleView.visibility = View.GONE
            openToggleView.visibility = View.GONE
            repliedToggleView.visibility = View.GONE
            opportunityToggleView.visibility = View.GONE
            interestedToggleView.visibility = View.GONE
            convertedToggleView.visibility = View.GONE
            doNotContactToggleView.visibility = View.GONE

            quotationToggle = UserModel.instance.getUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_QUOTATION, quotationToggle)
            lostQuotationToggle = UserModel.instance.getUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_LOST_QUOTATION, lostQuotationToggle)


            setupAlpha(quotationToggleView,quotationToggle)
            setupAlpha(lostQuotationToggleView,lostQuotationToggle)

            quotationToggleView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    quotationToggle = !quotationToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_QUOTATION,quotationToggle)
                    setupAlpha(quotationToggleView,quotationToggle)
                    updateStatus()
                }
            })
            lostQuotationToggleView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    lostQuotationToggle = !lostQuotationToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_LOST_QUOTATION,lostQuotationToggle)
                    setupAlpha(lostQuotationToggleView,lostQuotationToggle)
                    updateStatus()
                }
            })

            updateStatus()

        } else if (this is OfferDone) {
            leadToggleView.visibility = View.GONE
            openToggleView.visibility = View.GONE
            repliedToggleView.visibility = View.GONE
            opportunityToggleView.visibility = View.GONE
            interestedToggleView.visibility = View.GONE
            quotationToggleView.visibility = View.GONE
            lostQuotationToggleView.visibility = View.GONE

            convertedToggle = UserModel.instance.getUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_CONVERTED, convertedToggle)
            doNotContactToggle = UserModel.instance.getUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_DO_NOT_CONTACT, doNotContactToggle)


            setupAlpha(convertedToggleView,convertedToggle)
            setupAlpha(doNotContactToggleView,doNotContactToggle)

            convertedToggleView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    convertedToggle = !convertedToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_CONVERTED,convertedToggle)
                    setupAlpha(convertedToggleView,convertedToggle)
                    updateStatus()
                }
            })
            doNotContactToggleView.setOnClickListener(object: View.OnClickListener {
                override fun onClick(p0: View?) {
                    doNotContactToggle = !doNotContactToggle
                    UserModel.instance.setUserPreferencesAsBoolean(c, UserSettings.instance.LEAD_DO_NOT_CONTACT,doNotContactToggle)
                    setupAlpha(doNotContactToggleView,doNotContactToggle)
                    updateStatus()
                }
            })
            updateStatus()
        } else {
            statusToggle.visibility = View.GONE
        }
    }

    fun setupAlpha(v: TextView, toggle:Boolean) {
        if (toggle) {
            v.alpha = 1f
        } else {
            v.alpha = 0.5f
        }
    }

    fun updateStatus() {
        if (this is OfferPending) {
            status = ""

            if (leadToggle) status += "${LeadStatus.instance.LEAD},"
            if (openToggle) status += "${LeadStatus.instance.OPEN},"
            if (repliedToggle) status += "${LeadStatus.instance.REPLIED},"
            if (opportunityToggle) status += "${LeadStatus.instance.OPPORTUNITY},"
            if (interestedToggle) status += LeadStatus.instance.INTERESTED
            refreshItems()
        } else if (this is OfferActive) {
            status = ""

            if (quotationToggle) status += "${LeadStatus.instance.QUOTATION},"
            if (lostQuotationToggle) status += LeadStatus.instance.LOST_QUOTATION
            refreshItems()
        } else if (this is OfferDone) {
            status = ""

            if (convertedToggle) status += "${LeadStatus.instance.CONVERTED},"
            if (doNotContactToggle) status += LeadStatus.instance.DO_NOT_CONTACT
            refreshItems()
        }
    }

    fun refreshItems() {
        page = 0
        val adapter = offerAdapter
        if (adapter is OfferAdapter)
            adapter.cleared()
        fetchOfferData()
    }

    fun onListTapped(position: Int) {
        val viewOffer = Intent(context, ViewOffer::class.java)
        val json = Gson().toJson(offerData)
        viewOffer.putExtra("offer",json)
        startActivity(viewOffer)
    }

    override fun onLoadMoreItems() {
        fetchOfferData()
    }
    
    override fun getAPI(query:String, page:Int):Call<BaseSingleMessage<OfferData>>? {
        return null
    }

    override fun fetchDidSuccess(data: OfferData) {

    }

    override fun onQueryTextChange(p0: String?): Boolean {
        val q = p0
        if (q is String)
            query = q
        refreshItems()
        return true
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }
    
    //API
    fun fetchOfferData() {
        val _loading = loading
        if (_loading is RelativeLayout && page == 0) _loading.visibility = View.VISIBLE
        val start = page++ * Constant.instance.LIMIT_MASTER_LIST
        val callOffer = getAPI(query,start)
        if (callOffer != null) {
            callOffer.enqueue(object: BaseSingleMessageCallBack<OfferData>(context) {
                override fun responseData(data: OfferData) {
                    val sr = swipeRefresh
                    if (sr is SwipeRefreshLayout)
                        sr.isRefreshing = false
                    val lv = listView
                    if (lv is ListView) {
                        if (data.leads.size + data.quotations.size + data.opportunities.size > 0) {
                            lv.onFinishLoading(true, mutableListOf())
                            val adapter = offerAdapter
                            if (adapter is OfferAdapter) {
                                adapter.leads.addAll(data.leads)
                                adapter.quotations.addAll(data.quotations)
                                adapter.opportunities.addAll(data.opportunities)
                                adapter.notifyDataSetChanged()
                            }
                        } else {
                            lv.onFinishLoading(false,null)
                        }
                    }


                    fetchDidSuccess(data)
                    if (_loading is RelativeLayout) _loading.visibility = View.GONE
                }

                override fun failed(error: Throwable) {
                    val sr = swipeRefresh
                    if (sr is SwipeRefreshLayout)
                        sr.isRefreshing = false
                    if (_loading is RelativeLayout) _loading.visibility = View.GONE
                }
            })
        }
    }

}