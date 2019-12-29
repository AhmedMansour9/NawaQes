package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.Requests_Shortlists_Response
import com.nawaqes.Model.SubCategories_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class Shortlists_Requests_ViewModel :ViewModel() {

    var listProductsMutableLiveData: MutableLiveData<Requests_Shortlists_Response>? = null
    private lateinit var context: Context

    fun Requests(Token:String,lang:String, context: Context): LiveData<Requests_Shortlists_Response> {
        listProductsMutableLiveData = MutableLiveData<Requests_Shortlists_Response>()
        this.context = context
        getRequestss(Token,lang)
        return listProductsMutableLiveData as MutableLiveData<Requests_Shortlists_Response>
    }

    private fun getRequestss(Token:String,lang:String) {

        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getRequests("Bearer "+Token,lang)
        call?.enqueue(object : Callback, retrofit2.Callback<Requests_Shortlists_Response> {
            override fun onResponse(
                call: Call<Requests_Shortlists_Response>,
                response: Response<Requests_Shortlists_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<Requests_Shortlists_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }
}