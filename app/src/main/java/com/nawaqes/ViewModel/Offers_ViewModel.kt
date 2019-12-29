package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.Offers_Response
import com.nawaqes.Model.SubCategories_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class Offers_ViewModel :ViewModel(){

    var listProductsMutableLiveData: MutableLiveData<Offers_Response>? = null
    private lateinit var context: Context

    fun getOffersCategories(category_id:String?,Token:String,lang:String, context: Context): LiveData<Offers_Response> {
        listProductsMutableLiveData = MutableLiveData<Offers_Response>()
        this.context = context
        getOffers(category_id,Token,lang)
        return listProductsMutableLiveData as MutableLiveData<Offers_Response>
    }
    private fun getOffers(category_id:String?,Token:String,lang:String) {

        var map= HashMap<String,String>()
        category_id?.let { map.put("category_id", it) }
        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getOffers(map,"Bearer "+Token,lang)
        call?.enqueue(object : Callback, retrofit2.Callback<Offers_Response> {
            override fun onResponse(
                call: Call<Offers_Response>,
                response: Response<Offers_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body())

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<Offers_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }
}