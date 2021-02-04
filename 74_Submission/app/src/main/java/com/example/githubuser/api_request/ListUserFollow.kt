package com.example.githubuser.api_request

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.githubuser.adapter.ListUsersAdapter
import com.example.githubuser.model.UsersItems
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class ListUserFollow(private val context: Context) {

    var view:View? = null

    fun showLoading(state:Boolean){
        if(state){
            view?.visibility = View.VISIBLE
        }else{
            view?.visibility = View.GONE
        }
    }
    fun setUserFollow(adapters: ListUsersAdapter, users:String, categories:String ):ArrayList<UsersItems>{
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
}