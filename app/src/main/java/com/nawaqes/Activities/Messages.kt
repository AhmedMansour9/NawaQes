package com.nawaqes.Activities

import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nawaqes.Adapter.Inbox_Adapter
import com.nawaqes.Adapter.MyRequests_Adapter
import com.nawaqes.Model.Inbox_Response
import com.nawaqes.Model.Requests_Shortlists_Response
import com.nawaqes.R
import com.nawaqes.ViewModel.LastMessages_ViewModel
import com.nawaqes.ViewModel.Offers_ViewModel
import com.nawaqes.ViewModel.Shortlists_Requests_ViewModel
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.activity_profile.*

class Messages : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener{


    lateinit var messags_viewmodel: LastMessages_ViewModel
    lateinit var UserToken: String
    private lateinit var DataSaver: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        init()
        Selected_Tabs()
       SwipRefresh()
    }

    private fun init() {
        DataSaver = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        UserToken = DataSaver.getString("token", null)!!

    }

    fun SwipRefresh(){
        SwipMessages.setOnRefreshListener(this)
        SwipMessages.isRefreshing=true
        SwipMessages.isEnabled = true
        SwipMessages.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
        SwipMessages.post(Runnable {
            getInboxes()
            getSent()
        })
    }


    private fun getInboxes() {
        this.applicationContext?.let {
            messags_viewmodel= ViewModelProvider.NewInstanceFactory().create(
                LastMessages_ViewModel::class.java)
            messags_viewmodel.getLastMessages("shop",UserToken, it).observe(this,
                Observer<Inbox_Response> { loginmodel ->
                    SwipMessages.isRefreshing=false

                    if(loginmodel!=null) {
                        val listAdapter =
                            Inbox_Adapter(applicationContext, loginmodel.data)
//                        listAdapter.onClick(this)
                        recycler_inbox.layoutManager = LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        recycler_inbox.setAdapter(listAdapter)
                    }

                })
        }
    }
    private fun getSent() {
        this.applicationContext?.let {
            messags_viewmodel= ViewModelProvider.NewInstanceFactory().create(
                LastMessages_ViewModel::class.java)
            messags_viewmodel.getLastMessages("customer",UserToken, it).observe(this,
                Observer<Inbox_Response> { loginmodel ->
                    SwipMessages.isRefreshing=false

                    if(loginmodel!=null) {
                        val listAdapter =
                            Inbox_Adapter(applicationContext, loginmodel.data)
//                        listAdapter.onClick(this)
                        recycler_sent.layoutManager = LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        recycler_sent.setAdapter(listAdapter)
                    }

                })
        }
    }

    fun Selected_Tabs(){
        T_Send.setOnClickListener(){
            onClickSent()
        }
        T_Inbox.setOnClickListener(){
            onClickInbox()
        }
    }

    fun onClickSent(){
        T_Send.setTextColor(Color.parseColor("#ffffff"))
        T_Send.setBackgroundResource(R.drawable.bc_select_search)
//            T_MyRequests.setBackgroundColor(Color.TRANSPARENT)
        T_Inbox.setTextColor(Color.parseColor("#848586"))
        T_Inbox.setBackgroundColor(Color.TRANSPARENT)
        recycler_sent.visibility= View.VISIBLE
        recycler_inbox.visibility= View.GONE
    }
    fun onClickInbox(){
        T_Send.setTextColor(Color.parseColor("#848586"))
        T_Send.setBackgroundColor(Color.TRANSPARENT)
        T_Inbox.setTextColor(Color.parseColor("#ffffff"))
        T_Inbox.setBackgroundResource(R.drawable.bc_select_search)
        recycler_sent.visibility= View.GONE
        recycler_inbox.visibility= View.VISIBLE
    }

    override fun onRefresh() {
        getInboxes()
        getSent()
    }
}
