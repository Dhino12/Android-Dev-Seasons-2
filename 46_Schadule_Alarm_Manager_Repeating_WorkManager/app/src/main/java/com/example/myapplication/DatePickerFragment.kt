package com.aplikasi.myalarmmanager

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

//kita tambahkan kelas untuk membantu kita mengambil waktu.
//Fungsi onDateSet akan dipanggil ketika kita memilih tanggal yang kita inginkan. Kemudian setelah tanggal dipilih maka variable tanggal,
// bulan dan tahun akan dikirim ke MainActivity menggunakan bantuan interface DialogDateListener.
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var mListener: DialogDateListener? = null

    //==============================================================================================
    //Fungsi onAttach() hanya sekali dipanggil dalam fragment dan berfungsi untuk mengkaitkan dengan activity pemanggil,
    // sedangkan onDetach() hanya dipanggil sebelum fragmen tidak lagi dikaitkan dengan activity pemanggil.
    override fun onAttach(context: Context){
        super.onAttach(context)
        if (context != null){
            mListener = context as DialogDateListener?
        }
    }
    override fun onDetach() {
        super.onDetach()
        if (mListener != null){
            mListener = null
        }
    }
    //==============================================================================================

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calender = Calendar.getInstance()
        val years = calender.get(Calendar.YEAR)
        val mount = calender.get(Calendar.MONTH)
        val date = calender.get(Calendar.DATE)

        return DatePickerDialog(activity as Context, this, years, mount, date)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mListener?.onDialogDateSet(tag,year,month,dayOfMonth)
    }
//ini malalui ini dikirm ke MainActivity
    interface DialogDateListener{
        fun onDialogDateSet(tag:String?, year: Int, month:Int, dayOfMonth:Int)
    }
}