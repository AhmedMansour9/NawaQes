package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.Requests_Shortlists_Response
import com.nawaqes.Model.SentMessage_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class SentToken_ViewModel  : ViewModel() {

    var listProductsMutableLiveData: MutableLiveData<SentMessage_Response>? = null
    private lateinit var context: Context

    fun Requests(firebasetokn:String?,Token: String, context: Context): LiveData<SentMessage_Response> {
        listProductsMutableLiveData = MutableLiveData<SentMessage_Response>()
        this.context = context
        getRequestss(firebasetokn,Token)
        return listProductsMutableLiveData as MutableLiveData<SentMessage_Response>
    }

    private fun getRequestss(firebasetokn:String?,Token: String) {
        var map= HashMap<String,String>()
        if (firebasetokn != null) {
            map.put("firebaseToken", firebasetokn)
        }
        map.put("deviceType", "android")

        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.SentToken(map,"Bearer " + Token)
        call?.enqueue(object : Callback, retrofit2.Callback<SentMessage_Response> {
            override fun onResponse(
                call: Call<SentMessage_Response>,
                response: Response<SentMessage_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<SentMessage_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }
}