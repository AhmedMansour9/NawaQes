package com.nawaqes.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nawaqes.Adapter.SortsArea_Adapter
import com.nawaqes.Model.Cities_Response
import com.nawaqes.R
import com.nawaqes.ViewModel.States_ViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.sortarea.*
import java.util.*
import androidx.lifecycle.Observer
import com.nawaqes.SharedPrefManager
import com.nawaqes.View.Locationid_View
import kotlinx.android.synthetic.main.activity_areas.*

class Areas : AppCompatActivity(), Locationid_View {
    override fun Areaid(id: String, Name: String) {
        area_id=id
        SharedPrefManager.getInstance(this).saveAreaId(id)
        finish()
    }

    override fun Cat_id(CatId: Int, categories: Int, cat_name: String) {

    }

    override fun CityId(id: String, Name: String) {

    }
    companion object {
        var area_id: String=String()
    }
    lateinit var area_id:String
    lateinit var DeviceLang:String
    lateinit var UserToken: String
    internal lateinit var shared: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_areas)
        area_id=intent.getStringExtra("city_id")
        Language()
         getAllStates(area_id)

    }
    fun getAllStates(Id:String){
        val allCities = ViewModelProvider.NewInstanceFactory().create(States_ViewModel::class.java)
        this.applicationContext?.let {
            allCities.getData( Id,DeviceLang,it).observe(this, Observer<Cities_Response> { loginmodel ->
                if(loginmodel!=null) {

                    val listAdapter = SortsArea_Adapter(this.applicationContext, loginmodel.data)
                    listAdapter.onClick(this)
                    recycler_areas!!.layoutManager = LinearLayoutManager(
                        this.applicationContext,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    recycler_areas!!.setAdapter(listAdapter)


                }
            })
        }
    }

    fun Language() {
        shared = getSharedPreferences("Language", MODE_PRIVATE)
        val Lan = shared.getString("Lann", null)
        if(Lan!=null){
            DeviceLang = Lan
        }else {
            DeviceLang = Locale.getDefault().language

        }    }
}
