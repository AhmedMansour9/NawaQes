package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.Banners_Response
import com.nawaqes.Model.CountNotifications_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class CountNotifications_ViewModel :ViewModel () {

    var listProductsMutableLiveData: MutableLiveData<CountNotifications_Response>? = null
    private lateinit var context: Context
    fun getData(
        Token: String,
        context: Context
    ): LiveData<CountNotifications_Response> {
        listProductsMutableLiveData = MutableLiveData<CountNotifications_Response>()
        this.context = context
        getDataValues( Token)
        return listProductsMutableLiveData as MutableLiveData<CountNotifications_Response>
    }
    fun getNewNotifications(
        Token: String,
        context: Context
    ): LiveData<CountNotifications_Response> {
        listProductsMutableLiveData = MutableLiveData<CountNotifications_Response>()
        this.context = context
        getDataValues( Token)
        return listProductsMutableLiveData as MutableLiveData<CountNotifications_Response>
    }

    private fun getDataValues( Token: String) {

        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getNewMessage( "Bearer " + Token)
        call?.enqueue(object : Callback, retrofit2.Callback<CountNotifications_Response> {
            override fun onResponse(
                call: Call<CountNotifications_Response>,
                response: Response<CountNotifications_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<CountNotifications_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }
    private fun NewNotifications( Token: String) {

        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getNewMessage( "Bearer " + Token)
        call?.enqueue(object : Callback, retrofit2.Callback<CountNotifications_Response> {
            override fun onResponse(
                call: Call<CountNotifications_Response>,
                response: Response<CountNotifications_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<CountNotifications_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }
}