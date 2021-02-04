package com.aplikasi.myapplication.model

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aplikasi.myapplication.R
import com.aplikasi.myapplication.view.FollowerFragment

class SectionsPagerAdapter(private val mContext:Context,fm: FragmentManager): FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val tabTitle = intArrayOf(
        R.string.follower,
        R.string.following
    )
    var userName:String = ""

    override fun getItem(position: Int): Fragment {

        return FollowerFragment.newInstance(userName,position+1)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitle[position])
    }

    override fun getCount(): Int {
        return 2
    }
}