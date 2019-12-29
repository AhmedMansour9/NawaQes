package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.HistoryMessages_Response
import com.nawaqes.Model.Inbox_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class History_ViewModel :ViewModel() {

    var listProductsMutableLiveData: MutableLiveData<HistoryMessages_Response>? = null
    private lateinit var context: Context

    fun getHistoryMessages(id:String,type:String?,Token:String, context: Context): LiveData<HistoryMessages_Response> {
        listProductsMutableLiveData = MutableLiveData<HistoryMessages_Response>()
        this.context = context
        getLastMsg(id,type,Token)
        return listProductsMutableLiveData as MutableLiveData<HistoryMessages_Response>
    }
    private fun getLastMsg(id:String,type:String?,Token:String) {

        var map= HashMap<String,String>()
        type?.let { map.put("owner", type) }
        type?.let { map.put("shop_id", id) }

        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getHistoryMessage(map,"Bearer "+Token)
        call?.enqueue(object : Callback, retrofit2.Callback<HistoryMessages_Response> {
            override fun onResponse(
                call: Call<HistoryMessages_Response>,
                response: Response<HistoryMessages_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body())

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<HistoryMessages_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }
}