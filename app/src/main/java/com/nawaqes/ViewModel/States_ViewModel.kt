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

class States_ViewModel  : ViewModel() {

    public var listProductsMutableLiveData: MutableLiveData<Cities_Response>? = null
    private lateinit var context: Context


    fun getData( City_Id:String,context: Context): LiveData<Cities_Response> {
        listProductsMutableLiveData = MutableLiveData<Cities_Response>()
        this.context = context
        getDataValues(City_Id)
        return listProductsMutableLiveData as MutableLiveData<Cities_Response>
    }

    private fun getDataValues(City_Id:String) {
        var map= HashMap<String,String>()
        map.put("city_id", City_Id)
        var service = ApiClient.getClient()?.create(Service::class.java)
        val call = service?.getStates(map,"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImQyNDBmYWE5YjhhZTY1MGYzMjUwMzE4MmU4ZWFlYTNmODZmYWI2ZDJhZDQ3MDdiMTU1ZDBlYzhiZTVjOGU0ODRlYjkwYjU2ZTllM2RhMTE2In0.eyJhdWQiOiIxIiwianRpIjoiZDI0MGZhYTliOGFlNjUwZjMyNTAzMTgyZThlYWVhM2Y4NmZhYjZkMmFkNDcwN2IxNTVkMGVjOGJlNWM4ZTQ4NGViOTBiNTZlOWUzZGExMTYiLCJpYXQiOjE1NzU5ODA4MzcsIm5iZiI6MTU3NTk4MDgzNywiZXhwIjoxNjA3NjAzMjM3LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.SAI4XvAU1uTHMy_8TslrBvVcpYF-d1bn49afMm1qa9xIGC8AOQsmyjvcG-z6gWqCaeUUQ7teMmBsyB1jtGZnls_rxECONEoIOeFGxrBLiWtrbmTrxNY5-NROsvg9hqoiFXU6ja9YrFJKS1bJK5w_JMFjEyfoc-UmALiJ0ajocMPOGQoppLUHgg_T9cYdZAy0UwSLG-zuVrkxzbJ6X7n72U7aunvmgOFIWVk2lSL7keD2jWyTtNkQVGbpPRE6qEQElGbdw5l41n-mEOWypehEIAwLnm1LtkcbI9GUNSqZJBboku87SGzz88rZ6SL_ahQ7Kap7s38hdfgbP71djh-hJ662_0hzMWMP7KZQVBFcZz8HHueTD027qeVDIvaUtN0Ur1ZMeD4DcS2NMuGcNPn4TQNqFx720RAJQNf5p9sThghTfY5QaZAYNIR4FOYBB_K-_Llp3IV0V_IeIjzhyicShMz7ZQ6vomaNndVVhSzzkfJJefy38fceYRzI4mXq4mkJXmXJ67_mJE9oL2zoT_vdG0LAk2aavmke3M9cUBMio-t8lVjMtZJrv-GcZvSFLA-x8TdW_XEx6EkqWWpHoUIa59WKXd5LqEUFq2C2qilRrOLPVUnTFF3V3WFx74ny3aJnhh0GWMlYfHsQOnnl8AMDhFpJ1ptptIzCG6WhLB423dg")
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

