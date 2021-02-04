package com.aplikasi.myapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import com.aplikasi.myapplication.R
import com.aplikasi.myapplication.model.SectionsPagerAdapter
import com.aplikasi.myapplication.model.UsersItems
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PERSON = "extra_person"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val users: UsersItems = intent.getParcelableExtra<Parcelable>(
            EXTRA_PERSON
        ) as UsersItems

        profile_name.text = users.name
        profile_username.text = users.userName
        profile_follower.text = users.follower
        profile_repository.text = users.repository
        profile_following.text = users.following
        profile_location.text = users.location
        profile_company.text = users.company
        titleProfile_Company.text = resources.getString(R.string.company)
        titleProfile_Repository.text = resources.getString(R.string.repository)
        titleProfile_Location.text = resources.getString(R.string.location)
        titleProfile_Follower.text = resources.getString(R.string.follower)
        titleProfile_Following.text = resources.getString(R.string.following)


        if(users.profImage != null){
            Glide.with(this)
                .load(users.profImage)
                .apply(RequestOptions())
                .into(img_profile)
        }else{
            Glide.with(this)
                .load(users.avatarProf)
                .apply(RequestOptions())
                .into(img_profile)
        }
        img_cover.setImageResource(users.colors)

        //=========== TAB LAYOUT ==============
        val sectionPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionPagerAdapter.userName = users.userName
        view_pager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }
}