package com.aplikasi.mysubmmision

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {
    private lateinit var mtvName: TextView
    private lateinit var mtvUserName: TextView
    private lateinit var mtvFollower: TextView
    private lateinit var mtvRepository: TextView
    private lateinit var mtvFollowing: TextView
    private lateinit var mimageProf: CircleImageView
    private lateinit var mtvLocation: TextView
    private lateinit var mtvCompany: TextView
    private lateinit var mimageColors: ImageView

    companion object{
        const val EXTRA_PERSON = "extra_person"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mtvName = findViewById(R.id.profile_name)
        mtvUserName = findViewById(R.id.profile_username)
        mtvFollower = findViewById(R.id.profile_follower)
        mtvRepository = findViewById(R.id.profile_repository)
        mtvFollowing = findViewById(R.id.profile_following)
        mtvLocation = findViewById(R.id.profile_location)
        mtvCompany = findViewById(R.id.profile_company)
        mimageProf = findViewById(R.id.img_profile)
        mimageColors = findViewById(R.id.img_cover)

        val person = intent.getParcelableExtra(EXTRA_PERSON) as Person

        mtvName.text = person.name
        mtvUserName.text = person.username
        mtvFollower.text = person.follower
        mtvRepository.text = person.reopsitory
        mtvFollowing.text = person.following
        mtvLocation.text = person.location
        mtvCompany.text = person.company
        mimageProf.setImageResource(person.image)
        mimageColors.setImageResource(person.colors)
    }
}