package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_form_user_preference.*

class FormUserPreferenceActivity : AppCompatActivity() ,View.OnClickListener{

    companion object{
        const val EXTRA_TYPE_FORM = "extra_type_form"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101

        const val TYPE_ADD = 1
        const val TYPE_EDIT = 2

        private const val FIELD_REQUIRED = "Field Tidak Boleh Kosong"
        private const val FIELD_DIGIT_ONLY = "Hanya Boleh terisi data numerik"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
    }
    //Ketika Anda membuat obyek dari kelas UserPreference pada Activity berikutnya, maka obyek
    // Shared Preferences akan diciptakan dan hanya diciptakan sekali. Jika sudah ada, obyek
    // yang sudah ada yang akan dikembalikan. Semua itu Anda lakukan di konstruktor kelas UserPreference.

    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_user_preference)

        btn_save.setOnClickListener(this)

        //=========================================================
        //Menagmabil data dari intent
        //=========================================================
        userModel = intent.getParcelableExtra<UserModel>("USER") as UserModel
        val formType = intent.getIntExtra(EXTRA_TYPE_FORM,0)

        var actionBarTitle = ""
        var btnTitle = ""
        when(formType){
            TYPE_ADD -> {
                actionBarTitle = "TAMBAH BARU"
                btnTitle = "Simpan"
            }
            TYPE_EDIT -> {
                actionBarTitle = "UBAH"
                btnTitle = "Update"
                showPreferenceInForm()
            }
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_save.text = btnTitle

    }

    override fun onClick(view: View?) {
        if(view?.id == R.id.btn_save){
            val name = edt_name.text.toString().trim()
            val email = edt_email.text.toString().trim()
            val age = edt_age.text.toString().trim()
            val phoneNo =  edt_phone.text.toString().trim()
            val isLoveMu = rg_love_mu.checkedRadioButtonId == R.id.rb_yes

            if(name.isEmpty()){
                edt_name.error = FIELD_REQUIRED
                return
            }
            if (email.isEmpty()){
                edt_email.error = FIELD_REQUIRED
                return
            }
            if (!isValidEmail(email)){
                //husus untuk validasi email,
                // Anda perlu menggunakan regular expression untuk memeriksa apakah nilai email sudah valid atau belum.
                // Bentuk email yang valid yaitu xxx@xxx.xxx.
                edt_email.error = FIELD_IS_NOT_VALID
                return
            }

            if (age.isEmpty()){
                edt_age.error = FIELD_REQUIRED
                return
            }
            if (phoneNo.isEmpty()){
                edt_phone.error = FIELD_REQUIRED
                return
            }
            if ((!TextUtils.isDigitsOnly(phoneNo))){
                edt_phone.error = FIELD_DIGIT_ONLY
                return
            }

            saveUser(name,email,age,phoneNo,isLoveMu)

            //===========================================================================
            //Pada FormUserPreference kita memberikan result (hasil) dengan cara berikut:
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_RESULT,userModel)
            setResult(RESULT_CODE,resultIntent)
            //Kemudian diterima pada onActivityResult, dengan menyamakan resultCode-nya.
            //===========================================================================

            finish()
        }
    }

    private fun saveUser(name:String, email:String, age:String, phoneNo:String,isLoveMu:Boolean){

        val userPrefernce = UserPreference(this)

        userModel.name = name
        userModel.email = email
        userModel.age = Integer.parseInt(age)
        userModel.phoneNumber = phoneNo
        userModel.isLove = isLoveMu

        userPrefernce.setUser(userModel)
        Toast.makeText(this, "Data Tersimpan",Toast.LENGTH_SHORT).show()
    }

    //=============================================================================================
    //husus untuk validasi email,
    // Anda perlu menggunakan regular expression untuk memeriksa apakah nilai email sudah valid atau belum.
    // Bentuk email yang valid yaitu xxx@xxx.xxx.
    private fun isValidEmail(email:CharSequence):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    //=============================================================================================

    private fun showPreferenceInForm() {
        // untuk mengubah data hasil dari intent ke View-nya.
        edt_name.setText(userModel.name)
        edt_email.setText(userModel.email)
        edt_age.setText(userModel.age.toString())
        edt_phone.setText(userModel.phoneNumber)
        if(userModel.isLove){
            rb_yes.isChecked = true
        }else{
            rb_no.isChecked = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}