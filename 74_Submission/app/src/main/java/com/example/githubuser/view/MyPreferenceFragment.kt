package com.example.githubuser.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.githubuser.R
import com.example.githubuser.alarm_manager.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*

class MyPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener ,
    TimePickerFragment.DialogTimeListener {
    private lateinit var Remainder:String
    private lateinit var Lang:String
    private lateinit var alarmReceiver:AlarmReceiver
    private lateinit var switchPre:SwitchPreference
    private lateinit var TAG:Context

    companion object{

        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        alarmReceiver = AlarmReceiver()
        init()
        setSummeries()
    }

    private fun init(){
        Remainder = resources.getString(R.string.key_Switch)
        switchPre = findPreference<SwitchPreference>(Remainder) as SwitchPreference
    }

    private fun setSummeries(){
        val sh = preferenceManager.sharedPreferences
        switchPre.isChecked = sh.getBoolean(Remainder,false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if(key == Remainder){
            switchPre.isChecked = sharedPreferences.getBoolean(Remainder,false)

            if(switchPre.isChecked){
                Toast.makeText(activity,"True",Toast.LENGTH_SHORT).show()
                val repeatTime = "09:00"
                val repeatingMessage = "Test"
                alarmReceiver.setRepeatingAlarm(TAG, AlarmReceiver.TYPE_REPEATING, repeatTime, repeatingMessage)

            }else{
                Toast.makeText(activity,"False",Toast.LENGTH_SHORT).show()
                alarmReceiver.cancelAlarm(TAG, AlarmReceiver.TYPE_REPEATING)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        TAG = context
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat =SimpleDateFormat("HH:mm",Locale.getDefault())
        Toast.makeText(TAG,"masuk onDialogTimeSet ",Toast.LENGTH_SHORT).show()

        when(tag){
            TIME_PICKER_REPEAT_TAG -> dateFormat.format(calendar.time)
            else -> {}
        }
    }
}