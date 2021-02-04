package com.example.myflexiblefragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_option_dialog.*

//Agar object fragment sekarang menjadi dialog fragment yang akan mengambang di tampilan
class OptionDialogFragment : DialogFragment(), View.OnClickListener {
    private lateinit var btnChoose: Button
    private lateinit var btnClose:  Button
    private lateinit var rgOption: RadioGroup
    private lateinit var rbFerguson: RadioButton
    private lateinit var rbMourinho: RadioButton
    private lateinit var rbMoyes: RadioButton
    private lateinit var rbVangaal: RadioButton
    private var optionDialogListener: OnOptionDialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_option_dialog, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnChoose = view.findViewById(R.id.btn_choose)
        btnClose = view.findViewById(R.id.btn_close)
        rgOption = view.findViewById(R.id.rg_group)
        rbFerguson = view.findViewById(R.id.rb_ferguson)
        rbMourinho= view.findViewById(R.id.rb_mourinho)
        rbMoyes= view.findViewById(R.id.rb_moyes)
        rbVangaal= view.findViewById(R.id.rb_van_gaal)

        btnClose.setOnClickListener(this)
        btnChoose.setOnClickListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = parentFragment
        if(fragment is DetailCategoryFragment){
            val detailCategoryFragment = fragment
            this.optionDialogListener = detailCategoryFragment.optionDialogListener
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.optionDialogListener = null
    }
    override fun onClick(v: View) {
        when(v.id){
            //Ketika tombol Cancle diklik maka akan masuk kestate ini dgn menghapus dialog
            R.id.btn_close -> dialog?.cancel()
            R.id.btn_choose -> {
                val ceklistRadioBtn = rg_group.checkedRadioButtonId
                if(ceklistRadioBtn != -1){
                    var coach: String? = when(ceklistRadioBtn){
                        R.id.rb_ferguson -> rbFerguson.text.toString().trim()
                        R.id.rb_van_gaal -> rbVangaal.text.toString().trim()
                        R.id.rb_mourinho -> rbMourinho.text.toString().trim()
                        R.id.rb_moyes -> rbMoyes.text.toString().trim()
                        else -> "Pilih salah satu"
                    }
                    if(optionDialogListener != null){
                        //ketika tombol pilih diklik maka event ini akan muncul
                        //disini panggil interface yang tlh dibuat
                            //dan akan diimplementasikan pada DetailCategoryFragment sebagai Toast
                        optionDialogListener?.onOptionChosen(coach)
                    }
                    dialog?.dismiss()
                }
            }
        }
    }
    //Interface adalah sebuah kelas yang terdiri kumpulan method yang
        // hanya memuat deklarasi dan struktur method, tanpa detail implementasinya.
    interface OnOptionDialogListener{
        fun onOptionChosen(text:String?){

        }
    }

}