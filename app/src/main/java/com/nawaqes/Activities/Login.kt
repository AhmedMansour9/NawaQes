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
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.nawaqes.Model.Register_Model
import com.nawaqes.R
import com.nawaqes.ViewModel.Register_ViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.Btn_login
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.util.*
import java.util.logging.Logger
import java.util.regex.Pattern
import kotlinx.android.synthetic.main.activity_login.progressBarLogin as progressBarLogin1
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Login : AppCompatActivity() {
    private val PASSWORD_PATTERN = Pattern.compile(
        "(?=\\S+$)" +           //no white spaces
                ".{5,}"                //at least 4 characters
    )
    private lateinit var dataSaver: SharedPreferences
    private var callbackManager: CallbackManager? = null
    var socialid:String?= String()
    var email:String?=String()
    var name:String?=String()
    lateinit var mAuth: FirebaseAuth
    lateinit var googleApiClient: GoogleApiClient
    var RequestSignInCode:Int=7
    lateinit var googleSignInOptions: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        dataSaver = PreferenceManager.getDefaultSharedPreferences(this);
        mAuth = FirebaseAuth.getInstance();
        openRegister()
        GoogleSignOpition();
        openHome()
        LoginFacebook()
        Login_Google()
        EditText_Changer(text_input_password_login.editText!!)
        EditText_Changer(text_input_email_login.editText!!)
        openCompany()
        openShop()

    }

    private fun openShop() {
        Btn_shop.setOnClickListener(){
            val intent = Intent(this, Webview::class.java)
            intent.putExtra("type","http://nwqis.com/shop/login")
            startActivity(intent)
            finish()
        }
    }

    private fun openCompany() {
        Btn_company.setOnClickListener(){
            val intent = Intent(this, Webview::class.java)
            intent.putExtra("type","http://nwqis.com/company/login")
            startActivity(intent)
            finish()
        }
    }

    private fun GoogleSignOpition() {

        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient =  GoogleApiClient.Builder(applicationContext)
//                .enableAutoManage(applicationContext)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    private  fun Login_Google(){
     Btn_Google.setOnClickListener(){
         val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
         startActivityForResult(signInIntent, RequestSignInCode)
     }

    }

    private fun LoginFacebook() {
        Btn_Face.setOnClickListener(View.OnClickListener {
            // Login
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, response ->
                                if (`object`.has("email")) {
                                    email =`object`.get("email").toString()
                                }
                                if (`object`.has("id")) {
                                    socialid =`object`.get("id").toString()
                                }
                                if (`object`.has("name")) {
                                    name =`object`.get("name").toString()
                                }

                            LoginFaceBooks(socialid,email,name)
                        }
                        val parameters = Bundle()
                        parameters.putString("fields", "name,email,id,picture.type(large)")
                        request.parameters = parameters
                        request.executeAsync()
                    }

                    override fun onCancel() {
                    }

                    override fun onError(error: FacebookException) {
                        Toast.makeText(applicationContext,error.toString(),Toast.LENGTH_LONG).show()
                    }
                })

        })
    }


    fun openRegister(){
        T_Signup.setOnClickListener(){
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

    fun LoginFaceBooks(id:String?,email:String? ,name:String?){
        if(isConnected){
            var RegisterViewModel =  ViewModelProvider.NewInstanceFactory().create(
                Register_ViewModel::class.java)
            progressBarLogin.visibility= View.VISIBLE
            RegisterViewModel.getLoginFacebook(id, email,name,applicationContext).observe(this,
                Observer<Register_Model> { loginmodel ->
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
    private fun ValidateEmailLogin():Boolean{
        val Fullname=text_input_email_login.editText!!.text.toString()
        if(Fullname.isEmpty()){
            text_input_email_login.setErrorTextAppearance(R.style.error_appearance)
            text_input_email_login.error=resources.getString(R.string.feildempty)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Fullname).matches()) run {
            text_input_email_login.setErrorTextAppearance(R.style.error_appearance)
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
            text_input_password_login.setErrorTextAppearance(R.style.error_appearance)
            text_input_password_login.error =
                resources.getString(R.string.pasweak)
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
        if(requestCode==RequestSignInCode){
          var googleSignInResult:GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           if (googleSignInResult.isSuccess()) {
               var googleSignInAccount: GoogleSignInAccount = googleSignInResult.signInAccount!!;
                FirebaseUserAuth(googleSignInAccount);
            }

        }

    }
    private fun FirebaseUserAuth(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    LoginFaceBooks(
                        mAuth.currentUser!!.uid,
                        mAuth.currentUser!!.email,
                        mAuth.currentUser!!.displayName
                    )

                } else {
                    // If sign in fails, display a message to the user.
                }
            }
    }
    override fun onStop() {
        super.onStop()
        Btn_login.isEnabled=true
    }

    override fun onPause() {
        super.onPause()
        Btn_login.isEnabled=true
    }
}

