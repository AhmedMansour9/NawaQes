package com.nawaqes.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.nawaqes.Model.Register_Model
import com.nawaqes.R
import com.nawaqes.ViewModel.Register_ViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.Btn_login
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import java.util.regex.Pattern
import kotlinx.android.synthetic.main.activity_login.progressBarLogin as progressBarLogin1

class Login : AppCompatActivity() {
    private val PASSWORD_PATTERN = Pattern.compile(
        "(?=\\S+$)" +           //no white spaces
                ".{5,}"                //at least 4 characters
    )
    private lateinit var dataSaver: SharedPreferences
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dataSaver = PreferenceManager.getDefaultSharedPreferences(this);

        openRegister()
        openHome()
        LoginFacebook()
        EditText_Changer(text_input_password_login.editText!!)
        EditText_Changer(text_input_email_login.editText!!)
    }

    private fun LoginFacebook() {
//        Btn_Face.setOnClickListener(View.OnClickListener {
//            // Login
//            callbackManager = CallbackManager.Factory.create()
//            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
//            LoginManager.getInstance().registerCallback(callbackManager,
//                object : FacebookCallback<LoginResult> {
//                    override fun onSuccess(loginResult: LoginResult) {
////                        Log.d("MainActivity", "Facebook token: " + loginResult.accessToken.token)
//
////                        startActivity(Intent(applicationContext, AuthenticatedActivity::class.java))
//                    }
//
//                    override fun onCancel() {
////                        Log.d("MainActivity", "Facebook onCancel.")
//
//                    }
//
//                    override fun onError(error: FacebookException) {
////                        Log.d("MainActivity", "Facebook onError.")
//
//                    }
//                })
//        })
    }

    fun openRegister(){
        T_Signup.setOnClickListener(){
            T_Signup.isEnabled=false
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
    fun openHome(){
        Btn_login.setOnClickListener(){
            if (!ValidateEmailLogin() or !ValidatePasswordLogin()) {
                return@setOnClickListener
            }

            if(isConnected){
                var RegisterViewModel =  ViewModelProvider.NewInstanceFactory().create(
                    Register_ViewModel::class.java)
                progressBarLogin.visibility= View.VISIBLE
                Btn_login.isEnabled=false
                Btn_login.hideKeyboard()
                RegisterViewModel.getLogin(text_input_email_login.editText!!.text.toString(), text_input_password_login.editText!!.text.toString(),applicationContext).observe(this,
                    Observer<Register_Model> { loginmodel ->
                        Btn_login.isEnabled=true
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
                                Toast.makeText(
                                    applicationContext,
                                    applicationContext.getString(R.string.wrongemailorpass),
                                    Toast.LENGTH_LONG
                                ).show()
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

    }

    private fun ValidateEmailLogin():Boolean{
        val Fullname=text_input_email_login.editText!!.text.toString()
        if(Fullname.isEmpty()){
            text_input_email_login.error=resources.getString(R.string.feildempty)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Fullname).matches()) run {
            text_input_email_login.error =
                resources.getString(R.string.validemail)
            return false
        }
        else {
            text_input_email_login.error=null
            return true
        }
    }

    private fun ValidatePasswordLogin():Boolean{
        val Fullname=text_input_password_login.editText!!.text.toString()
        if(Fullname.isEmpty()){
            text_input_password_login.error=resources.getString(R.string.feildempty)
            return false
        } else if (!PASSWORD_PATTERN.matcher(Fullname).matches()) run {
            text_input_password_login.error =
                "Password too weak"
            return false
        }
        else {
            text_input_password_login.error=null
            return true
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    val Context.isConnected: Boolean
        get() {
            return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .activeNetworkInfo?.isConnected == true
        }

    private fun EditText_Changer(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (editText.id == R.id.E_Email) {
                    ValidateEmailLogin()
                }
                else if (editText.id == R.id.E_Password) {
                    ValidatePasswordLogin()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }
}
