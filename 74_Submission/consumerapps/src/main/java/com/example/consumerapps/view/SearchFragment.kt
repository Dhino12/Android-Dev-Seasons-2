package com.example.consumerapps.view

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.TaskStackBuilder
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.consumerapps.R
import com.example.consumerapps.adapter.ListUsersAdapter
import com.example.consumerapps.model.UsersItems
import com.example.consumerapps.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.*


class SearchFragment : Fragment() {

    lateinit var userName:String
    private lateinit var TAG: Context
    private lateinit var adapter: ListUsersAdapter
    private lateinit var mainViewModel: MainViewModel
    private var listItem = ArrayList<UsersItems>()
    var username = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userName = SearchFragmentArgs.fromBundle(arguments as Bundle).userName

        val rvGithuser = view.findViewById<RecyclerView>(R.id.rv_listSearch)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.view = progressBar
        mainViewModels()
        showRecyclerView(rvGithuser)
        username = mainViewModel.setUser(userName)
    }

    private fun showRecyclerView(rvGithub: RecyclerView){
        adapter = ListUsersAdapter()
        rvGithub.layoutManager = LinearLayoutManager(activity)
        rvGithub.adapter = adapter
        adapter.setItemClickCallback(object :
            ListUsersAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UsersItems) {
                showSelectedUser(data)
            }
        })
    }

    private fun mainViewModels(){
        mainViewModel.getUser().observe(viewLifecycleOwner, Observer {
                userItems -> if(userItems != null){
            listItem.clear()
            adapter.setData(userItems)
        }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        TAG = context
    }

    fun showSelectedUser(data: UsersItems) {
        val moveWithData = Intent(TAG, ProfileActivity::class.java)

        Toast.makeText(TAG,"isi dari TAG: $TAG",Toast.LENGTH_LONG).show()
        TaskStackBuilder.create(TAG)
            .addParentStack(ProfileActivity::class.java)
            .addNextIntent(moveWithData)
            .getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT)

        showLoading(true)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                listItem = mainViewModel.searchUserData(data.userName)
                delay(2000)
                withContext(Dispatchers.Main){
                    if(listItem.size > 0){
                        showLoading(false)
                        Toast.makeText(activity ,"Success", Toast.LENGTH_SHORT).show()
                    }else{
                        showLoading(true)
                        Toast.makeText(activity,"Failed: ${mainViewModel.statusCode1}", Toast.LENGTH_SHORT).show()
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
            progressBar.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.GONE
        }
    }

    /* ====================== Menu Option ==================================*/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu,menu)

        val searchView = menu.findItem(R.id.search)?.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.title)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("",false)
                mainViewModels()
                username = mainViewModel.setUser(query)
                Toast.makeText(activity,"FrontFragment: $query",Toast.LENGTH_LONG).show()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_language){
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }
    /* ================================================================================== */
}