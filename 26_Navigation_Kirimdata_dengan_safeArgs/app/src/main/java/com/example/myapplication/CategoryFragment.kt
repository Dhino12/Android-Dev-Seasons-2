package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_category.*


class CategoryFragment : Fragment() {

    companion object{
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_STOCK = "extra_stock"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_category_lifestyle.setOnClickListener{
            direction ->
            // val mBundle = Bundle()
            //Mengirim data ke DetailCategoryFragment
            // mBundle.putString(EXTRA_NAME,"Life_Style")
            // mBundle.putLong(EXTRA_STOCK,7)
            //Pindah Fragment / Activity dengan NavController ,dengan data yang disiapkan
            // direction.findNavController().navigate(R.id.action_categoryFragment_to_detailCategoryFragment,mBundle)

            // SafeArgs ==================
            // jangan lupa rebuild Project
            val toDetailCategoryFragment = CategoryFragmentDirections.actionCategoryFragmentToDetailCategoryFragment()
            toDetailCategoryFragment.name = "Lifestyle"
            toDetailCategoryFragment.stock = 7
            direction.findNavController().navigate(toDetailCategoryFragment)
        }
    }

}