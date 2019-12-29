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
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import com.nawaqes.Adapter.Cities_Adapter
import com.nawaqes.Model.Cities_Response
import com.nawaqes.Model.Register_Model
import com.nawaqes.R
import com.nawaqes.ViewModel.Cities_ViewModel
import com.nawaqes.ViewModel.Register_ViewModel
import com.nawaqes.ViewModel.States_ViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

class Register : AppCompatActivity() {
    lateinit var City_Id:String
    lateinit var State_Id:String
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

        getAllCities()
        EditText_Changer(text_input_name_register.editText!!)
        EditText_Changer(text_input_phone_register.editText!!)
        EditText_Changer(text_input_email_register.editText!!)
        EditText_Changer(text_input_password_register.editText!!)

    }

    fun getAllCities(){
        val allCities = ViewModelProvider.NewInstanceFactory().create(Cities_ViewModel::class.java)
        this.applicationContext?.let {
            allCities.getData( it)?.observe(this, Observer<Cities_Response> { loginmodel ->
                if(loginmodel!=null) {
                    val customAdapter = Cities_Adapter(this.applicationContext, loginmodel.data)
                    S_Area.setAdapter(customAdapter)
                    S_Area.setOnItemSelectedListener(object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View,
                            i: Int,
                            l: Long
                        ) {
                            var City = S_Area.getSelectedItem().toString()
                            var i = 0
                            while (i < loginmodel.data!!.size) {
                                if (loginmodel.data!!.get(i).name.equals(City)) {
                                    City_Id = loginmodel.data!!.get(i).id.toString()
                                }
                                i++
                            }
                            getAllStates(City_Id)
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
            allCities.getData( Id,it)?.observe(this, Observer<Cities_Response> { loginmodel ->
                if(loginmodel!=null) {
                    val customAdapter = Cities_Adapter(this.applicationContext, loginmodel.data)
                    S_Location.setAdapter(customAdapter)
                    S_Location.setOnItemSelectedListener(object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>,
                            view: View,
                            i: Int,
                            l: Long
                        ) {

                            var City = S_Location.getSelectedItem().toString()
                            var i = 0
                            while (i < loginmodel.data!!.size) {
                                if (loginmodel.data!!.get(i).name.equals(City)) {
                                    State_Id = loginmodel.data!!.get(i).id.toString()
                                }
                                i++
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

        if(isConnected){
                var RegisterViewModel =  ViewModelProvider.NewInstanceFactory().create(Register_ViewModel::class.java)
                progressBarLogin.visibility= View.VISIBLE
                view.isEnabled=false
                view.hideKeyboard()
                RegisterViewModel.getData(E_Full_Name.text.toString(),E_Email_Register.text.toString(), E_Password_Register.text.toString(),E_Phone.text.toString(),State_Id,City_Id, applicationContext).observe(this,
                    Observer<Register_Model> { loginmodel ->
                        view.isEnabled=true
                        progressBarLogin.visibility = View.GONE
                        if (loginmodel != null) {
                            val customer_id = loginmodel.accessToken
                            dataSaver.edit().putString("token", customer_id).apply()
                            val intent = Intent(this, Home::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val status: Boolean = RegisterViewModel.getStatus()
                            if (status == true) {
                                if(Register_ViewModel.LastEmail!=null){
                                    text_input_email_register.error =Register_ViewModel.LastEmail.toString()
                                }
                                if(Register_ViewModel.LastPhone!=null){
                                    text_input_phone_register.error =Register_ViewModel.LastPhone.toString()
                                }

                            }
                        }
                    }
                )
            }else {
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(R.string.nointernet),
                    Toast.LENGTH_LONG
                ).show()

        }
    }


    val Context.isConnected: Boolean
        get() {
            return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .activeNetworkInfo?.isConnected == true
        }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun ValidateRegister():Boolean{
      val Fullname=text_input_name_register.editText!!.text.toString()
        if(Fullname.isEmpty()){
            text_input_name_register.error="Field can't be empty"
            return false
        }else {
            text_input_name_register.error=null
            return true
        }
    }

    private fun ValidatePhoneRegister():Boolean{
        val Fullname=text_input_phone_register.editText!!.text.toString()
        if(Fullname.isEmpty()){
            text_input_phone_register.error="Field can't be empty"
            return false
        }else {
            text_input_phone_register.error=null
            return true
        }
    }
    private fun ValidateEmailRegister():Boolean{
        val Fullname=text_input_email_register.editText!!.text.toString()
        if(Fullname.isEmpty()){
            text_input_email_register.error="Field can't be empty"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Fullname).matches()) run {
            text_input_email_register.error =
                "Please enter a valid email address"
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
            text_input_password_register.error="Field can't be empty"
            return false
        } else if (!PASSWORD_PATTERN.matcher(Fullname).matches()) run {
            text_input_password_register.error =
                "Password too weak"
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
                else if (editText.id == R.id.E_Email_Register) {
                    ValidateEmailRegister()
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

}
