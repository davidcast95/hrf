package bobzone.hrf.humanresource.Fragments.Leave.Application

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleMessageCallBack
import bobzone.hrf.humanresource.Core.Base.List.MasterListDelegate
import bobzone.hrf.humanresource.Core.Base.List.MasterListEventListener
import bobzone.hrf.humanresource.Core.Global.Constant
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Core.Model.CreationResponse
import bobzone.hrf.humanresource.Fragments.SalesOrder.Active.SalesOrderActive
import bobzone.hrf.humanresource.Fragments.SalesOrder.AddSalesOrder.Item
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.Item.ItemArgs
import bobzone.hrf.humanresource.Model.Leave.LeaveApplicationData
import bobzone.hrf.humanresource.Model.Leave.LeaveApplicationStatus
import bobzone.hrf.humanresource.Model.SalesOrderItem.ItemData
import bobzone.hrf.humanresource.Model.SalesOrderItem.ItemDetailsData
import bobzone.hrf.humanresource.Model.SalesOrderItem.SalesOrderItemData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.Model.User.UserResponse
import bobzone.hrf.humanresource.Model.UserSettings
import bobzone.hrf.humanresource.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by davidwibisono on 3/6/18.
 */
open class LeaveApplication : Fragment(), MasterListEventListener<LeaveApplicationData> {

    var generatedView:View? = null
    var masterListDelegate: MasterListDelegate<LeaveApplicationData> = MasterListDelegate()
    var leaveApplicationList: List<LeaveApplicationData> = ArrayList<LeaveApplicationData>()
    var status = ""
    var open = true
    var approved = true
    var rejected = true

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val infl = inflater
        if (infl is LayoutInflater) {
            val v = inflater.inflate(R.layout.fragment_leave_application, container, false)
            generatedView = v
            setupStatusToggle(v, context)

            val leaveApplicationAdapter = LeaveApplicationAdapter(context, leaveApplicationList)
            masterListDelegate.setup(v, leaveApplicationAdapter, context)
            masterListDelegate.listenerMaster = this

            masterListDelegate.onFetchData()
            return v
        }
        return view
    }

    fun setupStatusToggle(v:View,c:Context) {
        val statusToggle = v.findViewById<LinearLayout>(R.id.status_toggle)
        statusToggle.visibility = View.VISIBLE

        open = UserModel.instance.getUserPreferencesAsBoolean(c,UserSettings.instance.LEAVE_APPLICATION_OPEN,open)
        approved = UserModel.instance.getUserPreferencesAsBoolean(c,UserSettings.instance.LEAVE_APPLICATION_APPROVED,approved)
        rejected = UserModel.instance.getUserPreferencesAsBoolean(c,UserSettings.instance.LEAVE_APPLICATION_REJECTED,rejected)

        val openToggleView = v.findViewById<TextView>(R.id.open_toggle)
        val approvedToggleView = v.findViewById<TextView>(R.id.approved_toggle)
        val rejectedToggleView = v.findViewById<TextView>(R.id.rejected_toggle)

        setupAlpha(openToggleView,open)
        setupAlpha(approvedToggleView,approved)
        setupAlpha(rejectedToggleView,rejected)

        openToggleView.setOnClickListener(object:View.OnClickListener {
            override fun onClick(p0: View?) {
                open = !open
                UserModel.instance.setUserPreferencesAsBoolean(c,UserSettings.instance.LEAVE_APPLICATION_OPEN,open)
                setupAlpha(openToggleView,open)
                updateStatus()
            }
        })
        approvedToggleView.setOnClickListener(object:View.OnClickListener {
            override fun onClick(p0: View?) {
                approved = !approved
                UserModel.instance.setUserPreferencesAsBoolean(c,UserSettings.instance.LEAVE_APPLICATION_APPROVED,approved)
                setupAlpha(approvedToggleView,approved)
                updateStatus()
            }
        })
        rejectedToggleView.setOnClickListener(object:View.OnClickListener {
            override fun onClick(p0: View?) {
                rejected = !rejected
                UserModel.instance.setUserPreferencesAsBoolean(c,UserSettings.instance.LEAVE_APPLICATION_REJECTED,rejected)
                setupAlpha(rejectedToggleView,rejected)
                updateStatus()
            }
        })
        updateStatus()
    }

    fun setupAlpha(v:TextView,toggle:Boolean) {
        if (toggle) {
            v.alpha = 1f
        } else {
            v.alpha = 0.5f
        }
    }

    fun updateStatus() {
        status = ""
        if (open) status += "${LeaveApplicationStatus.instance.OPEN},"
        if (approved) status += "${LeaveApplicationStatus.instance.APPROVED},"
        if (rejected) status += LeaveApplicationStatus.instance.REJECTED
        refreshItems()
    }

    fun refreshItems() {
        if (this is SalesOrderActive) {
            this.updateStatusToggle()
        }
        masterListDelegate.refreshItems()
    }

    override fun willRefreshItems() {

    }

    override fun fetchDidSuccess(data: MutableList<LeaveApplicationData>) {

    }


    override fun getAPIHandlingOnBaseMessage(query: String, page: Int): Call<BaseMessage<LeaveApplicationData>>? {
        val cookieJar = UserModel.instance.loadCookieJar(context)
        val api = Helper.instance.getAPIWithCookie(context,cookieJar)
        val start = page * Constant.instance.LIMIT_MASTER_LIST
        return api.getLeaveApplication(status,query,"$start")
    }

    override fun getAPIHandlingOnBaseResponse(query: String, page: Int): Call<BaseResponse<LeaveApplicationData>>? {
        return null
    }

    override fun onListTapped(item: LeaveApplicationData, position: Int) {
        val fullname = UserModel.instance.loadString(UserModel.instance.fullname, context)
        if (item.leave_approver != null) {
            if (item.leave_approver.equals(fullname)) {
                approveItem(item)
            } else {
                Toast.makeText(context, "${getString(R.string.only)} ${item.leave_approver} ${getString(R.string.can_approve_this_leave_application)}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "${getString(R.string.leave_approver_is_null)}", Toast.LENGTH_SHORT).show()
        }
    }

    var approvalDialog:AlertDialog? = null

    fun approveItem(item: LeaveApplicationData) {

        AlertDialog.Builder(activity)
        val li = LayoutInflater.from(activity)
        val promptsView = li.inflate(R.layout.dialog_leave_application_approval, null)

        Helper.instance.setTextView(promptsView,R.id.leave_no,item.leave_no)
        Helper.instance.setTextView(promptsView,R.id.employee_name, item.employee_name)

        var formatView = "EEE, d MMM yyyy"
        var formatField = "yyyy-MM-dd"
        val fromDate = Helper.instance.formatDateFromstring(formatField, formatView, item.from_date)

        val toDate = Helper.instance.formatDateFromstring(formatField, formatView, item.to_date)
        Helper.instance.setTextView(promptsView,R.id.interval_date, "${item.leave_type}\n$fromDate - $toDate")

        if (item.description != null)
            Helper.instance.setTextView(promptsView,R.id.description, item.description)

        var days = context.getString(R.string.day)
        if (days.equals("day")) {
            if (item.total_leave_days > 1) {
                days = "days"
            }
        }
        Helper.instance.setTextView(promptsView,R.id.total_leaves,"${item.total_leave_days} $days")

        val approveButton = promptsView.findViewById<TextView>(R.id.approve_button)
        val rejectButton = promptsView.findViewById<TextView>(R.id.reject_button)
        approveButton.setOnClickListener(object :View.OnClickListener {
            override fun onClick(p0: View?) {
                authProcess(item, true)
            }
        })
        rejectButton.setOnClickListener(object :View.OnClickListener {
            override fun onClick(p0: View?) {
                authProcess(item, false)
            }
        })

        val alertDialogBuilder = AlertDialog.Builder(
                activity)
        alertDialogBuilder.setView(promptsView)


        // create alert dialog

        approvalDialog = alertDialogBuilder.create()
        val ad = approvalDialog
        if (ad != null) {
            ad.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            ad.show()
        }

    }

    fun authProcess(item:LeaveApplicationData,approve:Boolean) {
        AlertDialog.Builder(activity)
        val li = LayoutInflater.from(activity)
        val promptsView = li.inflate(R.layout.dialog_leave_application_auth, null)


        val alertDialogBuilder = AlertDialog.Builder(
                activity)
        alertDialogBuilder.setView(promptsView)
        // create alert dialog
        val alertDialog = alertDialogBuilder.create()

        alertDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val auth = promptsView.findViewById<TextView>(R.id.auth)
        val proceedButton = promptsView.findViewById<TextView>(R.id.proceed_button)
        val cancelButton = promptsView.findViewById<TextView>(R.id.cancel_button)
        val password = promptsView.findViewById<EditText>(R.id.password)
        proceedButton.setOnClickListener(object :View.OnClickListener {
            override fun onClick(p0: View?) {
                if (password.text.toString().equals("")) {
                    Toast.makeText(context,getString(R.string.please_insert_your_password_to_proceed),Toast.LENGTH_SHORT).show()
                } else {
                    var count = 0
                    val timerLoading = Timer()
                    timerLoading.scheduleAtFixedRate(object: TimerTask() {
                        override fun run() {
                            var loading = ""
                            for (i in 0..count) {
                                loading += "."
                            }
                            count++
                            count = count % 3
                            auth.setText("${getString(R.string.authenticating)}$loading")
                        }
                    },0,500)

                    val cookieJar = UserModel.instance.loadCookieJar(context)
                    val api = Helper.instance.getAPIWithCookie(context,cookieJar)
                    val username = UserModel.instance.loadString(UserModel.instance.email,context)

                    val callLogin = api.loginUser(username,password.text.toString(),"mobile")
                    callLogin.enqueue(object : Callback<UserResponse> {
                        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                            if (response.code() == 200) {
                                submitUpdate(item,approve)
                                alertDialog.dismiss()
                            } else {
                                timerLoading.cancel()
                                auth.setText(getString(R.string.authentication))
                                cancelButton.setOnClickListener(object :View.OnClickListener {
                                    override fun onClick(p0: View?) {
                                        alertDialog.dismiss()
                                    }
                                })
                                alertDialog.setCancelable(true)
                                Toast.makeText(context,getString(R.string.invalid_password),Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<UserResponse>, throwable: Throwable) {
                            cancelButton.setOnClickListener(object :View.OnClickListener {
                                override fun onClick(p0: View?) {
                                    alertDialog.dismiss()
                                }
                            })
                            alertDialog.setCancelable(true)
                            Helper.instance.showConnectivityUnstable(context)
                        }
                    })


                    cancelButton.setOnClickListener(null)
                    alertDialog.setCancelable(false)
                }



            }
        })
        cancelButton.setOnClickListener(object :View.OnClickListener {
            override fun onClick(p0: View?) {
                alertDialog.dismiss()
            }
        })

        alertDialog.show()
    }

    fun submitUpdate(item:LeaveApplicationData, approve:Boolean) {
        val cookieJar = UserModel.instance.loadCookieJar(context)
        val api = Helper.instance.getAPIWithCookie(context,cookieJar)
        val json = JsonObject()
        json.addProperty("docstatus",1)
        if (approve) {
            json.addProperty("status",LeaveApplicationStatus.instance.APPROVED)
        } else {
            json.addProperty("status",LeaveApplicationStatus.instance.REJECTED)
        }
        val callUpdateLeaveApplication = api.updateLeaveApplication(item.leave_no,json)
        callUpdateLeaveApplication.enqueue(object : Callback<CreationResponse> {
            override fun onFailure(call: Call<CreationResponse>, t: Throwable?) {
                Helper.instance.showConnectivityUnstable(context)
            }

            override fun onResponse(call: Call<CreationResponse>, response: Response<CreationResponse>) {
                if (response.code() == 200) {
                    val ad = approvalDialog
                    if (ad != null)
                        ad.dismiss()
                    refreshItems()
                    if (approve)
                        Toast.makeText(context,"${item.leave_no} ${getString(R.string.has_approved)}",Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(context,"${item.leave_no} ${getString(R.string.has_rejected)}",Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

}