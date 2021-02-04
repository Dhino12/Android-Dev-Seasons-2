package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    // untuk menampilkan data dari SharedPreference yang sudah ada.
    private lateinit var mUserPreference: UserPreference
    private var isPreferenceEmpty = false
    private lateinit var userModel: UserModel

    companion object{
        private const val REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "My User Preference"

        mUserPreference = UserPreference(this)

        showExistingPreference()

        btn_save_main.setOnClickListener(this)
    }

    /*
    Menampilkan preference yang ada
     */
    private fun showExistingPreference(){
        //===================================================================================
        //Untuk menampilkan data pada MainActivity,
        userModel = mUserPreference.getUser()
        //Variabel userModel di atas kita inisiasikan dengan metode getUser yang ada di kelas UserPreference.
        // Setelah itu akan kita akan mengatur TextView dengan memanggil variabel userModel yang tadi sudah diinisiasikan.
        // Terakhir kita memanggil metode populateView dengan memasukan variabel userModel sebagai argumen.
        //===================================================================================

        populateView(userModel)
        checkForm(userModel)
    }

    /*
    Set tampilan menggunakan preferences
    */
    private fun populateView(userModel: UserModel){
        tv_name.text = if(userModel.name.toString().isEmpty()) "Tidak Ada" else userModel.name
        tv_age.text = if(userModel.age.toString().isEmpty()) "Tidak Ada" else userModel.age.toString()
        tv_is_love_mu.text = if(userModel.isLove) "Ya" else "Tidak"
        tv_email.text = if(userModel.email.toString().isEmpty()) "Tidak Ada" else userModel.email
        tv_phone.text = if (userModel.phoneNumber.toString().isEmpty()) "Tidak Ada" else userModel.phoneNumber

        // Pada bagian di atas, secara otomatis akan memanggil default value jika data kosong.
    }

    private fun checkForm(userModel: UserModel){
        when{
            userModel.name.toString().isNotEmpty() -> {
                btn_save_main.text = getString(R.string.change)

                // Fungsi dari kode di bawah yaitu untuk menentukan apakah sudah ada data yang tersimpan pada SharedPreference atau tidak,
                // kemudian memasukkannya ke variabel isPreferenceEmpty
                isPreferenceEmpty = false
            }
            else -> {
                btn_save_main.text = getString(R.string.save)

                // Fungsi dari kode di bawah yaitu untuk menentukan apakah sudah ada data yang tersimpan pada SharedPreference atau tidak,
                // kemudian memasukkannya ke variabel isPreferenceEmpty
                isPreferenceEmpty = true
            }
        }
    }

    override fun onClick(view: View?) {
        if(view?.id == R.id.btn_save_main){
            val intent = Intent(this@MainActivity, FormUserPreferenceActivity::class.java)

            // isPreferenceEmpty Variabel ini dipakai ketika tombol Save diklik pada fungsi onClick berikut:
            when{
                isPreferenceEmpty -> {
                    intent.putExtra(
                        FormUserPreferenceActivity.EXTRA_TYPE_FORM,
                        FormUserPreferenceActivity.TYPE_ADD)
                    intent.putExtra("USER",userModel)
                }
                else -> {
                    intent.putExtra(
                        FormUserPreferenceActivity.EXTRA_TYPE_FORM,
                        FormUserPreferenceActivity.TYPE_EDIT)
                    intent.putExtra("USER",userModel)
                }
            }

            //==========================================================
            // Di sini kita lihat, untuk memulai activity kita tidak menggunakan startActivity, melainkan startActivityForResult.
            // Apa bedanya? Bedanya yaitu startActivityForResult tidak hanya berpindah activity,
            // namun juga mendapatkan result (hasil) dari activity tersebut.
            startActivityForResult(intent, REQUEST_CODE)
            //REQUEST_CODE di sini dipakai ketika mendapatkan hasil pada onActivityResult.
            //==========================================================
        }
    }

    /*
    Akan dipanggil ketika formuserpreferenceactivity ditutup
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //==============================================================================
        ////REQUEST_CODE di sini dipakai ketika mendapatkan hasil pada onActivityResult.
        if(requestCode == REQUEST_CODE){
            if(resultCode == FormUserPreferenceActivity.RESULT_CODE){
                userModel = data?.getParcelableExtra<UserModel>(FormUserPreferenceActivity.EXTRA_RESULT) as UserModel
                populateView(userModel)
                checkForm(userModel)
            }
        }
        //==============================================================================

    }
}