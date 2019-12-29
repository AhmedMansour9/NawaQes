package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.DetailsShortlists_Response
import com.nawaqes.Model.Requests_Shortlists_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class RequestDetails_ViewModel :ViewModel() {

    var listProductsMutableLiveData: MutableLiveData<DetailsShortlists_Response>? = null
    private lateinit var context: Context

    fun Requests(id:String,Token:String,lang:String, context: Context): LiveData<DetailsShortlists_Response> {
        listProductsMutableLiveData = MutableLiveData<DetailsShortlists_Response>()
        this.context = context
        getRequestss(id,Token,lang)
        return listProductsMutableLiveData as MutableLiveData<DetailsShortlists_Response>
    }

    private fun getRequestss(id:String,Token:String,lang:String) {
        var map= HashMap<String,String>()
        map.put("shortlist_id",id)
        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getCustomRequests(map,"Bearer "+Token,lang)
        call?.enqueue(object : Callback, retrofit2.Callback<DetailsShortlists_Response> {
            override fun onResponse(
                call: Call<DetailsShortlists_Response>,
                response: Response<DetailsShortlists_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<DetailsShortlists_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }
}