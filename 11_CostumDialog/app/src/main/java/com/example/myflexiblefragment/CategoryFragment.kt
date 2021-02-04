package com.example.myflexiblefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class CategoryFragment : Fragment() ,View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnDetailCategory:Button = view.findViewById(R.id.btn_detail)
        btnDetailCategory.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        //Mengirim data ke DetailCategoryFragment ====================================
        if(v.id == R.id.btn_detail){
            val mDetailCategoryFragment = DetailCategoryFragment()
            //Mengirim data antar fragment dengan Bundle()
                //Mengirim data name =====================================================
            val mBundle = Bundle()
            mBundle.putString(DetailCategoryFragment.EXTRA_NAME,"Latina")
            mDetailCategoryFragment.arguments = mBundle
                //Mengirim data deskripsi =================================================
            val deskripsi = "Adalah karakter dari salah satu anime yang Kawaiii!!.."
            mDetailCategoryFragment.description = deskripsi
            val mFragmentManager = fragmentManager
            //Mengganti Fragment dari CategoryFragment menjadi DetailCategoryFragment
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container,mDetailCategoryFragment, DetailCategoryFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }
}