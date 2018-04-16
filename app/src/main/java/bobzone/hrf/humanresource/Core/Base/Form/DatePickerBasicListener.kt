package bobzone.hrf.humanresource.Core.Base.Form

import android.widget.DatePicker

/**
 * Created by davidwibisono on 4/16/18.
 */
open interface DatePickerBasicListener {
    fun onChange(datePicker:DatePicker, i:Int,i1:Int,i2:Int)
}