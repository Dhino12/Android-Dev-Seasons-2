package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() , View.OnClickListener, DatePickerFragment.DialogDateListener ,
    TimePickerFragment.DialogTimeListener {

    private lateinit var alarmReceiver: AlarmReceiver

    companion object{
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //============ Memberikan Aksi kepada view ==============
        btn_once_date.setOnClickListener(this)
        btn_once_time.setOnClickListener(this)
        btn_set_once_alarm.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()
        //==========================================================
    }

    //============ Memberikan Aksi kepada view ==============
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_once_date -> {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }
            R.id.btn_once_time -> {
                val timePickerFragmentOne = TimePickerFragment()
                timePickerFragmentOne.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
            }
            R.id.btn_set_once_alarm -> {
                val onceDate = tv_once_date.text.toString()
                val onceTime = tv_once_time.text.toString()
                val onceMessage = edt_once_message.text.toString()

                alarmReceiver.setOneTimeAlarm(this,
                    AlarmReceiver.TYPE_ONE_TIME,onceDate,onceTime,onceMessage)
                //Kode di atas berfungsi untuk memanggil metode setOneTimeAlarm yang berada di dalam AlarmReceiver.
            }
        }
    }
    //==========================================================

    //===================================================================================
    //Selanjutnya kita coba kembali ke kelas MainActivity. Kode di bawah ini adalah hasil dari
    // implementasi dari DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener.
    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {

        // Siapkan date formatter-nya terlebih dahulu

        val calender = Calendar.getInstance()
        calender.set(year,month,dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // Set text dari textview once
        tv_once_date.text = dateFormat.format(calender.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay:Int ,minute:Int){

        // Siapkan date formatter-nya terlebih dahulu

        val calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY,hourOfDay)
        calender.set(Calendar.MINUTE,minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        // Set text dari textview berdasarkan tag
        when(tag){
            TIME_PICKER_ONCE_TAG -> tv_once_time.text = dateFormat.format(calender.time)
            else -> {}
        }
    }
//Kode di atas adalah hasil dari callback dari DatePickerFragment.DialogDateListener
// dan TimePickerFragment.DialogTimeListener yang kemudian akan ditampilkan hasilnya.

}