package com.nawaqes.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.nawaqes.Adapter.SortsLocation_Adapter
import com.nawaqes.R
import kotlinx.android.synthetic.main.activity_home.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.arturogutierrez.Badges
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.nawaqes.Adapter.Categories_Adapter
import com.nawaqes.Adapter.Cities_Adapter
import com.nawaqes.Adapter.SortsArea_Adapter
import com.nawaqes.BadgeIntentService
import com.nawaqes.Model.Categories_Response
import com.nawaqes.Model.Cities_Response
import com.nawaqes.Model.CountNotifications_Response
import com.nawaqes.Model.SentMessage_Response
import com.nawaqes.SharedPrefManager
import com.nawaqes.View.Locationid_View
import com.nawaqes.ViewModel.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.sortarea.*
import me.leolin.shortcutbadger.ShortcutBadger
import java.util.*


class Home : AppCompatActivity(), Locationid_View, SwipeRefreshLayout.OnRefreshListener {



    private lateinit var bottomSheetBehavior: BottomSheetBehavior<RelativeLayout>
    private lateinit var bottomSheetAreaBehavior: BottomSheetBehavior<RelativeLayout>
    var  sheetdirec:RelativeLayout?=null
    var  sheetdArea:RelativeLayout?=null
    internal lateinit var shared: SharedPreferences
     var tokenfirebae:String?=String()
    lateinit var T_Cancel: TextView
    var recycler_Sort: RecyclerView?=null
    lateinit var DeviceLang:String
    lateinit var UserToken: String
    var Location_Status=false
    lateinit var listCities: ArrayAdapter<Cities_Response.Data>
    companion object {
         var CityId:String?=null
         var StateId: String?=null
    }

    private lateinit var DataSaver: SharedPreferences
    lateinit var categories:Categories_ViewModel
    lateinit var notifications: CountNotifications_ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shared = getSharedPreferences("Language", MODE_PRIVATE)
        val Lan = shared.getString("Lann", null)
        if (Lan != null) {
            val locale = Locale(Lan!!)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            baseContext.resources.updateConfiguration(
                config,
                baseContext.resources.displayMetrics
            )
        }
        setContentView(R.layout.activity_home)
        Language()
        init()
        getTokenFirebase()
        getCategories()
        SwipRefresh()
        openProfile()
        Search()
        openMessages()
        SentToken()
        SearchKeyBoard()
        EditSearchChanger()
//        Badges.setBadge(this, 5);
//        startService(
//                     Intent(applicationContext, BadgeIntentService::class.java).putExtra("badgeCount", 10)
//                );

    }

    private fun SearchKeyBoard() {
        E_Search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                doSearchWork()
            }
            true
        }
    }
    private  fun EditSearchChanger(){
        E_Search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
               if(s.isEmpty()){
                   getCategories()
               }else {
                   SearchResult()
               }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {


            }
        })

    }

    private fun doSearchWork() {
        if(!E_Search.text.toString().isEmpty()){
            SearchResult()
        }
    }
    private fun SearchResult(){
        SwipHome.isRefreshing=true
        this.applicationContext?.let {
            categories.searchData(E_Search.text.toString(), UserToken, DeviceLang, it)
                .observe(this, Observer<Categories_Response> { loginmodel ->
                    SwipHome.isRefreshing = false
                    if (loginmodel != null) {
                        val listAdapter =
                            Categories_Adapter(applicationContext, loginmodel.data)
                        recycler_Categroies.layoutManager = LinearLayoutManager(
                            applicationContext,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        recycler_Categroies.setAdapter(listAdapter)
                    }
                })
        }
    }

    private fun getTokenFirebase() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val tokens = task.result?.token

                SharedPrefManager.getInstance(applicationContext).saveDeviceToken(tokens)
            })
    }

    private fun SentToken() {
        val tokenn = ViewModelProvider.NewInstanceFactory().create(SentToken_ViewModel::class.java)
        this.applicationContext?.let {
            tokenn.Requests(tokenfirebae,UserToken, it)?.observe(this, Observer<SentMessage_Response> { loginmodel ->
                if(loginmodel!=null) {

                }
            })
        }
    }

    private fun openMessages() {
        Img_Msg.setOnClickListener(){
            val intent = Intent(this, Messages::class.java)
            startActivity(intent)

        }
    }

    private fun Search() {
        img_search.setOnClickListener(){
           doSearchWork()
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
            getAllCities()
            getCategories()
            getNewNotification()
            getNewMessage()
        })
    }

    fun getAllCities(){

        val allCities = ViewModelProvider.NewInstanceFactory().create(Cities_ViewModel::class.java)
        this.applicationContext?.let {
            allCities.getData( DeviceLang,it).observe(this, Observer<Cities_Response> { loginmodel ->
                SwipHome.isRefreshing=false
                if(loginmodel!=null) {
                    val itemList:MutableList<Cities_Response.Data> = ArrayList(loginmodel.data)
                    var dat: Cities_Response.Data =Cities_Response.Data(0,resources.getString(R.string.selectcity))
                    itemList.add(0,dat)
                    listCities= ArrayAdapter(this,R.layout.textcolorspinner,itemList)
                    listCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    T_Cities.setAdapter(listCities)
                    T_Cities.setSelection(0, true);
                    var v:View  = T_Cities.getSelectedView()
                    (v as TextView).setTextColor(Color.parseColor("#ffffff"))
                    T_Cities.setOnItemSelectedListener(object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View,
                            y: Int,
                            l: Long
                        ) {
                            T_Cities.setSelection(y, true);
                            var v:View  = T_Cities.getSelectedView()
                            (v as TextView).setTextColor(Color.parseColor("#ffffff"))
                            var City = T_Cities.getSelectedItem().toString()
                            if(!City.equals(resources.getString(R.string.selectcity))) {
                                var i = 0
                                var City = T_Cities.getSelectedItem().toString()
                                while (i < itemList!!.size) {
                                    if (itemList!!.get(i).name.equals(City)) {
                                        SharedPrefManager.getInstance(applicationContext).saveAreaId(null)
                                        CityId = itemList!!.get(i).id.toString()
                                        val intent = Intent(applicationContext, Areas::class.java)
                                        intent.putExtra("city_id", CityId)
                                        startActivity(intent)
                                    }
                                    i++
                                }
                            }else {
                                CityId=null
                            }
                        }
                        override fun onNothingSelected(adapterView: AdapterView<*>) {
                        }
                    })
                }
            })
        }
    }

    fun getAllStates(Id:String){
        val allCities = ViewModelProvider.NewInstanceFactory().create(States_ViewModel::class.java)
        this.applicationContext?.let {
            allCities.getData( Id,DeviceLang,it).observe(this, Observer<Cities_Response> { loginmodel ->
                SwipHome.isRefreshing=false
                if(loginmodel!=null) {
                    bottomSheetAreaBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
                    bottomSheetAreaBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                    Frame_Alpha.visibility=View.VISIBLE
                    val listAdapter = SortsArea_Adapter(this.applicationContext, loginmodel.data)
                    listAdapter.onClick(this)
                    recycler_Area!!.layoutManager = LinearLayoutManager(
                        this.applicationContext,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    recycler_Area!!.setAdapter(listAdapter)
                }
            })
        }
    }
    private fun getNewNotification() {
        this.applicationContext?.let {
            notifications= ViewModelProvider.NewInstanceFactory().create(
                CountNotifications_ViewModel::class.java)
            notifications.getNewNotifications(UserToken, it).observe(this, Observer<CountNotifications_Response> { loginmodel ->
                if(loginmodel!=null) {
                    if(loginmodel.data!=0)
                    T_Notifications.visibility=View.VISIBLE
                T_Notifications.text=loginmodel.data.toString()
                }else {
                    T_Notifications.visibility=View.GONE

                }
            })
        }
    }
    private fun getNewMessage() {
        this.applicationContext?.let {
            notifications= ViewModelProvider.NewInstanceFactory().create(
                CountNotifications_ViewModel::class.java)
            notifications.getData(UserToken, it).observe(this, Observer<CountNotifications_Response> { loginmodel ->
                if(loginmodel!=null) {
                    if(loginmodel.data!=0)
                T_Messages.visibility=View.VISIBLE
                T_Messages.text=loginmodel.data.toString()
                }else {
                    T_Messages.visibility=View.GONE

                }

            })
        }
    }

    private fun getCategories() {
        this.applicationContext?.let {
            categories.getData(UserToken,DeviceLang, it).observe(this, Observer<Categories_Response> { loginmodel ->
                SwipHome.isRefreshing=false
                if(loginmodel!=null) {
                    val listAdapter =
                        Categories_Adapter(applicationContext, loginmodel.data)
                     listAdapter.onClick(this)
                    recycler_Categroies.layoutManager = LinearLayoutManager(
                        applicationContext,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    recycler_Categroies.setAdapter(listAdapter)
                }

            })
        }
    }

    private fun init() {
        T_Cancel=findViewById(R.id.T_Cancel)
        recycler_Sort=findViewById(R.id.recycler_Sorts);
        DataSaver = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        tokenfirebae=SharedPrefManager.getInstance(this).deviceToken
//        Toast.makeText(this,tokenfirebae,Toast.LENGTH_LONG).show()

        UserToken = DataSaver.getString("token", null)!!
        categories= ViewModelProvider.NewInstanceFactory().create(
            Categories_ViewModel::class.java)

    }
    fun openProfile(){
        img_profile.setOnClickListener(){
            img_profile.isEnabled=false
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
    }


//    fun ButtonSheet() {
//        sheetdirec=findViewById(R.id.sheetdirec)
//        sheetdArea=findViewById(R.id.sheetArea)
//        bottomSheetBehavior = BottomSheetBehavior.from(sheetdirec)
//        bottomSheetAreaBehavior=BottomSheetBehavior.from(sheetdArea)
//    }

//    fun cancelSort(){
//        T_Cancel.setOnClickListener(){
//            Frame_Alpha.visibility=View.GONE
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
//        }
//    }
//    fun cancelArea(){
//        T_CancelArea.setOnClickListener(){
//            Frame_Alpha.visibility=View.GONE
//            bottomSheetAreaBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
//        }
//    }


    fun Language() {
        shared = getSharedPreferences("Language", MODE_PRIVATE)
        val Lan = shared.getString("Lann", null)
        if(Lan!=null){
            DeviceLang = Lan
        }else {
            DeviceLang = Locale.getDefault().language

        }    }

    override fun Areaid(id: String,Name:String) {
        StateId=id
//        T_Area.text=Name
//        Frame_Alpha.visibility=View.GONE
//
    }

    override fun CityId(id: String,Name:String) {
        Location_Status=true
        CityId=id
//        T_Location.text=Name
        getAllStates(id)
//        Frame_Alpha.visibility=View.GONE
        SwipHome.isRefreshing=true

    }

    override fun onRefresh() {
        getAllCities()
        getCategories()
        getNewNotification()
        getNewMessage()
    }
    override fun Cat_id(categorieid: Int,id:Int,cat_name:String) {
        var areaaid:String?= SharedPrefManager.getInstance(this).areaId

        if(CityId!=null&& areaaid!=null){
            if(id==0){
                val intent = Intent(this, Details_Product::class.java)
                intent.putExtra("cat_id",categorieid)
                intent.putExtra("cat_name",cat_name)
                startActivity(intent)

            }else {
                val intent = Intent(this, SubCategories::class.java)
                intent.putExtra("cat_id",categorieid)
                intent.putExtra("cat_name",cat_name)
                startActivity(intent)
            }
        }else {
            Toast.makeText(this,"Please Make Sure that you select City and Area",Toast.LENGTH_LONG).show()
        }

    }
    override fun onStop() {
        super.onStop()
        img_profile.isEnabled=true
    }

    override fun onPause() {
        super.onPause()
        img_profile.isEnabled=true
    }

    override fun onResume() {
        super.onResume()
        getNewNotification()
        getNewMessage()

    }

}
