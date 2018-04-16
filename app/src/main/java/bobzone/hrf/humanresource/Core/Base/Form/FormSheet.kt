package bobzone.hrf.humanresource.Core.Base.Form

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Spinner
import bobzone.hrf.humanresource.Core.Base.Callback.BaseResponseCallBack
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.R
import com.google.gson.Gson
import com.google.gson.JsonArray
import org.json.JSONObject
import retrofit2.Call
import java.util.*

/**
 * Created by davidwibisono on 2/13/18.
 */

open class FormSheet {
    var formLayout:Int = 0
    var fieldName = ""
    var fieldID = ""
    var hint = ""
    var field = ""
    var isRequired = false
    var isReadOnly = false
    var isHidden = false
}

open class Text(text:String) : FormSheet() {
    init {
        this.field = text
    }
}

open class BasicCheckbox(fieldName:String, fieldID:String) : FormSheet() {
    var listener:CheckboxListener? = null
    init {
        this.field = "false"
        this.fieldName = fieldName
        this.fieldID = fieldID
        this.formLayout = R.layout.form_checkbox
    }
}

open class BasicInput(fieldName:String, fieldID:String) : FormSheet() {
    var singleLine = true
    init {
        this.fieldName = fieldName
        this.fieldID = fieldID
        this.formLayout = R.layout.form_input_basic
    }
}

open class IntentBasicInput(fieldName: String, fieldID: String) : FormSheet() {
    var intent: Intent? = null
    var activity: Activity? = null
    var intentListener: FormIntentListener? = null
    var obj = JSONObject()

    init {
        this.fieldName = fieldName
        this.fieldID = fieldID
        this.formLayout = R.layout.form_intent_input_basic
        this.activity = activity
    }

    fun getRequestCode(): Int {
        var value = 0
        fieldID.forEach { c ->
            value += c.toInt()
        }
        return value
    }

    fun getCustomField(): String {
        val l = intentListener
        if (l is FormIntentListener) {
            return l.getField()
        }
        return ""
    }
    fun prepareIntent(i:Intent):Intent {
        val l = intentListener
        if (l is FormIntentListener) {
            return l.prepareIntent(i)
        }
        return Intent()
    }
}

open class BasicDatePicker(fieldName: String, fieldID: String) : FormSheet() {

    var currentDate = Date()
    var formatView = "EEE, d MMM yyyy"
    var formatField = "yyyy-MM-dd"
    var activity:Activity? = null
    var listener:DatePickerBasicListener? = null
    var minDate:Long = 0
    var maxDate:Long = 0


    init {
        minDate = System.currentTimeMillis() - 1000
        this.fieldName = fieldName
        this.fieldID = fieldID
        this.formLayout = R.layout.form_intent_input_basic
    }
}

open class BasicSpinner(fieldName:String, fieldID:String, options:List<String>) : FormSheet() {
    var options:List<String> = ArrayList<String>()
    var selectedIndex = 0
    var selectedField = ""
    init {
        this.fieldName = fieldName
        this.fieldID = fieldID
        this.options = options
        this.formLayout = R.layout.form_basic_spinner
    }

}

open class APIBasicSpinner<T: Any>(fieldName: String, fieldID: String) :
        FormSheet() {

    var listener:APIBasicSpinnerEventListener<T>? = null
    var call:Call<BaseResponse<T>>? = null
    var objects = mutableListOf<T>()
    var selectedIndex = -0
    var selectedField = ""
    var firstFetch = true
    init {
        this.fieldName = fieldName
        this.fieldID = fieldID
        this.formLayout = R.layout.form_basic_spinner
    }

    fun fetchData(context: Context, spinner: Spinner) {
        if (firstFetch) {
            firstFetch = false
            val c = call
            val l = listener
            if (c is Call<BaseResponse<T>> && l is APIBasicSpinnerEventListener<T>) {
                c.enqueue(object : BaseResponseCallBack<T>(context) {
                    override fun responseData(data: MutableList<T>) {
                        objects.addAll(data)
                        val options = mutableListOf<String>()
                        var selected = -1
                        var i = 0
                        data.forEach { d ->
                            val item = l.getItem(d)
                            options.add(item)
                            if (item.equals(selectedField)) {
                                selected = i
                            }
                            i++
                        }
                        val adapter = SpinnerBasicAdapter(context,options)
                        spinner.adapter = adapter
                        l.fetchSuccess(objects)
                        if (selected == -1) {
                            spinner.setSelection(selectedIndex,false)
                        } else {
                            spinner.setSelection(selected,false)
                        }

                    }

                    override fun failed(error: Throwable) {

                    }
                })
            }
        } else {
            val l = listener
            if (l is APIBasicSpinnerEventListener<T>) {
                val options = mutableListOf<String>()
                objects.forEach { d -> options.add(l.getItem(d)) }
                val adapter = SpinnerBasicAdapter(context, options)
                spinner.adapter = adapter
            }
        }
    }


    fun getField(position:Int):String {
        val l = listener
        if (l is APIBasicSpinnerEventListener<*>) {
            if (position < objects.size)
                return l.getField(objects[position])
            else
                return ""
        } else {
            return ""
        }
    }

}

open class TableForm<T:Any>(fieldName: String, fieldID: String) :
        FormSheet() {
    var adapter:ArrayAdapter<T>? = null
    var intent:Intent? = null
    var activity:Activity? = null
    var listener:TableFormEventListener<T>? = null
    init {
        this.fieldName = fieldName
        this.fieldID = fieldID
        this.formLayout = R.layout.form_table
    }

    fun getRequestCode(): Int {
        var value = 0
        fieldID.forEach { c ->
            value += c.toInt()
        }
        return value
    }

    fun addToAdapter(i:Intent) {
        val l = listener
        val a = adapter
        if (l is TableFormEventListener<T> && a is ArrayAdapter<T>) {
            val item = l.getItem(i)
            a.add(item)
            a.notifyDataSetChanged()
            l.itemDidUpdate()
        }
    }

    fun refreshItems() {
        val a = adapter
        if (a is ArrayAdapter<T>) {
            a.notifyDataSetChanged()
        }
        val l = listener
        if (l is TableFormEventListener<*>)
            l.itemDidUpdate()
    }

    fun getIntented():Intent? {
        val l = listener
        val i = intent
        if (i is Intent && l is TableFormEventListener<*>) {
            l.prepareIntent(i)
            return i
        }
        return intent
    }
    fun getFields() {
        val objects = mutableListOf<T>()
        val a = adapter
        val l = listener
        if (a is ArrayAdapter<T>) {
            for (i in 0 until a.count) {
                objects.add(a.getItem(i))
            }
        }
        val json = Gson().toJson(objects)
        val jsonArray = Gson().fromJson<JsonArray>(json,JsonArray::class.java)
        if (l is TableFormEventListener<*>)
            l.getField(jsonArray)
    }

    fun tapItem(position: Int) {
        val a = adapter
        val l = listener
        if (a is ArrayAdapter<T> && l is TableFormEventListener<T>) {
            val item = a.getItem(position)
            l.onListTapped(item, position)
        }
    }



}

