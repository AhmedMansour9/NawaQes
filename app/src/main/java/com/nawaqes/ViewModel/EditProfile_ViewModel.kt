package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.EditProfile_Response
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

class EditProfile_ViewModel :ViewModel() {

    var listProductsMutableLiveData: MutableLiveData<EditProfile_Response>? = null
    private lateinit var context: Context
    fun getData(
        filename: File?, email: String, phone:String,full_name:String, token:String,
        context: Context
    ): LiveData<EditProfile_Response> {
        listProductsMutableLiveData = MutableLiveData<EditProfile_Response>()
        this.context = context
        getDataValues(filename,email,phone,full_name,token)
        return listProductsMutableLiveData as MutableLiveData<EditProfile_Response>
    }

    private fun getDataValues(filename: File?, email: String, phone:String,full_name:String, token:String) {

        val requestDEmail= RequestBody.create(MediaType.parse("multipart/form-data"),email)
        val requestPhone= RequestBody.create(MediaType.parse("multipart/form-data"),phone)
        val requestName= RequestBody.create(MediaType.parse("multipart/form-data"),full_name)

        if(filename!=null){
            val requestFile= RequestBody.create(MediaType.parse("multipart/form-data"),filename)
            val requestImage=
                MultipartBody.Part.createFormData("image",filename?.name,requestFile)
            SentRequest(requestImage,requestDEmail,requestPhone,requestName,token)
        }else {
            SentRequest(null,requestDEmail,requestPhone,requestName,token)
        }


    }

    fun SentRequest(requestImage: MultipartBody.Part?, requestEmail: RequestBody, requestPhone: RequestBody, requestName: RequestBody, token:String){
        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.EditProfile(requestImage,requestEmail, requestPhone,requestName,"Bearer "+token)
        call?.enqueue(object : Callback, retrofit2.Callback<EditProfile_Response> {
            override fun onResponse(call: Call<EditProfile_Response>, response: Response<EditProfile_Response>) {

                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else  if(response.code()==401){
                    listProductsMutableLiveData?.setValue(null)

                }
            }

            override fun onFailure(call: Call<EditProfile_Response>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })

    }
}