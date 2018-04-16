package bobzone.hrf.humanresource.Core.Base.Form

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.R
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by davidwibisono on 2/14/18.
 */

open class FormAdapter(context: Context, list: List<FormSheet>) :
        ArrayAdapter<FormSheet>(context, R.layout.form_input_basic, list), FormAdapterListener {
    var calendar = Calendar.getInstance()
    var list:List<FormSheet> = ArrayList<FormSheet>()

    init {
        this.list = list
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val current = list[position]
        var view = inflater.inflate(current.formLayout, parent, false)

        if (current.isHidden) {
            view = inflater.inflate(R.layout.form_empty, parent, false)
            return view
        }
        val warning = view.findViewById<TextView>(R.id.warning_required)
        if (warning is TextView) {
            if (current.isRequired && current.field.equals("") && !current.isReadOnly) {
                warning.visibility = View.VISIBLE
            } else {
                warning.visibility = View.GONE
            }
        }

        if (current.formLayout == R.layout.form_checkbox && current is BasicCheckbox) {
            val field = view.findViewById<CheckBox>(R.id.field)
            field.setText(current.fieldName)
            if (current.isReadOnly) {
                field.isEnabled = false
            }
            if (current.field.equals("true")) field.isChecked = true
            else field.isChecked = false

            field.setOnCheckedChangeListener { compoundButton, b ->
                if (b) current.field = "true"
                else current.field = "false"
                val l = current.listener
                if (l is CheckboxListener) {
                    l.onCheckChange(b)
                }
            }
        }
        if (current.formLayout == R.layout.form_text && current is Text) {
            val field = view.findViewById<TextView>(R.id.field)
            field.setText(current.field)
        }
        if (current.formLayout == R.layout.form_input_basic && current is BasicInput) {
            val fieldName = view.findViewById<TextView>(R.id.field_name)
            val field = view.findViewById<EditText>(R.id.field)
            fieldName.setText(current.fieldName)
            field.setText(current.field)
            field.setHint(current.hint)
            field.setSingleLine(current.singleLine)
            if (current.isReadOnly) {
                field.setOnClickListener(null)
                field.keyListener = null
            } else {
                field.setOnFocusChangeListener(object:View.OnFocusChangeListener {
                    override fun onFocusChange(p0: View?, p1: Boolean) {
                        if (p1) {
                            field.setSelection(field.text.length)
                        }
                    }

                })
                field.addTextChangedListener(object:TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        Log.e("asd","asds")
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        current.field = p0.toString()
                    }

                })
            }

        } else if (current.formLayout == R.layout.form_intent_input_basic && current is IntentBasicInput) {
            val fieldName = view.findViewById<TextView>(R.id.field_name)
            val field = view.findViewById<EditText>(R.id.field)
            fieldName.setText(current.fieldName)
            field.setHint(current.hint)
            field.setText(current.field)
            if (current.isReadOnly) {
                field.setOnClickListener(null)
                field.keyListener = null
            } else {
                field.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {

                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        current.field = p0.toString()
                    }

                })

                val intent = current.intent
                val activity = current.activity
                if (intent is Intent && activity is Activity) {

                    field.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(p0: View?) {
                            val requestCode = current.getRequestCode()
                            current.prepareIntent(intent)
                            activity.startActivityForResult(intent, requestCode)
                        }

                    })
                }
            }
        } else if (current.formLayout == R.layout.form_intent_input_basic && current is BasicDatePicker) {
            val fieldName = view.findViewById<TextView>(R.id.field_name)
            val field = view.findViewById<EditText>(R.id.field)
            fieldName.setText(current.fieldName)
            field.setHint(current.hint)
            var fieldDate = Helper.instance.formatDateFromstring(current.formatField,current.formatView,current.field)
            field.setText(fieldDate)

            if (current.isReadOnly) {
                field.setOnClickListener(null)
                field.keyListener = null
            } else {
                field.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(p0: View?) {
                        Log.e("tes", "as")
                        val a = current.activity
                        if (a is Activity) {
                            val datePickerDialog = DatePickerDialog(a, DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                                calendar.set(Calendar.YEAR, i)
                                calendar.set(Calendar.MONTH, i1)
                                calendar.set(Calendar.DAY_OF_MONTH, i2)

                                val sdf = SimpleDateFormat(current.formatField, Locale.US)
                                current.field = sdf.format(calendar.time)


                                val sdv = SimpleDateFormat(current.formatView, Locale.US)
                                field.setText(sdv.format(calendar.time))
                                current.currentDate = calendar.time

                                val l = current.listener
                                if (l is DatePickerBasicListener) {
                                    l.onChange(datePicker,i,i1,i2)
                                }
                            }, calendar
                                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH))
                            datePickerDialog.datePicker.minDate = current.minDate
                            val defaultMaxDate:Long = 0
                            if (current.maxDate != defaultMaxDate)
                                datePickerDialog.datePicker.maxDate = current.maxDate
                            datePickerDialog.show()
                        }
                    }

                })
            }
        } else if (current.formLayout == R.layout.form_basic_spinner && current is BasicSpinner) {
            val fieldName = view.findViewById<TextView>(R.id.field_name)
            val spinner = view.findViewById<Spinner>(R.id.spinner)
            fieldName.setText(current.fieldName)
            spinner.isEnabled = !current.isReadOnly

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    current.selectedIndex = spinner.selectedItemPosition
                    current.field = spinner.getItemAtPosition(current.selectedIndex).toString()
                }

            }
            val adapter = SpinnerBasicAdapter(context, current.options)
            spinner.adapter = adapter
            var selected = -1
            var i = 0
            current.options.forEach { o ->
                if (o.equals(current.selectedField)) {
                    selected = i
                }
                i++
            }
            if (selected != -1) {
                spinner.setSelection(selected, false)
            } else {
                spinner.setSelection(current.selectedIndex, false)
            }
            spinner.setSelection(current.selectedIndex)

        } else if (current.formLayout == R.layout.form_basic_spinner && current is APIBasicSpinner<*>) {
            val fieldName = view.findViewById<TextView>(R.id.field_name)
            val spinner = view.findViewById<Spinner>(R.id.spinner)
            fieldName.setText(current.fieldName)
            current.fetchData(context, spinner)
            spinner.isEnabled = !current.isReadOnly

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Log.e("e", "empty")
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    current.selectedIndex = spinner.selectedItemPosition
                    current.field = spinner.getItemAtPosition(current.selectedIndex).toString()
                }


            }

        } else if (current.formLayout == R.layout.form_table && current is TableForm<*>) {
            val fieldName = view.findViewById<TextView>(R.id.field_name)
            fieldName.setText(current.fieldName)
            val addButton = view.findViewById<LinearLayout>(R.id.add_button)
            val i = current.getIntented()
            if (i is Intent) {
                addButton.visibility = View.VISIBLE
                addButton.setOnClickListener(object:View.OnClickListener {
                    override fun onClick(p0: View?) {
                        val l = current.listener
                        if (l is TableFormEventListener<*>) {
                            if (l.shouldAddItem()) {
                                var a = current.activity
                                val i = current.getIntented()
                                if (a is Activity && i is Intent) {
                                    val requetCode = current.getRequestCode()
                                    a.startActivityForResult(i, requetCode)
                                }
                            }
                        }
                    }
                })
            } else {
                addButton.visibility = View.GONE
            }

            val tableForm = view.findViewById<ListView>(R.id.table_form)
            val adapter = current.adapter
            if (tableForm is ListView && adapter is ArrayAdapter<*>) {
                tableForm.adapter = adapter
                tableForm.setOnItemClickListener(object:AdapterView.OnItemClickListener {
                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        current.tapItem(p2)
                    }

                })
//                Helper.instance.setAndGetListViewHeightBasedOnChildren(tableForm)
            }
        }
        return view
    }

    fun getViewByPosition(pos: Int, listView: ListView): View {
        val firstListItemPosition = listView.getFirstVisiblePosition()
        val lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.adapter.getView(pos, null, listView)
        } else {
            val childIndex = pos - firstListItemPosition
            return listView.getChildAt(childIndex)
        }
    }

    override fun getField(position: Int, lv:ListView): String {
        val fs = list[position]
        if (fs is BasicCheckbox) {
            if (fs.field.equals("true"))
                return "1"
            else
                return "0"
        }
        else if (fs is BasicInput) {
            return fs.field
        } else if (fs is BasicSpinner) {
            return fs.options[fs.selectedIndex]
        } else if (fs is APIBasicSpinner<*>) {
            return fs.getField(fs.selectedIndex)
        } else if (fs is IntentBasicInput) {
            return fs.getCustomField()
        } else if (fs is BasicDatePicker) {
            return fs.field
        } else if (fs is TableForm<*>) {
            fs.getFields()
        }
        return ""
    }

    override fun setField(position: Int, text: String, lv:ListView) {
        val v = getViewByPosition(position,lv)
        val current = list[position]
        current.field = text
        if (v is LinearLayout) {
            val et = v.findViewById<EditText>(R.id.field)
            if (et is EditText) {
                et.setText(text)
            }
        }
    }





}