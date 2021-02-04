package com.example.myapplication

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*


//Kali ini kelas fragment ini akan membantu untuk setup jam.
class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var mListener : DialogTimeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(context != null){
            mListener = context as DialogTimeListener?
        }
    }

    override fun onDetach() {
        super.onDetach()
        if(mListener != null){
            mListener = null
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calender = Calendar.getInstance()
        val hours = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)
        val formatHour24 = true
        return TimePickerDialog(activity,this,hours,minute,formatHour24)
    }

    //  TimePickerFragment diimplementasikan dengan metode TimePickerDialog.OnTimeSetListener.
    //  Variabel yang berbeda adalah jam dan menit.
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        mListener?.onDialogTimeSet(tag,hourOfDay,minute)
    }

    interface DialogTimeListener{
        fun onDialogTimeSet(tag:String?, hourOfDay: Int, minute: Int)
    }

}