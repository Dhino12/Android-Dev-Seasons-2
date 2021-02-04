package com.aplikasi.myapplication.viewmodel

import android.app.Application
import android.content.res.TypedArray
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aplikasi.myapplication.R
import com.aplikasi.myapplication.model.UsersItems
import com.aplikasi.myapplication.view.ListUsersAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val listUserGithub = MutableLiveData<ArrayList<UsersItems>>()
    var view:View? = null
    private val context = getApplication<Application>().applicationContext
    var statusCode1:Int? = null

    fun showLoading(state:Boolean){
        if(state){
            view?.visibility = View.VISIBLE
        }else{
            view?.visibility = View.GONE
        }
    }

    fun setUser(user:String?):ArrayList<String>{
        showLoading(true)
            val personUsername = ArrayList<String>()
            val listItem = ArrayList<UsersItems>()
            val token = "3289d7200c9aa29cc2c385983dbbd5a04eeebe42"
                val url = "https://api.github.com/search/users?q=$user"
                val client=  AsyncHttpClient()
                client.addHeader("Authorization",token)
                client.addHeader("User-Agent","request")
                client.get(url,object : AsyncHttpResponseHandler(){
                    override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                        showLoading(false)
                        try {
                            val result = responseBody?.let { String(it) } ?: ""
                            val responseObject = JSONObject(result)
                            val item = responseObject.getJSONArray("items")
                            for(position in 0 until item.length()){
                                val person = item.getJSONObject(position)
                                personUsername.add(person.getString("login"))
                                val userItems =
                                    UsersItems(
                                        "",
                                        person.getString("login"),
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        person.getString("avatar_url"),
                                        position_ = position,
                                        visibility = View.GONE
                                    )
                                listItem.add(userItems)
                                listUserGithub.postValue(listItem)
                            }
                        }catch (e: Exception){
                            Log.d("Exception",e.message.toString())
                            Toast.makeText(context.applicationContext,"catch[SearchUser]:: ${e.message} \n ", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                        Log.d("onFailure",error?.message.toString())
                        Toast.makeText(context.applicationContext,"onFailure[SearchUser]: ${error?.message} \n ", Toast.LENGTH_SHORT).show()
                    }
                })
        return personUsername
    }

    fun prepare(adapter: ListUsersAdapter):ArrayList<UsersItems>{
        val listItem = ArrayList<UsersItems>()

        val dataColors: TypedArray = context.resources.obtainTypedArray(R.array.flat)
        val dataPhoto: TypedArray = context.resources.obtainTypedArray(R.array.avatar)
        val dataName:Array<String> = context.resources.getStringArray(R.array.name)
        val dataUserName:Array<String> = context.resources.getStringArray(R.array.username)
        val dataFollower:Array<String> = context.resources.getStringArray(R.array.followers)
        val dataRepo:Array<String> = context.resources.getStringArray(R.array.repository)
        val dataFollowing:Array<String> = context.resources.getStringArray(R.array.following)
        val dataLocation:Array<String> = context.resources.getStringArray(R.array.location)
        val dataCompany:Array<String> = context.resources.getStringArray(R.array.company)

        for(position in dataUserName.indices){
            val users = UsersItems(
                dataName[position],
                dataUserName[position],
                dataRepo[position],
                dataFollower[position],
                dataFollowing[position],
                dataCompany[position],
                dataLocation[position],
                "",
                profImage = dataPhoto.getResourceId(position,-1),
                colors = dataColors.getResourceId(position,-1),
                position_ = position,
                visibility = View.VISIBLE
            )
            listItem.add(users)
            adapter.setData(listItem)
        }
        return listItem
    }

    fun searchUserData(personUsername: String):ArrayList<UsersItems>{
        val listItemUser = ArrayList<UsersItems>()
            Log.d("error","listItemUser[searchUser]: ${listItemUser.toList()} isi String: $personUsername")

            val url = "https://api.github.com/users/${personUsername }"

            val token = "3289d7200c9aa29cc2c385983dbbd5a04eeebe42"
            val client=  SyncHttpClient()
            client.addHeader("Authorization",token)
            client.addHeader("User-Agent","request")
            client.get(url,object : AsyncHttpResponseHandler(){

                override fun onSuccess(statusCode: Int, headers: Array<out Header>? , responseBody: ByteArray?) {
                    try {
                        val result = responseBody?.let { String(it) }
                        val users = JSONObject(result)
                        val userItems = UsersItems(
                                users.getString("name"),
                                users.getString("login"),
                                users.getString("public_repos"),
                                users.getString("followers"),
                                users.getString("following"),
                                users.getString("company"),
                                users.getString("location"),
                                users.getString("avatar_url"),
                                visibility = View.VISIBLE
                            )
                        listItemUser.add(userItems)
                    }catch (e: Exception){
                        Log.d("error","catch[DataSearchData]: ${e.message} ")
                    }
                }

                override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                    Log.d("error","onFailure[searchUserData]: ${error?.message} statusCode: $statusCode")
                    statusCode1 = statusCode
                }
            })
        return listItemUser
    }

    fun setUserFollow(adapters:ListUsersAdapter, users:String, categories:String ):ArrayList<UsersItems>{
        showLoading(true)
        val listItem = ArrayList<UsersItems>()
        val token = "3289d7200c9aa29cc2c385983dbbd5a04eeebe42"
        val url = "https://api.github.com/users/$users/$categories"
        val client = AsyncHttpClient()
        client.addHeader("Authorization",token)
        client.addHeader("User-Agent","request")
        client.get(url,object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                showLoading(false)
                try {
                    val result = responseBody?.let { String(it) }
                    val user = JSONArray(result)

                    for(position in 0 until user.length()){
                        val person = user.getJSONObject(position)
                        val userItems =
                            UsersItems(
                                "",
                                person.getString("login"),
                                "",
                                "",
                                "",
                                "",
                                "",
                                person.getString("avatar_url"),
                                visibility = View.GONE
                            )
                        listItem.add(userItems)
                    }
                    adapters.setData(listItem)

                }catch (e:Exception){
                    Log.d("error","catch: ${e.message}")
                    Toast.makeText(context.applicationContext,"catch[dataFollo]: ${e.message} \n ", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?
            ) {
                Log.d("error","onFailure: ${error?.message} status: $statusCode categories: $$categories")
                Toast.makeText(context.applicationContext,"onFailure[dataFollo]: ${error?.message} \n ", Toast.LENGTH_SHORT).show()
            }

        })
        return listItem
    }
    
    fun getUser() : LiveData<ArrayList<UsersItems>>{
        return listUserGithub
    }
}
