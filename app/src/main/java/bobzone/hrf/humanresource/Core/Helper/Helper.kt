package bobzone.hrf.humanresource.Core.Helper

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import bobzone.hrf.humanresource.Core.Global.AppConfiguration
import bobzone.hrf.humanresource.Core.Global.MyCookieJar
import bobzone.hrf.humanresource.Helper.APIHelper
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.xml.transform.dom.DOMLocator

/**
 * Created by davidwibisono on 12/23/17.
 */

class Helper private constructor() {
    private object Holder { val INSTANCE = Helper() }

    companion object {
        val instance: Helper by lazy { Holder.INSTANCE }
    }


    //JSON Helper
    fun convertJsonToString(obj:Any) : String {
        val gson = Gson()
        val result = gson.toJson(obj)
        Log.d("HELPER", result)
        return result
    }

    //LANGUAGE
    fun getLanguage(activity: Activity) {
        val prefs = activity.getSharedPreferences("LanguageSwitch", Context.MODE_PRIVATE)
        val language = prefs.getString("language", "English")

        if (language!!.contentEquals("English")) {
            setLocal(activity, "en")
        } else {
            setLocal(activity, "in")
        }
    }

    private fun setLocal(activity: Activity, language: String) {
        val myLocale: Locale
        myLocale = Locale(language)
        val res = activity.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
    }

    //API
    fun getAPIWithCookie(context: Context, cookieJar: MyCookieJar?) : APIHelper {
        if (cookieJar == null) {
            Log.e("LOGIN","USER COOKIES EXPIRY")

            //create client to get cookies from OkHttp
            val okHttpClient = OkHttpClient.Builder()
            val client = okHttpClient
                    .build()

            //add cookie jar intercept to retrofit
            val retrofit = Retrofit.Builder()
                    .baseUrl(AppConfiguration.instance.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            return retrofit.create(APIHelper::class.java)
        } else {
            //create client to get cookies from OkHttp
            val okHttpClient = OkHttpClient.Builder()
            okHttpClient.cookieJar(cookieJar)
            val client = okHttpClient
                    .build()

            //add cookie jar intercept to retrofit
            val retrofit = Retrofit.Builder()
                    .baseUrl(AppConfiguration.instance.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            return retrofit.create(APIHelper::class.java)
        }
    }




    fun getGoogleMapsAPI(): APIHelper {
        //create client to get cookies from OkHttp
        val okHttpClient = OkHttpClient.Builder()
        val client = okHttpClient
                .build()

        //add cookie jar intercept to retrofit
        val retrofit = Retrofit.Builder()
                .baseUrl(AppConfiguration.instance.GOOGLEMAP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        return retrofit.create(APIHelper::class.java)
    }

    fun getGoogleMapsRoadsAPI(): APIHelper {
        //create client to get cookies from OkHttp
        val okHttpClient = OkHttpClient.Builder()
        val client = okHttpClient
                .build()

        //add cookie jar intercept to retrofit
        val retrofit = Retrofit.Builder()
                .baseUrl(AppConfiguration.instance.GOOGLEMAP_ROADS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        return retrofit.create(APIHelper::class.java)
    }

    //MONEY FORMATTER
    fun convertDecimal(dec:Double):String {
        val nf_ge = NumberFormat.getInstance(Locale.GERMAN)
        val number_ge = nf_ge.format(dec.toDouble())
        if (number_ge != null)
            return number_ge
        else
            return "0"
    }
    fun convertInt(dec:Int):String {
        val nf_ge = NumberFormat.getInstance(Locale.GERMAN)
        val number_ge = nf_ge.format(dec.toDouble())
        if (number_ge != null)
            return number_ge
        else
            return "0"
    }
    fun normalizeDigit(str: String):String {
        return str.replace(".","").replace(",","")
    }
    fun fromStringToDecimal(str:String):Double {
        if (str.equals("")) return 0.0
        return normalizeDigit(str).toDouble()
    }
    fun fromStringToInt(str:String):Int {
        if (str.equals("")) return 0
        return normalizeDigit(str).toInt()
    }


    //TOAST
    fun showConnectivityUnstable(context: Context) {
        Toast.makeText(context.applicationContext,context.getString(R.string.toast_connectivity_unstable),
                        AppConfiguration.instance.TOAST_INTERVAL_TIME).show()
    }
    fun showServerError(context: Context, errorCode:Int) {
        Toast.makeText(context.applicationContext,"Server error with code $errorCode",
                AppConfiguration.instance.TOAST_INTERVAL_TIME).show()
    }
    fun showServerNotResponding(context: Context) {
        Toast.makeText(context.applicationContext,"Server is not responding",
                AppConfiguration.instance.TOAST_INTERVAL_TIME).show()
    }



    //TEXTVIEW
    fun setEditTextToMoneyInput(view:View,  fieldID:Int) {
        val f = view.findViewById<EditText>(fieldID)
        if (f is EditText) {
            f.setSelectAllOnFocus(true)
            var lastInput = f.text.toString()
            f.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    f.setSelection(f.text.length)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val now = p0.toString()
                    if (lastInput.equals(now)) return;
                    lastInput = now
                    f.setText(convertDecimal(fromStringToDecimal(now)))
                }

            })
        }
    }
    fun setTextView(view:View,  fieldID:Int, field:String) {
        val f = view.findViewById<TextView>(fieldID)
        if (f is TextView) {
            f.setText(field)
        }
    }
    fun setHint(view:View,  fieldID:Int, hint:String) {
        val f = view.findViewById<EditText>(fieldID)
        if (f is EditText) {
            f.setHint(hint)
        }
    }
    fun setTextView(a:Activity,  fieldID:Int, field:String) {
        val f = a.findViewById<TextView>(fieldID)
        if (f is TextView) {
            f.setText(field)
        }
    }
    fun setReadOnly(view:View,  fieldID:Int) {
        val f = view.findViewById<EditText>(fieldID)
        if (f is EditText) {
            f.setOnClickListener(null)
            f.keyListener = null
        }
    }

    fun setHeight(view:View, height:Int) {
        val params = view.layoutParams
        params.height = height
        view.layoutParams = params
        view.requestLayout()
    }

    //LISTVIEW
    fun setAndGetListViewHeightBasedOnChildren(listView: ListView): Int {
        val listAdapter = listView.adapter
                ?: // pre-condition
                return 0

        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)
        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
        return params.height
    }

    fun stringToDate(yyyy_mm_dd:String):Date {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val myDate = simpleDateFormat.parse(yyyy_mm_dd)
        return myDate
    }



    //DATE
    internal fun formatDateFromstring(inputFormat: String, outputFormat: String, inputDate: String?): String {

        if (inputDate == null) return ""
        var parsed: Date? = null
        var outputDate = ""

        val df_input = SimpleDateFormat(inputFormat, Locale.getDefault())
        val df_output = SimpleDateFormat(outputFormat, Locale.getDefault())

        try {
            parsed = df_input.parse(inputDate)
            outputDate = df_output.format(parsed)

        } catch (e: ParseException) {
            Log.e("DATE", "ParseException - dateFormat")
        }

        return outputDate
    }
}