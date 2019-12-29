package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.AddRequest_Response
import com.nawaqes.Model.Register_Model
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File
import javax.security.auth.callback.Callback

class AddReques_ViewModel :ViewModel () {

     var listProductsMutableLiveData: MutableLiveData<AddRequest_Response>? = null
    private lateinit var context: Context
    var sub_id:String= String()
    fun getData(
        filename:File?,Descrption: String,CityId:String,StateId:String,CategoryId:String
        ,SubId:String,token:String,
        context: Context
    ): LiveData<AddRequest_Response> {
        listProductsMutableLiveData = MutableLiveData<AddRequest_Response>()
        this.context = context
      getDataValues(filename,Descrption,CityId,StateId,CategoryId,SubId,token)
        return listProductsMutableLiveData as MutableLiveData<AddRequest_Response>
    }

    private fun getDataValues(filename:File?,Descrption: String,CityId:String,StateId:String,CategoryId:String
                              ,SubId:String?,token:String) {

        val requestDescrption=RequestBody.create(MediaType.parse("multipart/form-data"),Descrption)
        val requestCityId=RequestBody.create(MediaType.parse("multipart/form-data"),CityId)
        val requestStateId=RequestBody.create(MediaType.parse("multipart/form-data"),StateId)
        val requestLat=RequestBody.create(MediaType.parse("multipart/form-data"),"0")
        val requestLng=RequestBody.create(MediaType.parse("multipart/form-data"),"0")
        val requestCategoryId=RequestBody.create(MediaType.parse("multipart/form-data"),CategoryId)

        if(SubId.equals("null")){
            if(filename!=null){
                val requestFile=RequestBody.create(MediaType.parse("multipart/form-data"),filename)
                val requestImage=MultipartBody.Part.createFormData("image",filename?.name,requestFile)
                SentRequest(requestImage,requestDescrption,requestCityId,requestStateId,requestLat
                    ,requestLng,requestCategoryId,null,token)
            }else {
                SentRequest(null,requestDescrption,requestCityId,requestStateId,requestLat
                    ,requestLng,requestCategoryId,null,token)
            }
        }else {
            val requestSubId=RequestBody.create(MediaType.parse("multipart/form-data"),SubId)
            if(filename!=null){
                val requestFile=RequestBody.create(MediaType.parse("multipart/form-data"),filename)
                val requestImage=MultipartBody.Part.createFormData("image",filename?.name,requestFile)
                SentRequest(requestImage,requestDescrption,requestCityId,requestStateId,requestLat
                    ,requestLng,requestCategoryId,requestSubId,token)
            }else {
                SentRequest(null,requestDescrption,requestCityId,requestStateId,requestLat
                    ,requestLng,requestCategoryId,requestSubId,token)
            }
        }


    }

    fun SentRequest(requestImage:MultipartBody.Part?,requestDescrption: RequestBody,requestCityId:RequestBody,
                    requestStateId:RequestBody,requestLat:RequestBody,requestLng:RequestBody,requestCategoryId:RequestBody
                    ,requestSubId:RequestBody?,token:String){
        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.AddRequest(requestImage,requestDescrption,requestCityId,requestStateId,requestLat,requestLng,requestCategoryId
            ,requestSubId,"Bearer "+token,"en")
        call?.enqueue(object : Callback, retrofit2.Callback<AddRequest_Response> {
            override fun onResponse(call: Call<AddRequest_Response>, response: Response<AddRequest_Response>) {

                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else  if(response.code()==401){
                    listProductsMutableLiveData?.setValue(null)

                }
            }

            override fun onFailure(call: Call<AddRequest_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })

    }
}