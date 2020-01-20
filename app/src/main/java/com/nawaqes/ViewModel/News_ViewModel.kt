package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.News_Response
import com.nawaqes.Model.Offers_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class News_ViewModel :ViewModel() {

    var listProductsMutableLiveData: MutableLiveData<News_Response>? = null
    private lateinit var context: Context

    fun getNews(category_id:String?,Token:String,lang:String, context: Context): LiveData<News_Response> {
        listProductsMutableLiveData = MutableLiveData<News_Response>()
        this.context = context
        news(category_id,Token,lang)
        return listProductsMutableLiveData as MutableLiveData<News_Response>
    }
    private fun news(category_id:String?,Token:String,lang:String) {

        var map= HashMap<String,String>()
        category_id?.let { map.put("category_id", it) }
        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getNews(map,"Bearer "+Token,lang)
        call?.enqueue(object : Callback, retrofit2.Callback<News_Response> {
            override fun onResponse(
                call: Call<News_Response>,
                response: Response<News_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body())

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<News_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }
}