package com.aplikasi.mysubmmision

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var rvUsers:RecyclerView
    private var list:ArrayList<Person> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvUsers = findViewById(R.id.rv_gitUser)
        rvUsers.setHasFixedSize(true)
        showRecyclerView()
        prepare()
    }

    private fun showRecyclerView(){
        rvUsers.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        rvUsers.adapter = listUserAdapter
        listUserAdapter.setItemClickCallback(object :ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Person) {
                showSelectedUser(data)
            }
        })
    }

    private fun prepare(){

        val dataColors:TypedArray = resources.obtainTypedArray(R.array.flat)
        val dataPhoto:TypedArray = resources.obtainTypedArray(R.array.avatar)
        val dataName:Array<String> = resources.getStringArray(R.array.name)
        val dataUserName:Array<String> = resources.getStringArray(R.array.username)
        val dataFollower:Array<String> = resources.getStringArray(R.array.followers)
        val dataRepo:Array<String> = resources.getStringArray(R.array.repository)
        val dataFollowing:Array<String> = resources.getStringArray(R.array.following)
        val dataLocation:Array<String> = resources.getStringArray(R.array.location)
        val dataCompany:Array<String> = resources.getStringArray(R.array.company)

        for(position in dataUserName.indices){
            val users = Person(
                dataUserName[position],
                dataRepo[position],
                dataFollower[position],
                dataFollowing[position],
                dataPhoto.getResourceId(position,-1),
                dataName[position],
                dataLocation[position],
                dataCompany[position],
                dataColors.getResourceId(position,-1)
            )
            list.add(users)
        }
    }

    fun showSelectedUser(data:Person){
        val moveWithData = Intent(this,ProfileActivity::class.java)
        for(position in list.indices){
            when(data.username){
                "JakeWharton" -> {
                    if(position == 0){
                        moveWithData.putExtra(ProfileActivity.EXTRA_PERSON,list[position])
                        startActivity(moveWithData)
                    }
                }
                "amitshekhariitbhu" -> {
                    if(position == 1){
                        moveWithData.putExtra(ProfileActivity.EXTRA_PERSON,list[position])
                        startActivity(moveWithData)
                    }
                }
                "romainguy" -> {
                    if(position == 2){
                        moveWithData.putExtra(ProfileActivity.EXTRA_PERSON,list[position])
                        startActivity(moveWithData)
                    }
                }
                "chrisbanes" -> {
                    if(position == 3){
                        moveWithData.putExtra(ProfileActivity.EXTRA_PERSON,list[position])
                        startActivity(moveWithData)
                    }
                }
                "tipsy" -> {
                    if(position == 4){
                        moveWithData.putExtra(ProfileActivity.EXTRA_PERSON,list[position])
                        startActivity(moveWithData)
                    }
                }
                "ravi8x" -> {
                    if(position == 5){
                        moveWithData.putExtra(ProfileActivity.EXTRA_PERSON,list[position])
                        startActivity(moveWithData)
                    }
                }
                "jasoet" -> {
                    if(position == 6){
                        moveWithData.putExtra(ProfileActivity.EXTRA_PERSON,list[position])
                        startActivity(moveWithData)
                    }
                }
                "budioktaviyan" -> {
                    if(position == 7){
                        moveWithData.putExtra(ProfileActivity.EXTRA_PERSON,list[position])
                        startActivity(moveWithData)
                    }
                }
                "hendisantika" -> {
                    if(position == 8){
                        moveWithData.putExtra(ProfileActivity.EXTRA_PERSON,list[position])
                        startActivity(moveWithData)
                    }
                }
                "sidiqpermana" -> {
                    if(position == 9){
                        moveWithData.putExtra(ProfileActivity.EXTRA_PERSON,list[position])
                        startActivity(moveWithData)
                    }
                }
            }
        }
    }
}