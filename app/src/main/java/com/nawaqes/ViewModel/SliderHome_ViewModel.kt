package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.Banners_Response
import com.nawaqes.Model.Categories_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class SliderHome_ViewModel :ViewModel () {

     var listProductsMutableLiveData: MutableLiveData<Banners_Response>? = null
    private lateinit var context: Context
    fun getData(category_id:String,Token:String,lang:String, context: Context): LiveData<Banners_Response> {
        listProductsMutableLiveData = MutableLiveData<Banners_Response>()
        this.context = context
        getDataValues(category_id,Token,lang)
        return listProductsMutableLiveData as MutableLiveData<Banners_Response>
    }

    private fun getDataValues(category_id:String,Token:String,lang:String) {

        var map= HashMap<String,String>()
        map.put("category_id",category_id)
        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getBanners(map,"Bearer "+Token,lang)
        call?.enqueue(object : Callback, retrofit2.Callback<Banners_Response> {
            override fun onResponse(
                call: Call<Banners_Response>,
                response: Response<Banners_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<Banners_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }


}