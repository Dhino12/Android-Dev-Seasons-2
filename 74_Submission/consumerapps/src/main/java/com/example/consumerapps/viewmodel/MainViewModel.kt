package com.example.consumerapps.viewmodel

import android.app.Application
import android.content.res.TypedArray
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.consumerapps.R
import com.example.consumerapps.adapter.ListUsersAdapter
import com.example.consumerapps.model.UsersItems
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
                        Toast.makeText(context.applicationContext,"onFailure[SearchUser]: ${error?.message} \n StutsCode: $statusCode", Toast.LENGTH_SHORT).show()
                    }
                })
        return personUsername
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
    
    fun getUser() : LiveData<ArrayList<UsersItems>>{
        return listUserGithub
    }
}
