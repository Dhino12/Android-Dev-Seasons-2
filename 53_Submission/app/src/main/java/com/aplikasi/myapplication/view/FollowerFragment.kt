package com.aplikasi.myapplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.myapplication.R
import com.aplikasi.myapplication.model.UsersItems
import com.aplikasi.myapplication.viewmodel.MainViewModel

class FollowerFragment : Fragment() {
    private lateinit var adapters: ListUsersAdapter
    private lateinit var rvFollower:RecyclerView
    private lateinit var mainViewModel: MainViewModel
    private var listItem = ArrayList<UsersItems>()

    companion object{
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_SECTION = "section_number"
        fun newInstance(user:String, index:Int): FollowerFragment {
                val args = Bundle()
                val fragment = FollowerFragment()
                args.putString(EXTRA_USERNAME,user)
                args.putInt(EXTRA_SECTION,index)
                fragment.arguments = args
                return fragment
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_follower,container,false)
        var userName:String? = null
        var index:Int? = null
        val category = arrayOf("followers","following")
        if(arguments != null){
            userName = arguments?.getString(EXTRA_USERNAME)
            index = arguments?.getInt(EXTRA_SECTION)
        }

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        mainViewModel.view = view.findViewById(R.id.progress_bar_fol)
        rvFollower = view.findViewById(R.id.rv_gitFollower)

        rvFollower.setHasFixedSize(true)

        adapters = ListUsersAdapter()
        rvFollower.layoutManager = LinearLayoutManager(activity)
        rvFollower.adapter = adapters

        listItem = if (index == 1){
            mainViewModel.setUserFollow(adapters,userName.toString(),category[0] )
        }else{
            mainViewModel.setUserFollow(adapters,userName.toString(),category[1] )
        }

        return view
    }

}