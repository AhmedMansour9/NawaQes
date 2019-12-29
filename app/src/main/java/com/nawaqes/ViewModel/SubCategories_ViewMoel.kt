package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.Categories_Response
import com.nawaqes.Model.SubCategories_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class SubCategories_ViewMoel :ViewModel() {

     var listProductsMutableLiveData: MutableLiveData<SubCategories_Response>? = null
    private lateinit var context: Context

    fun getSubCategories(category_id:String,Token:String,lang:String, context: Context): LiveData<SubCategories_Response> {
        listProductsMutableLiveData = MutableLiveData<SubCategories_Response>()
        this.context = context
        getSubCategoriesDataValues(category_id,Token,lang)
        return listProductsMutableLiveData as MutableLiveData<SubCategories_Response>
    }

    fun SearchSubCategories(category_id:String,searchSubCategory:String,Token:String,lang:String, context: Context): LiveData<SubCategories_Response> {
        listProductsMutableLiveData = MutableLiveData<SubCategories_Response>()
        this.context = context
        getSearchSubCategories(category_id,searchSubCategory,Token,lang)
        return listProductsMutableLiveData as MutableLiveData<SubCategories_Response>
    }

    private fun getSubCategoriesDataValues(category_id:String,Token:String,lang:String) {

        var map= HashMap<String,String>()
        map.put("category_id",category_id)
        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getSubCategories(map,"Bearer "+Token,lang)
        call?.enqueue(object : Callback, retrofit2.Callback<SubCategories_Response> {
            override fun onResponse(
                call: Call<SubCategories_Response>,
                response: Response<SubCategories_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<SubCategories_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }
    private fun getSearchSubCategories(category_id:String,searchSubCategory:String,Token:String,lang:String) {

        var map= HashMap<String,String>()
        map.put("category_id",category_id)
        map.put("search",searchSubCategory)
        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.SearchSubCategories(map,"Bearer "+Token,lang)
        call?.enqueue(object : Callback, retrofit2.Callback<SubCategories_Response> {
            override fun onResponse(
                call: Call<SubCategories_Response>,
                response: Response<SubCategories_Response>
            ) {
                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<SubCategories_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }


}