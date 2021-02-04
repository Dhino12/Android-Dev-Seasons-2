package com.example.myapplication

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.myapplication.HomeFragment

class SectionsPagerAdapter(private val mContext:Context,fm:FragmentManager):FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_text_1,R.string.tab_text_2,R.string.tab_text_3)

    override fun getItem(position: Int): Fragment {
        //Mengirim data ke fragement
        var fragment = HomeFragment.newInstance(position + 1)
        return fragment
    }

    @Nullable
    //Memberi judul masing masing tab
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    //menentukan jumlh tab yang ingin ditampilkan
    override fun getCount(): Int {
        return 3
    }
}