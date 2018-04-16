package bobzone.hrf.humanresource.Core.Base.Form

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.ThemedSpinnerAdapter
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Core.Model.CreationResponse
import bobzone.hrf.humanresource.R
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by davidwibisono on 2/13/18.
 */

open class FormBuilder {
    var formList:ListView? = null
    var formSheets:MutableList<FormSheet> = ArrayList<FormSheet>()
    var context:Context? = null
    var listener:FormEventListener? = null
    var editMode = false

    fun setup(context: Context,activity: Activity) {
        this.context = context
        formList = activity.findViewById(R.id.form_list)
    }

    fun onResult(resultCode:Int, requestCode:Int, intent:Intent?) {
        var index = 0
        formSheets.forEach { fs ->
            if (fs is IntentBasicInput) {
                val il = fs.intentListener
                val reqCode = fs.getRequestCode()
                val i = intent
                val fl = formList
                if (fl is ListView) {
                    val adapter = fl.adapter
                    if (adapter is FormAdapter) {
                        if (resultCode == Activity.RESULT_OK && il is FormIntentListener && requestCode == reqCode && i is Intent) {
                            adapter.setField(index,il.onIntentResult(i),fl)
                        }
                    }
                }
            }
            if (fs is TableForm<*>) {
                val il = fs.listener
                val reqCode = fs.getRequestCode()
                val i = intent
                if (i is Intent && resultCode == Activity.RESULT_OK && il is TableFormEventListener<*> && reqCode == requestCode) {
                    fs.addToAdapter(i)

                    val fl = formList
                    if (fl is ListView) {
                        val adapter = fl.adapter
                        if (adapter is FormAdapter) {
                            adapter.notifyDataSetChanged()
                        }
//                        Helper.instance.setAndGetListViewHeightBasedOnChildren(fl)
                    }
                }
            }
            index++
        }
    }

    fun build() {
        val c = context
        if (c is Context) {
            val formAdapter = FormAdapter(c,formSheets)
            val fl = formList
            if (fl is ListView) {
                fl.adapter = formAdapter
            }

            if (editMode) {
                populateData()
            }
        }

    }

    fun refreshItems() {
        val fl = formList
        if (fl is ListView) {
            val a = fl.adapter
            if (a is FormAdapter) {
                a.notifyDataSetChanged()
            }

        }
    }

    fun populateData() {
        val l = listener
        if (l is FormEventListener) {
            val cs = l.getCallFetch()
            if (cs is Call<JsonObject>) {
                cs.enqueue(object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable?) {
                        l.requestDidFailed(t)
                    }

                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response == null) {
                            l.requestDidNullResponse()
                        } else {
                            val JsonObject = response.body()
                            if (JsonObject != null) {
                                l.fetchDidSuccess(JsonObject)
                                refresh()
                            }
                            else
                                l.requestDidResponseError(response.code())
                        }
                    }

                })
            }
        }
    }

    fun refresh() {
        val fl = formList
        if (fl is ListView) {
            val adapter = fl.adapter
            if (adapter is FormAdapter) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun setText(indexForm:Int, text:String) {
        val fl = formList
        if (fl is ListView) {
            val adapter = fl.adapter
            if (adapter is FormAdapter) {
                adapter.setField(indexForm,text,fl)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private var fieldTemps = mutableListOf<String>()
    fun validate():Boolean {
        val l = listener
        fieldTemps.clear()
        val fl = formList
        var i = 0
        if (fl is ListView) {
            formSheets.forEach { f ->
                val adapter = fl.adapter
                if (adapter is FormAdapter) {
                    fieldTemps.add(adapter.getField(i,fl))
                    if (f.isRequired) {
                        if (f is TableForm<*>) {
                            val tfAdapter = f.adapter
                            if (tfAdapter is ArrayAdapter<*>) {
                                if (tfAdapter.count == 0)
                                    return false
                            }
                        } else if (adapter.getField(i,fl).equals("")) {
                            adapter.notifyDataSetChanged()
                            Toast.makeText(context,R.string.warning_field_is_required,Toast.LENGTH_SHORT).show()
                            return false
                        }
                    }
                }

                i++
            }
        }
        if (l is FormEventListener) {
            if (!l.validating()) return false
        }
        return true
    }

    fun submit(force: Boolean) {
        var isSubmit = false
        if (force) {
            isSubmit = true
        } else {
            if (validate()) {
                isSubmit = true
            }
        }
        if (isSubmit) {
            val l = listener
            if (l is FormEventListener) {
                val jsonObject = JsonObject()

                for ((i, v) in fieldTemps.withIndex()) {
                    jsonObject.addProperty(formSheets[i].fieldID, fieldTemps[i])
                }
                if (editMode) {
                    val cs = l.getCallUpdate(jsonObject)
                    if (cs is Call<CreationResponse>) {
                        cs.enqueue(object : Callback<CreationResponse> {
                            override fun onFailure(call: Call<CreationResponse>, t: Throwable?) {
                                l.requestDidFailed(t)
                            }

                            override fun onResponse(call: Call<CreationResponse>, response: Response<CreationResponse>) {
                                if (response == null) {
                                    l.requestDidNullResponse()
                                } else {
                                    val creationResponse = response.body()
                                    if (creationResponse != null)
                                        l.submitDidSuccess(creationResponse)
                                    else
                                        l.requestDidResponseError(response.code())
                                }
                            }

                        })
                    }
                } else {
                    val cs = l.getCallSubmit(jsonObject)
                    if (cs is Call<CreationResponse>) {
                        cs.enqueue(object : Callback<CreationResponse> {
                            override fun onFailure(call: Call<CreationResponse>, t: Throwable?) {
                                l.requestDidFailed(t)
                            }

                            override fun onResponse(call: Call<CreationResponse>, response: Response<CreationResponse>) {

                                if (response == null) {
                                    l.requestDidNullResponse()
                                } else {
                                    val creationResponse = response.body()
                                    if (creationResponse != null)
                                        l.submitDidSuccess(creationResponse)
                                    else
                                        l.requestDidResponseError(response.code())
                                }
                            }

                        })
                    }
                }
            }
        }
    }

    fun disabled() {
        val fl = formList
        if (fl is ListView) {
            for (i in 0 until fl.childCount) {
                val v = fl.getChildAt(i)
                v.isEnabled = false
            }
        }
    }

    fun enabled() {
        val fl = formList
        if (fl is ListView) {
            for (i in 0 until fl.childCount) {
                val v = fl.getChildAt(i)
                v.isEnabled = true
            }
        }
    }



}