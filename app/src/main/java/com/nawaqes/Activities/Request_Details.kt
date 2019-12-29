package com.nawaqes.Activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.nawaqes.Adapter.MyRequests_Adapter
import com.nawaqes.Adapter.RequestDetails_Adapter
import com.nawaqes.Model.DetailsShortlists_Response
import com.nawaqes.Model.Requests_Shortlists_Response
import com.nawaqes.R
import com.nawaqes.ViewModel.RequestDetails_ViewModel
import com.nawaqes.ViewModel.Shortlists_Requests_ViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_request__details.*
import kotlinx.android.synthetic.main.activity_request__details.SwipHome
import kotlinx.android.synthetic.main.activity_request__details.recycler_Requests

class Request_Details : AppCompatActivity() {
    lateinit var requests:RequestDetails_ViewModel
    lateinit var UserToken: String
    private lateinit var DataSaver: SharedPreferences
    lateinit var id:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request__details)
        getData()
       getRequests()

    }

    private fun getData() {
        DataSaver = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        UserToken = DataSaver.getString("token", null)!!

        val request:Requests_Shortlists_Response.Data=intent.getParcelableExtra("item")
        T_Request.text=getString(R.string.requestto)+" "+request.categoryName
        T_Date.text=request.createdAt
        T_Description.text=request.description
        T_Viwers.text=resources.getString(R.string.viwers) +" ( "+request.acceptedByShop+" "+request.categoryName+" )"
        id=request.id.toString()
        Glide.with(this)
            .load( request.image)
            .into(roundedImageView)
    }

    private fun getRequests() {
        requests= ViewModelProvider.NewInstanceFactory().create(
            RequestDetails_ViewModel::class.java)
        this.applicationContext?.let {
            requests.Requests(id,UserToken,"en", it).observe(this,
                Observer<DetailsShortlists_Response> { loginmodel ->
                    SwipHome.isRefreshing=false

                    if(loginmodel!=null) {
                        val listAdapter =
                            RequestDetails_Adapter(applicationContext, loginmodel.data)
//                        listAdapter.onClick(this)
                        recycler_Requests.layoutManager = LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        recycler_Requests.setAdapter(listAdapter)
                    }

                })
        }
    }

    override fun onResume() {
        super.onResume()
        getRequests()
    }
}
