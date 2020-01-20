package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.Categories_Response
import com.nawaqes.Model.Cities_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class Categories_ViewModel :ViewModel() {

    public var listProductsMutableLiveData: MutableLiveData<Categories_Response>? = null
    private lateinit var context: Context


    fun getData(Token:String,lang:String, context: Context): LiveData<Categories_Response> {
        if(listProductsMutableLiveData==null)
        listProductsMutableLiveData = MutableLiveData<Categories_Response>()
        this.context = context
        getDataValues(Token,lang)
        return listProductsMutableLiveData as MutableLiveData<Categories_Response>
    }
    fun searchData(Search:String,Token:String,lang:String, context: Context): LiveData<Categories_Response> {
        listProductsMutableLiveData = MutableLiveData<Categories_Response>()
        this.context = context
        getSearchDataValues(Search,Token,lang)
        return listProductsMutableLiveData as MutableLiveData<Categories_Response>
    }

    private fun getDataValues(Token:String,lang:String) {


        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getCategories("Bearer "+Token,lang)
        call?.enqueue(object : Callback, retrofit2.Callback<Categories_Response> {
            override fun onResponse(
                call: Call<Categories_Response>,
                response: Response<Categories_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<Categories_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }

    private fun getSearchDataValues(Search:String,Token:String,lang:String) {

        var map= HashMap<String,String>()
        map.put("search",Search)
        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getSearchCategories(map,"Bearer "+Token,lang)
        call?.enqueue(object : Callback, retrofit2.Callback<Categories_Response> {
            override fun onResponse(
                call: Call<Categories_Response>,
                response: Response<Categories_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<Categories_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }


}

