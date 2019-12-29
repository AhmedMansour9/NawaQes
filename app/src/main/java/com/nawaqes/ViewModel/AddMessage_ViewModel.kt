package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.AddRequest_Response
import com.nawaqes.Model.SentMessage_Response
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import javax.security.auth.callback.Callback

class AddMessage_ViewModel :ViewModel() {

    var listProductsMutableLiveData: MutableLiveData<SentMessage_Response>? = null
    private lateinit var context: Context
    fun getData(
        filename: File?, Descrption: String, CityId:String, token:String,
        context: Context
    ): LiveData<SentMessage_Response> {
        listProductsMutableLiveData = MutableLiveData<SentMessage_Response>()
        this.context = context
        getDataValues(filename,Descrption,CityId,token)
        return listProductsMutableLiveData as MutableLiveData<SentMessage_Response>
    }

    private fun getDataValues(filename: File?, Descrption: String,id:String, token:String) {

        val requestDescrption= RequestBody.create(MediaType.parse("multipart/form-data"),Descrption)
        val requestId= RequestBody.create(MediaType.parse("multipart/form-data"),id)
            if(filename!=null){
                val requestFile= RequestBody.create(MediaType.parse("multipart/form-data"),filename)
                val requestImage=
                    MultipartBody.Part.createFormData("image",filename?.name,requestFile)
                SentRequest(requestImage,requestDescrption,requestId,token)
            }else {
                SentRequest(null,requestDescrption,requestId,token)
            }


    }

    fun SentRequest(requestImage: MultipartBody.Part?, requestDescrption: RequestBody,requestId:RequestBody, token:String){
        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.SentMessage(requestImage,requestDescrption, requestId,"Bearer "+token)
        call?.enqueue(object : Callback, retrofit2.Callback<SentMessage_Response> {
            override fun onResponse(call: Call<SentMessage_Response>, response: Response<SentMessage_Response>) {

                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else  if(response.code()==401){
                    listProductsMutableLiveData?.setValue(null)

                }
            }

            override fun onFailure(call: Call<SentMessage_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })

    }
}