package com.nawaqes.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.nawaqes.Adapter.Cities_Adapter
import com.nawaqes.Model.Cities_Response
import com.nawaqes.Model.Register_Model
import com.nawaqes.R
import com.nawaqes.ViewModel.Cities_ViewModel
import com.nawaqes.ViewModel.Register_ViewModel
import com.nawaqes.ViewModel.States_ViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class Register : AppCompatActivity() {
     var City_Id:String=""
     var State_Id:String=""
    internal lateinit var shared: SharedPreferences
    lateinit var DeviceLang:String

    val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
      private val PASSWORD_PATTERN = Pattern.compile(
          "^" +//"(?=.*[0-9])" + //at least 1 digit
     //"(?=.*[a-z])" +         //at least 1 lower case letter
                        //"(?=.*[A-Z])" +         //at least 1 upper case letter
     //                    "(?=.*[a-zA-Z])" +      //any letter
     //                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(?=\\S+$)" +           //no white spaces
    ".{5,}"                //at least 4 characters
)
    private lateinit var dataSaver: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        dataSaver = PreferenceManager.getDefaultSharedPreferences(this);
        Language()
        getAllCities()
        EditText_Changer(text_input_name_register.editText!!)
        EditText_Changer(text_input_phone_register.editText!!)
        EditText_Changer(text_input_email_register.editText!!)
        EditText_Changer(text_input_password_register.editText!!)
        openCompany()
        openShop()

    }
    private fun openShop() {
        Btn_shopregister.setOnClickListener(){
            val intent = Intent(this, Webview::class.java)
            intent.putExtra("type","http://nwqis.com/en/customer/showRegisterFormShop")
            startActivity(intent)
            finish()
        }
    }

    private fun openCompany() {
        Btn_companyregister.setOnClickListener(){
            val intent = Intent(this, Webview::class.java)
            intent.putExtra("type","http://nwqis.com/en/customer/showRegisterFormCompany")
            startActivity(intent)
            finish()
        }
    }



    fun getAllCities(){
        val allCities = ViewModelProvider.NewInstanceFactory().create(Cities_ViewModel::class.java)
        this.applicationContext?.let {
            allCities.getData( DeviceLang,it).observe(this, Observer<Cities_Response> { loginmodel ->
                if(loginmodel!=null) {
                    val itemList:MutableList<Cities_Response.Data> = ArrayList(loginmodel.data)
                     var dat: Cities_Response.Data =Cities_Response.Data(0,resources.getString(R.string.city))
                    itemList.add(dat)
                    val customAdapter = Cities_Adapter(this.applicationContext, itemList)
                    S_Area.setPrompt(resources.getString(R.string.city));
                    S_Area.setAdapter(customAdapter)
                    S_Area.setSelection(customAdapter.getCount());
                    S_Area.setOnItemSelectedListener(object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View,
                            i: Int,
                            l: Long
                        ) {
                            var City = S_Area.getSelectedItem().toString()
                            if(!City.equals(resources.getString(R.string.selectlocation))) {

                                var s = 0
                                while (s < itemList.size) {
                                    if (itemList.get(s).name.equals(City)) {
                                        City_Id = itemList.get(s).id.toString()
                                    }
                                    s++
                                }
                                getAllStates(City_Id)
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
                if(loginmodel!=null) {
                    S_Location.visibility=View.VISIBLE
                    area.visibility=View.VISIBLE
                    val itemList:MutableList<Cities_Response.Data> = ArrayList(loginmodel.data)
                    var dat: Cities_Response.Data =Cities_Response.Data(0,resources.getString(R.string.selectarea))
                    itemList.add(dat)
                    val customAdapter = Cities_Adapter(this.applicationContext, itemList)

                    S_Location.setAdapter(customAdapter)
                    S_Location.setSelection(customAdapter.getCount());
                    S_Location.setOnItemSelectedListener(object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View,
                            s: Int,
                            l: Long
                        ) {

                            var City = S_Location.getSelectedItem().toString()
                            if(!City.equals(resources.getString(R.string.selectarea))) {
                                var i = 0
                                while (i < itemList.size) {
                                    if (itemList.get(i).name.equals(City)) {
                                        State_Id = itemList.get(i).id.toString()
                                    }
                                    i++
                                }
                            }
                        }

                        override fun onNothingSelected(adapterView: AdapterView<*>) {

                        }
                    })


                }
            })
        }
    }

    fun Btn_Register(view: View) {
        if (!ValidateEmailRegister() or !ValidatePasswordRegister() or !ValidatePhoneRegister() or !ValidateRegister()) {
            return
        }

//        if(isConnected){
//                if(State_Id.equals("")&&City_Id.equals("")){
//                    Toast.makeText(this,resources.getString(R.string.validcityamdarea),Toast.LENGTH_LONG).show()
//                    return
//                }
        if(City_Id.equals("")){
            Toast.makeText(this,resources.getString(R.string.validcity),Toast.LENGTH_LONG).show()
            return
        }
        else if(State_Id.equals("")){
            Toast.makeText(this,resources.getString(R.string.validarea),Toast.LENGTH_LONG).show()
                return
        }
       else {

            var RegisterViewModel =
                ViewModelProvider.NewInstanceFactory().create(Register_ViewModel::class.java)
            progressBarLogin.visibility = View.VISIBLE
            view.isEnabled = false
            view.hideKeyboard()
            RegisterViewModel.getData(
                E_Full_Name.text.toString(),
                E_Email_Register.text.toString(),
                E_Password_Register.text.toString(),
                E_Phone.text.toString(),
                City_Id,
                State_Id,
                applicationContext
            ).observe(this,
                Observer<Register_Model> { loginmodel ->
                    view.isEnabled = true
                    progressBarLogin.visibility = View.GONE
                    if (loginmodel != null) {
                        val customer_id = loginmodel.accessToken
                        dataSaver.edit().putString("token", customer_id).apply()
                        val intent = Intent(this, Home::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        val status: Boolean = RegisterViewModel.getStatus()
                        if (status == true) {
                            if (Register_ViewModel.LastEmail != null) {
                                text_input_email_register.error =
                                    Register_ViewModel.LastEmail.toString()
                            }
                            if (Register_ViewModel.LastPhone != null) {
                                text_input_phone_register.error =
                                    Register_ViewModel.LastPhone.toString()
                            }

                        }
                    }
                }
            )


        }

    }


//    val Context.isConnected: Boolean
//        get() {
//            return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
//                .activeNetworkInfo?.isConnected == true
//        }
//

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun ValidateRegister():Boolean{
      val Fullname=text_input_name_register.editText!!.text.toString()
        if(Fullname.isEmpty()){
            text_input_name_register.error=resources.getString(R.string.feildempty)
            return false
        }else {
            text_input_name_register.error=null
            return true
        }
    }

    private fun ValidatePhoneRegister():Boolean{
        val Fullname=text_input_phone_register.editText!!.text.toString()
        if(Fullname.isEmpty()){
            text_input_phone_register.error=resources.getString(R.string.feildempty)
            return false
        }else {
            text_input_phone_register.error=null
            return true
        }
    }
    private fun ValidateEmailRegister():Boolean{
        val Fullname=text_input_email_register.editText!!.text.toString()
        if(Fullname.isEmpty()){
            text_input_email_register.error=resources.getString(R.string.feildempty)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Fullname).matches()) run {
            text_input_email_register.error =
                resources.getString(R.string.validemail)
            return false
        }
        else {
            text_input_email_register.error=null
            return true
        }
    }
    private fun ValidatePasswordRegister():Boolean{
        val Fullname=text_input_password_register.editText!!.text.toString()
        if(Fullname.isEmpty()){
            text_input_password_register.error=resources.getString(R.string.feildempty)
            return false
        } else if (!PASSWORD_PATTERN.matcher(Fullname).matches()) run {
            text_input_password_register.error =
                resources.getString(R.string.pasweak)
            return false
        }
        else {
            text_input_password_register.error=null
            return true
        }
    }


    private fun EditText_Changer(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (editText.id == R.id.E_Full_Name) {
                    ValidateRegister()
                }
                else if (editText.id == R.id.E_Phone) {
                    ValidatePhoneRegister()
                }
                else if (editText.id == R.id.E_Email_Register) {
                    ValidateEmailRegister()
                }
                else if (editText.id == R.id.E_Password_Register) {
                    ValidatePasswordRegister()
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

    fun Language() {
        shared = getSharedPreferences("Language", MODE_PRIVATE)
        val Lan = shared.getString("Lann", null)
        if(Lan!=null){
            DeviceLang = Lan
        }else {
            DeviceLang = Locale.getDefault().language

        }
    }

}
