package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.Inbox_Response
import com.nawaqes.Model.Offers_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class LastMessages_ViewModel  :ViewModel(){

    var listProductsMutableLiveData: MutableLiveData<Inbox_Response>? = null
    private lateinit var context: Context

    fun getLastMessages(type:String?,Token:String, context: Context): LiveData<Inbox_Response> {
        listProductsMutableLiveData = MutableLiveData<Inbox_Response>()
        this.context = context
        getLastMsg(type,Token)
        return listProductsMutableLiveData as MutableLiveData<Inbox_Response>
    }
    private fun getLastMsg(type:String?,Token:String) {

        var map= HashMap<String,String>()
        type?.let { map.put("owner", type) }
        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getLastMessage(map,"Bearer "+Token)
        call?.enqueue(object : Callback, retrofit2.Callback<Inbox_Response> {
            override fun onResponse(
                call: Call<Inbox_Response>,
                response: Response<Inbox_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body())

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<Inbox_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }
}