package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //panggil data pada resource string.xml
    private val list = ArrayList<Hero>()
    //============== Mengubah Judul Halaman =================
    private var title = "ModeList"
    //============== Menjaga tampilan (SaveInstanceState)
    private var mode:Int = 0

    companion object{
        private const val STATE_TITLE = "state_string"
        private const val STATE_LIST = "state_list"
        private const val STATE_MODE = "state_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Inisalisasikan recycler view
        rv_heroes.setHasFixedSize(true)

        //===================== Menjaga tampilan (SaveInstanceState) ===============
        if(savedInstanceState == null){
            setActionBarTitle(title)
            list.addAll(getListHeroes())
            showRecyclerList()
            mode = R.id.action_list
        } else {
            title = savedInstanceState.getString(STATE_TITLE).toString()
            val stateList = savedInstanceState.getParcelableArrayList<Hero>(STATE_LIST)
            val stateMode = savedInstanceState.getInt(STATE_MODE)

            setActionBarTitle(title)
            if(stateList != null){
                list.addAll(stateList)
            }
            setMode(stateMode)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_TITLE,title)
        outState.putParcelableArrayList(STATE_LIST,list)
        outState.putInt(STATE_MODE,mode)
    }
    //===========================================================================

    private fun getListHeroes(): ArrayList<Hero>{
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.getStringArray(R.array.data_photo)

        //Memasukan data ke Parcelabel =======================================
        val listHero = ArrayList<Hero>()
        for(position in dataName.indices){
            val hero = Hero(
                dataName[position],
                dataDescription[position],
                dataPhoto[position]
            )
            listHero.add(hero)
        }
        return listHero
    }

    //============== Mengubah Judul Halaman =================
    private fun setActionBarTitle(title:String){
        supportActionBar?.title = title
    }
    //=========================================================

    //==============================================================================
    // ================== List View ================================================
    //==============================================================================
    private fun showRecyclerList(){
        rv_heroes.layoutManager = LinearLayoutManager(this)
        val listHeroAdapter = ListHeroAdapter(list)
        rv_heroes.adapter = listHeroAdapter
    }
    //==============================================================================
    // ================== Grid View ================================================
    //==============================================================================
    private fun showRecyclerGrid(){
        rv_heroes.layoutManager = GridLayoutManager(this,2)
        val gridHeroAdapter = GridHeroAdapter(list)
        rv_heroes.adapter = gridHeroAdapter
    }
    //================================================================================

    //==============================================================================
    // ================== Grid View ================================================
    //==============================================================================
    private fun showRecyclerCardView(){
        rv_heroes.layoutManager = LinearLayoutManager(this)
        val cardHeroAdapter = CardViewAdapter(list)
        rv_heroes.adapter = cardHeroAdapter
    }
    //================================================================================

    //Lalu menambahkan permission untuk terhubung dengan internet di AndroidManifest.xml

    //=================================================================================
    //================= Menu =========================================================
    //================================================================================
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode:Int){
        when(selectedMode){
            R.id.action_list -> {
                title = "Mode List"
                showRecyclerList()
            }
            R.id.action_grid -> {
                title = "Mode Grid"
                showRecyclerGrid()
            }
            R.id.action_cardView -> {
                title = "Mode CardView"
                showRecyclerCardView()
            }
        }
        mode = selectedMode
        setActionBarTitle(title)
    }
    //========================================================================
}
