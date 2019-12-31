package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.Cities_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class Cities_ViewModel : ViewModel() {

    public var listProductsMutableLiveData: MutableLiveData<Cities_Response>? = null
    private lateinit var context: Context


    fun getData( DeviceLang:String,context: Context): LiveData<Cities_Response> {
        listProductsMutableLiveData = MutableLiveData<Cities_Response>()
        this.context = context
        getDataValues(DeviceLang)
        return listProductsMutableLiveData as MutableLiveData<Cities_Response>
    }

    private fun getDataValues(DeviceLang:String) {

        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getCities(DeviceLang)
        call?.enqueue(object : Callback, retrofit2.Callback<Cities_Response> {
            override fun onResponse(
                call: Call<Cities_Response>,
                response: Response<Cities_Response>
            ) {

                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<Cities_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }


}

