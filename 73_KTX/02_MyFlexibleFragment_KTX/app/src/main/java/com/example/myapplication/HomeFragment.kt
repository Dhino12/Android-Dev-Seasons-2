package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.commit

class HomeFragment : Fragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //Mendefinisakn dan transformasi file layout brupa xml kdalam view dengan inflate()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Merupakan object dari LayoutInflater untuk mengubah layout xml kdlm bntk objk
            //viewgroup / widget dngn inflate()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    //Inisialisasikan view dan juga action(setOnClickListener(this))
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Catatan:
        //Perlu diperhatikan untuk pemanggilan findViewById()
            // di sini tidak dapat langsung dilakukan seperti di Activity.
            // Anda perlu menambahkan variabel view terlebih
            // dulu di depannya sehingga menjadi view.findViewById().
        val btnCategory:Button = view.findViewById(R.id.btn_category)
        btnCategory.setOnClickListener(this)
    }
    override fun onClick(v: View) {
        if(v.id == R.id.btn_category){
            val mCategoryFragment = CategoryFragment()

            //=-========================= Menggunakan KTX ==========================================
            val mFragmentManager = parentFragmentManager
            mFragmentManager.commit {

                //Mengganti HomeFragement dengan CategoryFragment
                replace(R.id.frame_container, mCategoryFragment, CategoryFragment::class.java.simpleName)

                //Kembali ke Fragment sebelumnya yakni HomeFragment
                addToBackStack(null)
            }
            //======================================================================================
        }
    }
}