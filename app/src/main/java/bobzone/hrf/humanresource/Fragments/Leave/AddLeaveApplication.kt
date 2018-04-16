package bobzone.hrf.humanresource.Fragments.Leave

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import bobzone.hrf.humanresource.Core.Base.Activity.FormActivity
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleCallBack
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleMessageCallBack
import bobzone.hrf.humanresource.Core.Base.Form.*
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Core.Model.CreationResponse
import bobzone.hrf.humanresource.Fragments.Customer.CustomerLocation
import bobzone.hrf.humanresource.Fragments.SalesOrder.AddSalesOrder.SearchCustomer
import bobzone.hrf.humanresource.Model.BaseSingleMessage
import bobzone.hrf.humanresource.Model.Company.CompanyData
import bobzone.hrf.humanresource.Model.Leave.LeaveApplicationStatus
import bobzone.hrf.humanresource.Model.Leave.LeaveTypeData
import bobzone.hrf.humanresource.Model.Leave.RequestLeaveApplicationData
import bobzone.hrf.humanresource.Model.Territory.TerritoryData
import bobzone.hrf.humanresource.Model.User.UserData
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import com.google.gson.JsonObject
import retrofit2.Call
import java.util.*

/**
 * Created by davidwibisono on 4/16/18.
 */
open class AddLeaveApplication : FormActivity() {

    var namingSeries = BasicSpinner("Status","status", arrayListOf("LAP/"))
    val companyForm = APIBasicSpinner<CompanyData>("Company","company")
    val employeeForm = IntentBasicInput("Employee","employee")
    var leaveTypeForm = APIBasicSpinner<LeaveTypeData>("Leave Type","leave_type")
    var fromDate = BasicDatePicker("From Date","from_date")
    var toDate = BasicDatePicker("To Date","to_date")
    var halfDayDateCheckbox = BasicCheckbox("Is Half Day Date","half_day")
    var halfDayDate = BasicDatePicker("Half Day","half_day_date")
    var reasonForm = BasicInput("Reason","description")
    var statusSpinner = BasicSpinner("Status","status", arrayListOf("Open","Approved"))
    var leaveApproverForm = APIBasicSpinner<UserData>("Leave Approver","leave_approver")
    var rejectedForm = BasicInput("Request Status","")

    var employee = ""

    override fun getLayout(): Int {
        return R.layout.activity_form_default
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.request_leave_application)
        namingSeries.isRequired = true
        formBuilder.formSheets.add(namingSeries)

        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext, cookieJar)
        val companyCall = api.getCompany()
        companyForm.call = companyCall
        companyForm.listener = object :APIBasicSpinnerEventListener<CompanyData> {
            override fun getItem(item: CompanyData): String {
                return item.name
            }

            override fun getField(item: CompanyData): String {
                return item.name
            }

            override fun fetchSuccess(objects: List<CompanyData>) {

            }
        }
        formBuilder.formSheets.add(companyForm)
        employeeForm.isRequired = true
        employeeForm.intent = Intent(applicationContext, SearchEmployee::class.java)
        employeeForm.activity = this
        employeeForm.intentListener = object: FormIntentListener {
            override fun onIntentResult(data: Intent): String {
                employee = data.getStringExtra("employee")
                val employeeName = data.getStringExtra("employee_name")
                return "$employeeName ($employee)"
            }

            override fun getField(): String {
                return employee
            }

            override fun prepareIntent(i: Intent): Intent {
                return i
            }

        }
        formBuilder.formSheets.add(employeeForm)

        val leaveTypeCall = api.getLeaveType()
        leaveTypeForm.call = leaveTypeCall
        leaveTypeForm.listener = object : APIBasicSpinnerEventListener<LeaveTypeData> {
            override fun getItem(item: LeaveTypeData): String {
                return item.name
            }

            override fun getField(item: LeaveTypeData): String {
                return item.name
            }

            override fun fetchSuccess(objects: List<LeaveTypeData>) {

            }
        }
        formBuilder.formSheets.add(leaveTypeForm)

        formBuilder.formSheets.add(statusSpinner)

        fromDate.activity = this
        fromDate.isRequired = true
        fromDate.listener = object :DatePickerBasicListener {
            override fun onChange(datePicker: DatePicker, i: Int, i1: Int, i2: Int) {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, i)
                calendar.set(Calendar.MONTH, i1)
                calendar.set(Calendar.DAY_OF_MONTH, i2)
                toDate.minDate = calendar.timeInMillis
            }
        }

        formBuilder.formSheets.add(fromDate)
        toDate.activity = this
        toDate.isRequired = true
        formBuilder.formSheets.add(toDate)

        halfDayDateCheckbox.listener = object : CheckboxListener {
            override fun onCheckChange(boolean: Boolean) {
                halfDayDate.minDate = fromDate.currentDate.time
                halfDayDate.maxDate = toDate.currentDate.time
                halfDayDate.isHidden = !boolean
                formBuilder.refresh()
            }
        }
        formBuilder.formSheets.add(halfDayDateCheckbox)

        halfDayDate.activity = this
        halfDayDate.isHidden = true
        formBuilder.formSheets.add(halfDayDate)



        reasonForm.singleLine = false
        formBuilder.formSheets.add(reasonForm)

        leaveApproverForm.isRequired = true
        leaveApproverForm.call = api.getUser()
        leaveApproverForm.listener = object : APIBasicSpinnerEventListener<UserData> {
            override fun getItem(item: UserData): String {
                return item.name
            }

            override fun getField(item: UserData): String {
                return item.name
            }

            override fun fetchSuccess(objects: List<UserData>) {
            }

        }
        formBuilder.formSheets.add(leaveApproverForm)

//        reasonForm.field = "This field will contain the validation of requested"
//        reasonForm.isReadOnly = true
//        formBuilder.formSheets.add(rejectedForm)

        formBuilder.build()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        formBuilder.onResult(resultCode,requestCode,data)
    }


    override fun requestDidResponseError(errorCode: Int) {
        Log.e("asd", "$errorCode")
    }

    override fun submitDidSuccess(creationResponse: CreationResponse) {
        setResult(Activity.RESULT_OK)
        finish()

    }


    override fun getCallSubmit(jsonObject: JsonObject): Call<CreationResponse> {
        setLoadingText("Requesting Leave Application")
        var docstatus = 0
        if (statusSpinner.selectedField.equals(LeaveApplicationStatus.instance.APPROVED)) docstatus = 1
        jsonObject.addProperty("docstatus",docstatus)
        val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
        val api = Helper.instance.getAPIWithCookie(applicationContext,cookieJar)
        return api.submitLeaveApplication(jsonObject)
    }

    var isValidating = false

    override fun validating():Boolean {
        if (toDate.currentDate.time < fromDate.currentDate.time) {
            Toast.makeText(applicationContext,getString(R.string.from_date_must_be_less_than_to_date),Toast.LENGTH_LONG).show()
            return false
        } else {
            if (!isValidating) {
                setLoadingText("Validating Request Leave Application")
                startLoading()
                isValidating = true
                val cookieJar = UserModel.instance.loadCookieJar(applicationContext)
                val api = Helper.instance.getAPIWithCookie(applicationContext, cookieJar)
                var docstatus = 0
                if (statusSpinner.selectedField.equals(LeaveApplicationStatus.instance.APPROVED)) docstatus = 1
                var halfDay = 0
                if (halfDayDateCheckbox.field.equals("true")) halfDay = 1
                val callReqLeave = api.requestLeaveApplication(employee, companyForm.field, leaveTypeForm.field, fromDate.field, toDate.field, statusSpinner.field, "$halfDay", halfDayDate.field, "$docstatus", leaveApproverForm.field)
                callReqLeave.enqueue(object : BaseSingleMessageCallBack<RequestLeaveApplicationData>(applicationContext) {
                    override fun failed(error: Throwable) {
                        stopLoading()
                        isValidating = false
                        Log.e("Request Leave App Error", error.message)
                    }

                    override fun responseData(data: RequestLeaveApplicationData) {
                        isValidating = false
                        if (data.result.equals("success")) {
                            formBuilder.submit(true)
                        } else {
                            stopLoading()
                            Toast.makeText(applicationContext,data.error_message[0],Toast.LENGTH_SHORT).show()
                        }

                    }
                })
            }
        }



        return false
    }


}