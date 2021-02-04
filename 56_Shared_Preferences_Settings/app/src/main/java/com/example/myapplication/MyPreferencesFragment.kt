package com.example.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat

class MyPreferencesFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var NAME: String
    private lateinit var EMAIL: String
    private lateinit var AGE: String
    private lateinit var PHONE: String
    private lateinit var LOVE: String

    private lateinit var namePreferences: EditTextPreference
    private lateinit var emailPreferences: EditTextPreference
    private lateinit var agePreferences: EditTextPreference
    private lateinit var phonePreferences: EditTextPreference
    private lateinit var isLoveMuPreferences: CheckBoxPreference

    companion object{
        private const val DEFAULT_VALUE = "tidak ada"
    }

    override fun onCreatePreferences(bundle:Bundle?,s:String?){
        addPreferencesFromResource(R.xml.preference)

        //Kemudian buat metode init() untuk inisiasi dan panggil metode tersebut di onCreatePreferences.
        init()
        setSummeries()
    }

    //========================================================================
    //Metode init() digunakan untuk memanggil komponen seperti EditTextPreference dan CheckBoxPreference
    // berdasarkan key yang sudah ditetapkan di preferences.xml.
    private fun init() {
        NAME = resources.getString(R.string.key_name)
        EMAIL = resources.getString(R.string.key_email)
        AGE = resources.getString(R.string.key_age)
        PHONE = resources.getString(R.string.key_phone)
        LOVE = resources.getString(R.string.key_love)

        namePreferences = findPreference<EditTextPreference>(NAME) as EditTextPreference
        emailPreferences = findPreference<EditTextPreference>(EMAIL) as EditTextPreference
        agePreferences = findPreference<EditTextPreference>(AGE) as EditTextPreference
        phonePreferences = findPreference<EditTextPreference>(PHONE) as EditTextPreference
        isLoveMuPreferences = findPreference<CheckBoxPreference>(LOVE) as CheckBoxPreference
    }
    //========================================================================

    //========================================================================
    //Perintah setSummary berguna untuk mengubah value dari EditTextPreference
    // menjadi nilai yang baru sesuai dengan data yang tersimpan.
    private fun setSummeries(){
        val sh = preferenceManager.sharedPreferences
        namePreferences.summary = sh.getString(NAME, DEFAULT_VALUE)
        emailPreferences.summary = sh.getString(EMAIL, DEFAULT_VALUE)
        agePreferences.summary = sh.getString(AGE, DEFAULT_VALUE)
        phonePreferences.summary = sh.getString(PHONE, DEFAULT_VALUE)
        isLoveMuPreferences.isChecked = sh.getBoolean(LOVE, false)
    }
    //==========================================================================================

    //==========================================================================================
    //Selanjutnya kita wajib register dan unregister listener preference-nya di onResume dan onPause.
    // Silakan override kedua metode tersebut dan sesuaikan kode seperti ini:
    override fun onResume() {
        super.onResume()
        //Kemudian ketika terjadi perubahan pada bagian onResume dan onPause panggilah kode berikut:
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        ////Kemudian ketika terjadi perubahan pada bagian onResume dan onPause panggilah kode berikut:
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
    // Kode di atas digunakan untuk me-register ketika aplikasi dibuka dan me-unregister ketika aplikasi ditutup.
    // Hal ini supaya listener tidak berjalan terus menerus dan menyebabkan memory leak.
    //==========================================================================================

    //==========================================================================================
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == NAME){
            namePreferences.summary = sharedPreferences.getString(NAME, DEFAULT_VALUE)
        }
        if(key == EMAIL){
            emailPreferences.summary = sharedPreferences.getString(EMAIL, DEFAULT_VALUE)
        }
        if(key == AGE){
            agePreferences.summary = sharedPreferences.getString(AGE, DEFAULT_VALUE)
        }
        if(key == PHONE){
            phonePreferences.summary = sharedPreferences.getString(PHONE, DEFAULT_VALUE)
        }
        if (key == LOVE){
            isLoveMuPreferences.isChecked = sharedPreferences.getBoolean(LOVE,false)
        }
    }
    //Kode di atas digunakan untuk mengecek apakah terjadi perubahan pada data yang tersimpan.
    // Jika terdapat value yang berubah maka akan memanggil listener onSharedPreferenceChanged. Untuk mendapatkan value tersebut,
    // lakukan validasi terlebih dahulu. Dengan mencocokkan key mana yang berubah, hasilnya juga akan berubah.
    //==========================================================================================

}