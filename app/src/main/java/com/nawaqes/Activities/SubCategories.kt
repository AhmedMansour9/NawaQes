package com.nawaqes.Activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nawaqes.Adapter.SubCategories_Adapter
import com.nawaqes.Model.SubCategories_Response
import com.nawaqes.R
import com.nawaqes.ViewModel.SubCategories_ViewMoel
import kotlinx.android.synthetic.main.activity_sub_categories.*
import java.util.*
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nawaqes.Adapter.Categories_Adapter
import com.nawaqes.Model.Categories_Response
import com.nawaqes.View.SubCat_View
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_sub_categories.SwipHome
import kotlinx.android.synthetic.main.activity_sub_categories.recycler_Categroies

class SubCategories : AppCompatActivity() , SwipeRefreshLayout.OnRefreshListener, SubCat_View {

    internal lateinit var shared: SharedPreferences

    var subCategoris:Int = 0
    lateinit var DeviceLang:String
    lateinit var UserToken: String
    private lateinit var DataSaver: SharedPreferences
    lateinit var subCategories: SubCategories_ViewMoel
    lateinit var Cat_Name:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_categories)
        Language()
        init()
        getData()
        SwipRefresh()
        Search()
        SearchKeyBoard()
        EditSearchChanger()
        
    }
    private fun SearchKeyBoard() {
        E_Search_sub.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                doSearchWork()
            }
            true
        }
    }

    private fun doSearchWork() {
        if(!E_Search_sub.text.toString().isEmpty())
            SwipHome.isRefreshing=true
        SearchResult()
    }
    private  fun EditSearchChanger(){
        E_Search_sub.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(s.isEmpty()){
                    getSubCategories()
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

    fun SearchResult(){
        this.applicationContext?.let {
            subCategories.SearchSubCategories(subCategoris.toString(),E_Search_sub.text.toString(),UserToken,DeviceLang, it).observe(this, Observer<SubCategories_Response> { loginmodel ->
                SwipHome.isRefreshing=false

                if(loginmodel!=null) {
                    val listAdapter =
                        SubCategories_Adapter(applicationContext, loginmodel.data)
//                    listAdapter.onClick(this)
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
            getSubCategories()

        })
    }
    private fun Search() {
        img_search_sub.setOnClickListener(){
         doSearchWork()
        }
    }

    private fun getSubCategories() {
        this.applicationContext?.let {
            subCategories.getSubCategories(subCategoris.toString(),UserToken,DeviceLang, it).observe(this,
                Observer<SubCategories_Response> { loginmodel ->
                SwipHome.isRefreshing=false

                if(loginmodel!=null) {
                    val listAdapter =
                        SubCategories_Adapter(applicationContext, loginmodel.data)
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
        DataSaver = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        UserToken = DataSaver.getString("token", null)!!
        subCategories= ViewModelProvider.NewInstanceFactory().create(
            SubCategories_ViewMoel::class.java)
    }

    private fun getData() {
        subCategoris =intent.getIntExtra("cat_id",0)
        Cat_Name=intent.getStringExtra("cat_name")!!

    }

    fun Language() {
        shared = getSharedPreferences("Language", MODE_PRIVATE)
        val Lan = shared.getString("Lann", null)
        if(Lan!=null){
            DeviceLang = Lan
        }else {
            DeviceLang = Locale.getDefault().language

        }    }
    override fun onRefresh() {
        getSubCategories()
    }

    override fun SubId(Id: Int, name: String) {
        val intent = Intent(this, Details_Product::class.java)

        intent.putExtra("cat_id",subCategoris)
        intent.putExtra("cat_name",Cat_Name)
        intent.putExtra("sub_id",Id.toString())
        intent.putExtra("sub_name",name)
        startActivity(intent)

    }


}
