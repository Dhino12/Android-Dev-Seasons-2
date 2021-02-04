package com.aplikasi.myapplication.view


import android.app.PendingIntent
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasi.myapplication.viewmodel.MainViewModel
import com.aplikasi.myapplication.R
import com.aplikasi.myapplication.model.UsersItems
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ListUsersAdapter
    private lateinit var mainViewModel: MainViewModel
    private var listItem = ArrayList<UsersItems>()
    var username = ArrayList<String>()

    companion object{
        const val STATE_RESULT = "state_result"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(STATE_RESULT,username)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this@MainActivity).get(MainViewModel::class.java)
        mainViewModel.view = progress_bar
        mainViewModels()
        showRecyclerView()
        if(username.isEmpty()){
            listItem = mainViewModel.prepare(adapter)
        }
    }

    private fun showRecyclerView(){
        adapter = ListUsersAdapter()
        rv_gitUser.layoutManager = LinearLayoutManager(this)
        rv_gitUser.adapter = adapter
        adapter.setItemClickCallback(object :
            ListUsersAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UsersItems) {
                showSelectedUser(data)
            }
        })
    }

    private fun mainViewModels(){
            mainViewModel.getUser().observe(this, Observer {
                    userItems -> if(userItems != null){
                listItem.clear()
                adapter.setData(userItems)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu,menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.title)
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("",false)

                mainViewModels()
                username = mainViewModel.setUser(query)

                Toast.makeText(this@MainActivity,"input: $query",Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean { 
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_language){
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun showSelectedUser(data: UsersItems) {
        val moveWithData = Intent(this@MainActivity, ProfileActivity::class.java)

        TaskStackBuilder.create(this)
            .addParentStack(ProfileActivity::class.java)
            .addNextIntent(moveWithData)
            .getPendingIntent(110,PendingIntent.FLAG_UPDATE_CURRENT)

        showLoading(true)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                listItem = mainViewModel.searchUserData(data.userName)
                delay(2000)
                withContext(Dispatchers.Main){
                    if(listItem.size > 0){
                        showLoading(false)
                        Toast.makeText(this@MainActivity,"Success", Toast.LENGTH_SHORT).show()
                    }else{
                        showLoading(true)
                        Toast.makeText(this@MainActivity,"Failed: ${mainViewModel.statusCode1}", Toast.LENGTH_SHORT).show()
                    }
                    for (position in 0 until listItem.size) {
                        
                        Log.d("error","isi listItem: ${listItem.toList()}")

                        moveWithData.putExtra(ProfileActivity.EXTRA_PERSON, listItem[position])
                        startActivity(moveWithData)
                    }
                }

            }catch (e:Exception){
                Log.d("error","Coroutines[Exception]: ${e.message}")
            }
        }
    }
    private fun showLoading(state:Boolean){
        if(state){
            progress_bar.visibility = View.VISIBLE
        }else{
            progress_bar.visibility = View.GONE
        }
    }
}