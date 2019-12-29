package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.Categories_Response
import com.nawaqes.Model.Profile_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class Profile_ViewModel :ViewModel(){

    public var listProductsMutableLiveData: MutableLiveData<Profile_Response>? = null
    private lateinit var context: Context

    fun getData(Token:String,lang:String, context: Context): LiveData<Profile_Response> {
        listProductsMutableLiveData = MutableLiveData<Profile_Response>()
        this.context = context
        getDataValues(Token,lang)
        return listProductsMutableLiveData as MutableLiveData<Profile_Response>
    }


    private fun getDataValues(Token:String,lang:String) {


        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getProfile("Bearer "+Token,lang)
        call?.enqueue(object : Callback, retrofit2.Callback<Profile_Response> {
            override fun onResponse(
                call: Call<Profile_Response>,
                response: Response<Profile_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<Profile_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }

}