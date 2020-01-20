package com.nawaqes.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nawaqes.Adapter.*
import com.nawaqes.Model.*
import com.nawaqes.R
import com.nawaqes.View.Locationid_View
import com.nawaqes.View.SubCat_View
import com.nawaqes.ViewModel.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.SwipHome
import kotlinx.android.synthetic.main.activity_sub_categories.*
import kotlinx.android.synthetic.main.activity_sub_categories.recycler_Categroies
import kotlinx.android.synthetic.main.buttomsheet_profile.*
import java.util.*

class Profile : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener , Locationid_View {

    lateinit var requests:Shortlists_Requests_ViewModel
    lateinit var offers:Offers_ViewModel
    lateinit var profile:Profile_ViewModel
    lateinit var UserToken: String
    private lateinit var DataSaver: SharedPreferences
    lateinit var categories: Categories_ViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
    var  sheetdirec:RelativeLayout?=null
    lateinit var DeviceLang:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        Language()
        init()
        Selected_Tabs()
        SwipRefresh()
        openSort()
        openEditProfile()
    }

    private fun openEditProfile() {
        Img_Setting.setOnClickListener(){
            val intent = Intent(this, Edit_Profile::class.java)
            startActivity(intent)
        }
    }

    private fun showInfo() {
        this.applicationContext?.let {
            profile.getData(UserToken,"en", it).observe(this,
                Observer<Profile_Response> { loginmodel ->
                    SwipHome.isRefreshing=false
                    if(loginmodel!=null) {
                        Title.text=loginmodel.data.fullName
                        Email.text=loginmodel.data.email
                        Glide.with(this)
                            .load(loginmodel.data.image_path)
                            .error(R.drawable.emptyprofile)
                            .into(Img_Profile)

                    }
                })
        }

    }

    private fun init() {
        DataSaver = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        UserToken = DataSaver.getString("token", null)!!
        sheetdirec=findViewById(R.id.sheetdirecprofile)
        bottomSheetBehavior = BottomSheetBehavior.from(sheetdirec)
        requests= ViewModelProvider.NewInstanceFactory().create(
            Shortlists_Requests_ViewModel::class.java)
        offers= ViewModelProvider.NewInstanceFactory().create(
            Offers_ViewModel::class.java)
        profile= ViewModelProvider.NewInstanceFactory().create(
            Profile_ViewModel::class.java)
        categories= ViewModelProvider.NewInstanceFactory().create(
            Categories_ViewModel::class.java)

    }
    fun openSort() {
        img_filter.setOnClickListener() {

            if (bottomSheetBehavior.state== BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                Frame_AlphaProf.visibility=View.VISIBLE
            } else {
                Frame_AlphaProf.visibility=View.GONE
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
            }
        }
    }

    fun SwipRefresh(){
        SwipHome.setOnRefreshListener(this)
        SwipHome.isRefreshing=true
        SwipHome.isEnabled = true
        SwipHome.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )
        SwipHome.post(Runnable {
            getRequests()
            showInfo()
           getOffers()
            getCategories()

        })
    }

    private fun getCategories() {

        this.applicationContext?.let {
            categories.getData(UserToken,DeviceLang, it).observe(this, Observer<Categories_Response> { loginmodel ->
                SwipHome.isRefreshing=false

                if(loginmodel!=null) {
                    val listAdapter =
                        CategoriesSelected_Adapter(applicationContext, loginmodel.data)
                    listAdapter.onClick(this)
                    recycler_Sorts.layoutManager = LinearLayoutManager(
                        applicationContext,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    recycler_Sorts.setAdapter(listAdapter)
                }

            })
        }
    }

    private fun getRequests() {
        this.applicationContext?.let {
            requests.Requests(UserToken,"en", it).observe(this,
                Observer<Requests_Shortlists_Response> { loginmodel ->
                    SwipHome.isRefreshing=false

                    if(loginmodel!=null) {
                        val listAdapter =
                            MyRequests_Adapter(applicationContext, loginmodel.data)
//                        listAdapter.onClick(this)
                        recycler_Requests.layoutManager = LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        recycler_Requests.setAdapter(listAdapter)
                    }

                })
        }
    }

    private fun getOffers() {
        this.applicationContext?.let {
            offers.getOffersCategories(null,UserToken,"en", it).observe(this,
                Observer<Offers_Response> { loginmodel ->
                    SwipHome.isRefreshing=false

                    if(loginmodel!=null) {
                        val listAdapter =
                            Offers_Adapter(applicationContext, loginmodel.data)
//                        listAdapter.onClick(this)
                        recycler_offfers.layoutManager = LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        recycler_offfers.setAdapter(listAdapter)
                    }

                })
        }
    }

    fun Selected_Tabs(){
        T_MyRequests.setOnClickListener(){
            img_filter.visibility=View.GONE
           onClickRequests()
        }
        T_Offers.setOnClickListener(){
            img_filter.visibility=View.VISIBLE
            onClickOffers()
        }
    }
    fun onClickRequests(){
        T_MyRequests.setTextColor(Color.parseColor("#ffffff"))
        T_MyRequests.setBackgroundResource(R.drawable.bc_select_search)
//            T_MyRequests.setBackgroundColor(Color.TRANSPARENT)
        T_Offers.setTextColor(Color.parseColor("#848586"))
        T_Offers.setBackgroundColor(Color.TRANSPARENT)
        recycler_Requests.visibility= View.VISIBLE
        recycler_offfers.visibility=View.GONE
    }
    fun onClickOffers(){
        T_MyRequests.setTextColor(Color.parseColor("#848586"))
        T_MyRequests.setBackgroundColor(Color.TRANSPARENT)
        T_Offers.setTextColor(Color.parseColor("#ffffff"))
        T_Offers.setBackgroundResource(R.drawable.bc_select_search)
        recycler_Requests.visibility= View.GONE
        recycler_offfers.visibility=View.VISIBLE
    }
    override fun onRefresh() {
     getRequests()
        showInfo()
        getOffers()
    }

    fun Language() {
        DeviceLang = Locale.getDefault().language
    }
    override fun CityId(id: String, Name: String) {

    }

    override fun Areaid(id: String, Name: String) {
        onClickOffers()
        recycler_offfers.visibility=View.GONE
        Frame_AlphaProf.visibility=View.GONE
        SwipHome.isRefreshing=true
        this.applicationContext?.let {
            offers.getOffersCategories(id,UserToken,"en", it).observe(this,
                Observer<Offers_Response> { loginmodel ->
                    recycler_offfers.visibility=View.VISIBLE
                    SwipHome.isRefreshing=false
                    if(loginmodel!=null) {
                        val listAdapter =
                            Offers_Adapter(applicationContext, loginmodel.data)
                        listAdapter.onClick(this)
                        recycler_offfers.layoutManager = LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        recycler_offfers.setAdapter(listAdapter)
                    }

                })
        }
    }

    override fun Cat_id(CatId: Int, categories: Int, cat_name: String) {

    }

    override fun onResume() {
        super.onResume()
        showInfo()
    }
//    override fun onStop() {
//        super.onStop()
//        Btn_login.isEnabled=true
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Btn_login.isEnabled=true
//    }

}
