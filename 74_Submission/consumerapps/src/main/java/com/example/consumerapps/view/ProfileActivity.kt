package com.example.consumerapps.view

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.res.TypedArray
import android.database.ContentObserver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapps.R
import com.example.consumerapps.database.DatabaseContract
import com.example.consumerapps.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.consumerapps.helper.MappingHelper
import com.example.consumerapps.model.FavoriteItems
import com.example.consumerapps.model.SectionsPagerAdapter
import com.example.consumerapps.model.UsersItems
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream


class ProfileActivity : AppCompatActivity(),View.OnClickListener {

    //=============== Implementation SQLITE ===========================
    private var isEdit = false
    private var favoriteItems:FavoriteItems? = null
    private var position :Int = 0
    private lateinit var uriWithID:Uri
    private var it:UsersItems? = null
    var listFavorite = ArrayList<FavoriteItems>()
    var favIcon:TypedArray? = null
    var idFromDB:Int? = null
    //=================================================================

    companion object {
        const val EXTRA_PERSON = "extra_person"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val REQUEST_UPDATE = 200

        const val EXTRA_USER = "extra_note"
        const val EXTRA_POSISI = "extra_posisi"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.elevation = 0f

        //=============== Implementation SQLITE ===========================
        fab_add.setOnClickListener(this)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }
        contentResolver?.registerContentObserver(CONTENT_URI,true,myObserver)

        favoriteItems = intent.getParcelableExtra(EXTRA_FAVORITE) as FavoriteItems?

        if(favoriteItems != null){
            position = intent.getIntExtra(EXTRA_POSISI,0)
            isEdit = true

            uriWithID = Uri.parse(CONTENT_URI.toString() + "/" + favoriteItems?.id)
            val cursor = contentResolver?.query(uriWithID,null,null,null,null)
            if(cursor != null){
                favoriteItems = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }

            profile_name.text = favoriteItems?.name
            profile_username.text = favoriteItems?.userName
            profile_follower.text = favoriteItems?.follower
            profile_repository.text = favoriteItems?.repository
            profile_following.text = favoriteItems?.following
            profile_location.text = favoriteItems?.location
            profile_company.text = favoriteItems?.company
            titleProfile_Company.text = resources.getString(R.string.company)
            titleProfile_Repository.text = resources.getString(R.string.repository)
            titleProfile_Location.text = resources.getString(R.string.location)
            titleProfile_Follower.text = resources.getString(R.string.follower)
            titleProfile_Following.text = resources.getString(R.string.following)

            Glide.with(this)
                .load(favoriteItems?.favIcon)
                .apply(RequestOptions())
                .into(fab_add)

            if(favoriteItems?.profImage != null){
                Glide.with(this)
                    .load(favoriteItems?.profImage)
                    .apply(RequestOptions())
                    .into(img_profile)
            }else{
                Glide.with(this)
                    .load(favoriteItems?.avatarProf)
                    .apply(RequestOptions())
                    .into(img_profile)
            }

            Toast.makeText(this,"Masuk IF EDIT",Toast.LENGTH_SHORT).show()
        }else{
            it = intent.getParcelableExtra<Parcelable>(EXTRA_PERSON) as UsersItems?

            profile_name.text = it?.name
            profile_username.text = it?.userName
            profile_follower.text = it?.follower
            profile_repository.text = it?.repository
            profile_following.text = it?.following
            profile_location.text = it?.location
            profile_company.text = it?.company
            titleProfile_Company.text = resources.getString(R.string.company)
            titleProfile_Repository.text = resources.getString(R.string.repository)
            titleProfile_Location.text = resources.getString(R.string.location)
            titleProfile_Follower.text = resources.getString(R.string.follower)
            titleProfile_Following.text = resources.getString(R.string.following)

            if(it?.profImage != null){
                Glide.with(this)
                    .load(it?.profImage)
                    .apply(RequestOptions())
                    .into(img_profile)
            }else{
                Glide.with(this)
                    .load(it?.avatarProf)
                    .apply(RequestOptions())
                    .into(img_profile)
            }
            loadUserAsync()
            favoriteItems = FavoriteItems()
        }

        //=========== TAB LAYOUT ==============
        val sectionPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        if(favoriteItems != null && it == null){
            sectionPagerAdapter.userName = favoriteItems?.userName.toString()
        }else{
            sectionPagerAdapter.userName = it?.userName.toString()
        }
        view_pager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(view_pager)

    }

    @SuppressLint("ResourceType")
    override fun onClick(view: View?) {
        if(view?.id == R.id.fab_add){
            val userName = profile_username.text.toString()
            val name = profile_name.text.toString()
            val company = profile_company.text.toString()
            val location = profile_location.text.toString()
            val follower = profile_follower.text.toString()
            val following = profile_following.text.toString()
            val repository = profile_repository.text.toString()

            if(it?.userName != null && idFromDB == null){
                favIcon = resources.obtainTypedArray(R.array.favIcon)
                favoriteItems?.favImage = favIcon?.getResourceId(0,0)
                favoriteItems?.userName = userName
                favoriteItems?.name = name
                favoriteItems?.company = company
                favoriteItems?.location = location
                favoriteItems?.follower = follower
                favoriteItems?.following = following
                favoriteItems?.repository = repository
                favoriteItems?.avatarProf = it?.avatarProf

                val intent = Intent()
                intent.putExtra(EXTRA_USER,favoriteItems)
                intent.putExtra(EXTRA_POSISI,position)

                val values = ContentValues()
                values.put(DatabaseContract.UserColumns.USERNAME,userName)
                values.put(DatabaseContract.UserColumns.NAME,name)
                values.put(DatabaseContract.UserColumns.COMPANY,company)
                values.put(DatabaseContract.UserColumns.LOCATION,location)
                values.put(DatabaseContract.UserColumns.FOLLOWER,follower)
                values.put(DatabaseContract.UserColumns.FOLLOWING,following)
                values.put(DatabaseContract.UserColumns.REPOSITORY,repository)
                values.put(DatabaseContract.UserColumns.IMAGE,favoriteItems?.avatarProf)
                val v = BitmapFactory.decodeResource(resources,R.drawable.baseline_favorite_white_18dp)
                Log.d("error","isiBitmap: $v")
                values.put(DatabaseContract.UserColumns.FAVICON,imageViewToByte(v))

                if(isEdit){
                    Toast.makeText(this,"isEdit Tambah: $isEdit",Toast.LENGTH_SHORT).show()
                }else{

                    contentResolver?.insert(CONTENT_URI,values)
                    Toast.makeText(this,"Satu Item Berhasil Disimpan",Toast.LENGTH_SHORT).show()
                }
            }else{
                favIcon = resources.obtainTypedArray(R.array.favIcon)
                favoriteItems?.favImage = favIcon?.getResourceId(1,0)
                fab_add.setImageResource(favoriteItems?.favImage?:0)
                if(idFromDB == null && it?.userName == null) {
                    contentResolver?.delete(uriWithID,null,null)
                }else{
                    uriWithID = Uri.parse("$CONTENT_URI/$idFromDB")
                    contentResolver?.delete(uriWithID,null,null)
                }
                Toast.makeText(this, "Satu item berhasil dihapus ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun imageViewToByte(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun loadUserAsync(){
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI,null,null,null,null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorite = deferredFavorite.await()

            if(favorite.size > 0){
                listFavorite = favorite
                favIcon = resources.obtainTypedArray(R.array.favIcon)
                    withContext(Dispatchers.Main){
                        for(position in listFavorite.indices){
                            if(listFavorite[position].userName == it?.userName){
                                idFromDB = listFavorite[position].id
                                fab_add.setImageResource(favIcon?.getResourceId(0,0)!!)
                            }
                        }
                    }
            }else{
                listFavorite = ArrayList()
            }
        }
    }
}