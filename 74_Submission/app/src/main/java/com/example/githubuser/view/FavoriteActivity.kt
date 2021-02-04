package com.example.githubuser.view

import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.ListFavoriteAdapter
import com.example.githubuser.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuser.database.UserHelper
import com.example.githubuser.helper.MappingHelper
import com.example.githubuser.model.FavoriteItems
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter :ListFavoriteAdapter
    companion object{
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        showRecyclerView()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadFavoriteAsync()
            }
        }

        contentResolver?.registerContentObserver(CONTENT_URI,true,myObserver)

        if(savedInstanceState == null){
            loadFavoriteAsync()
        }else{
            val list = savedInstanceState.getParcelableArrayList<FavoriteItems>(EXTRA_STATE)
            if(list != null){
                adapter.listFavorite = list
            }
        }
        supportActionBar?.title = "Favorite"
    }

    private fun showRecyclerView(){
        adapter = ListFavoriteAdapter(this)
        rv_ListUserFavorite.layoutManager = LinearLayoutManager(this)
        rv_ListUserFavorite.adapter = adapter
        rv_ListUserFavorite.setHasFixedSize(true)

    }
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList(EXTRA_STATE,adapter.listFavorite)
    }

    private fun loadFavoriteAsync(){
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorite = async(Dispatchers.IO){
                val cursor = contentResolver?.query(CONTENT_URI,null,null,null,null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorite = deferredFavorite.await()
            if(favorite.size > 0){
                adapter.listFavorite = favorite
            }else{
                adapter.listFavorite = ArrayList()
                Toast.makeText(this@FavoriteActivity,"Tidak Ada Data Saat ini",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null){
            when(requestCode){
                ProfileActivity.REQUEST_ADD ->
                    if(resultCode == ProfileActivity.RESULT_ADD){
                        val favorite = data.getParcelableExtra<FavoriteItems>(ProfileActivity.EXTRA_USER)
                        if(favorite != null){
                            adapter.addItem(favorite)
                        }
                        rv_ListUserFavorite.smoothScrollToPosition(adapter.itemCount - 1)
                    }

                ProfileActivity.REQUEST_UPDATE ->
                    when(resultCode){
                        ProfileActivity.RESULT_UPDATE -> {
                            val favoriteUpdate = data.getParcelableExtra<FavoriteItems>(ProfileActivity.EXTRA_USER)
                            val positionUpdate = data.getIntExtra(ProfileActivity.EXTRA_POSISI,0)

                            if(favoriteUpdate != null){
                                adapter.updateItem(positionUpdate,favoriteUpdate)
                            }
                            rv_ListUserFavorite.smoothScrollToPosition(positionUpdate)
                        }

                        ProfileActivity.RESULT_DELETE -> {
                            val positionDeleted = data.getIntExtra(ProfileActivity.EXTRA_POSISI,0)
                            adapter.removeItem(positionDeleted)
                        }
                    }
            }
        }
    }

}