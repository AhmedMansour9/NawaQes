package com.nawaqes.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nawaqes.Model.Register_Model
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback
import android.widget.Toast
import android.R.string
import android.os.Build
import androidx.annotation.RequiresApi
import com.nawaqes.Retrofit.ApiClient
import com.nawaqes.Retrofit.Service
import org.json.JSONObject
import org.json.JSONArray





class Register_ViewModel : ViewModel() {

    public var listProductsMutableLiveData: MutableLiveData<Register_Model>? = null
    private lateinit var context: Context
    private var Wron_Email: Boolean = false

    companion object {

      var LastPhone: String? = ""
        var LastEmail: String? = ""
    }
    fun getStatus():Boolean{
        return Wron_Email
    }

     fun getData(
        full_name:String,
        Email: String,
        Password:String,
        Phone:String,
        city_id:String,
        state_id:String,
        context: Context
    ): LiveData<Register_Model> {
        listProductsMutableLiveData = MutableLiveData<Register_Model>()
        this.context = context
        getDataValues(full_name,Email,Password,Phone,city_id,state_id)
        return listProductsMutableLiveData as MutableLiveData<Register_Model>
    }
    public fun getLogin(
        Email: String,
        Password:String,
        context: Context
    ): LiveData<Register_Model> {
        listProductsMutableLiveData = MutableLiveData<Register_Model>()
        this.context = context
        getLoginValues(Email,Password)
        return listProductsMutableLiveData as MutableLiveData<Register_Model>

    }
    public fun getLoginFacebook(
        id: String?,
        email:String?,
        name:String?,
        context: Context
    ): LiveData<Register_Model> {
        listProductsMutableLiveData = MutableLiveData<Register_Model>()
        this.context = context
        getface(id,email,name)
        return listProductsMutableLiveData as MutableLiveData<Register_Model>

    }



    private fun getDataValues(full_name:String,Email: String,Password:String,Phone:String,city_id:String
                              ,state_id:String) {
      var map= HashMap<String,String>()
        map.put("full_name",full_name)
        map.put("email",Email)
        map.put("password",Password)
        map.put("phone",Phone)
        map.put("city_id",city_id)
        map.put("state_id",state_id)
        map.put("password_confirmation",Password)
        map.put("lat","0")
        map.put("lng","0")

        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.userRegister(map)
        call?.enqueue(object : Callback, retrofit2.Callback<Register_Model> {
            override fun onResponse(call: Call<Register_Model>, response: Response<Register_Model>) {

                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else  if(response.code()==401){
                    Wron_Email=true
                        var jObjError = JSONObject(response.errorBody()!!.string())
                        var jOsonData = jObjError.getJSONObject("data")
                    if(let { jOsonData.has("phone")}){
                        LastPhone=jOsonData.getJSONArray("phone").get(0).toString()
                    }
                    if(let { jOsonData.has("email")}){
                        LastEmail=jOsonData.getJSONArray("email").get(0).toString()
                    }
                         listProductsMutableLiveData?.setValue(null)

                }
            }

            override fun onFailure(call: Call<Register_Model>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }
    private fun getLoginValues(Email: String,Password:String) {
        var map= HashMap<String,String>()
        map.put("email",Email)
        map.put("password",Password)


        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.userLogin(map)


        call?.enqueue(object : Callback, retrofit2.Callback<Register_Model> {
            override fun onResponse(call: Call<Register_Model>, response: Response<Register_Model>) {

                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    Wron_Email=true
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<Register_Model>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }
    private fun getface(social_id: String?,email:String?,full_name:String?) {
        var map= HashMap<String,String>()
        map.put("social_id",social_id!!)
        map.put("email",email!!)
        map.put("full_name",full_name!!)


        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.userLoginFacebook(map)


        call?.enqueue(object : Callback, retrofit2.Callback<Register_Model> {
            override fun onResponse(call: Call<Register_Model>, response: Response<Register_Model>) {

                if (response.code() == 200) {
                    listProductsMutableLiveData?.setValue(response.body()!!)

                } else {
                    Wron_Email=true
                    listProductsMutableLiveData?.setValue(null)
                }
            }

            override fun onFailure(call: Call<Register_Model>, t: Throwable) {
                listProductsMutableLiveData?.setValue(null)

            }
        })
    }
}