package com.nawaqes.Activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nawaqes.Adapter.HistoryMessages_Adapter
import com.nawaqes.Adapter.Inbox_Adapter
import com.nawaqes.Model.HistoryMessages_Response
import com.nawaqes.Model.Inbox_Response
import com.nawaqes.R
import com.nawaqes.ViewModel.History_ViewModel
import com.nawaqes.ViewModel.LastMessages_ViewModel
import kotlinx.android.synthetic.main.activity_history__messages.*
import kotlinx.android.synthetic.main.activity_messages.*

class History_Messages : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener{
    override fun onRefresh() {
        getHistory()
    }

    lateinit var messags_viewmodel: History_ViewModel
    lateinit var UserToken: String
    private lateinit var DataSaver: SharedPreferences
    lateinit var LastMsg:Inbox_Response.Data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history__messages)
        init()
        getData()
        SwipRefresh()
        SentMessage()
    }

    private fun SentMessage() {
        Open_Replay.setOnClickListener(){
            val intent= Intent(this, Sent_Message::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("type","inbox")
            intent.putExtra("name",HistoryMessages_Adapter.name)
            intent.putExtra("id",HistoryMessages_Adapter.id)
            intent.putExtra("phone",HistoryMessages_Adapter.phone)
            intent.putExtra("address",HistoryMessages_Adapter.address)

            startActivity(intent)
        }

    }

    private fun getData() {
        val type=intent.getStringExtra("type")
        if(type.equals("inbox")) {
            LastMsg = intent.getParcelableExtra("inbox")
        }else {

        }
    }

    private fun init() {
        DataSaver = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        UserToken = DataSaver.getString("token", null)!!

    }

    fun SwipRefresh(){
        SwipHistory.setOnRefreshListener(this)
        SwipHistory.isRefreshing=true
        SwipHistory.isEnabled = true
        SwipHistory.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
        SwipHistory.post(Runnable {
            getHistory()
        })
    }

    private fun getHistory() {
        this.applicationContext?.let {
            messags_viewmodel= ViewModelProvider.NewInstanceFactory().create(
                History_ViewModel::class.java)
            messags_viewmodel.getHistoryMessages(LastMsg.shop.id.toString(),LastMsg.owner,UserToken, it).observe(this,
                Observer<HistoryMessages_Response> { loginmodel ->
                    SwipHistory.isRefreshing=false

                    if(loginmodel!=null) {
                        Open_Replay.visibility= View.VISIBLE
                        val listAdapter =
                            HistoryMessages_Adapter(applicationContext, loginmodel.data)
//                        listAdapter.onClick(this)
                        recycler_history.layoutManager = LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        recycler_history.setAdapter(listAdapter)
                        recycler_history.scrollToPosition(loginmodel.data.size - 1);

                    }

                })
        }
    }

    override fun onResume() {
        super.onResume()
        getHistory()
    }
}
