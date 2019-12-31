package com.nawaqes.Activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.nawaqes.Model.EditProfile_Response
import com.nawaqes.Model.Profile_Response
import com.nawaqes.Model.Register_Model
import com.nawaqes.R
import com.nawaqes.ViewModel.EditProfile_ViewModel
import com.nawaqes.ViewModel.Profile_ViewModel
import com.nawaqes.ViewModel.Register_ViewModel
import kotlinx.android.synthetic.main.activity_details__product.*
import kotlinx.android.synthetic.main.activity_edit__profile.*
import kotlinx.android.synthetic.main.activity_edit__profile.Img_Profile
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.E_Email_Register
import kotlinx.android.synthetic.main.activity_register.E_Full_Name
import kotlinx.android.synthetic.main.activity_register.E_Phone
import kotlinx.android.synthetic.main.activity_register.progressBarLogin
import kotlinx.android.synthetic.main.activity_register.text_input_email_register
import kotlinx.android.synthetic.main.activity_register.text_input_name_register
import kotlinx.android.synthetic.main.activity_register.text_input_phone_register
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class Edit_Profile : AppCompatActivity() {
    private val GALLERY = 1
    private val CAMERA = 2
    var file: File? = File("")
    var REQUEST_WRITE_PERMISSION:Int=786
    lateinit var UserToken: String
    private lateinit var DataSaver: SharedPreferences
    lateinit var profile: Profile_ViewModel
    lateinit var DeviceLang:String
    private lateinit var dataSaver: SharedPreferences
    internal lateinit var share: SharedPreferences.Editor
    internal lateinit var shared: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit__profile)
        DataSaver = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        UserToken = DataSaver.getString("token", null)!!
        dataSaver = PreferenceManager.getDefaultSharedPreferences(this);
        Language()
        showLanguage()
        Choose_Image()
        showInfo()
        openArabic()
        openEnglish()
        openLogout()

        EditText_Changer(text_input_name_register.editText!!)
        EditText_Changer(text_input_phone_register.editText!!)
        EditText_Changer(text_input_email_register.editText!!)
    }

    private fun openLogout() {
        Btn_logout.setOnClickListener(){
            dataSaver.edit().putString("token", null).apply()
            val intent = Intent(this, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun openEnglish() {
        Btn_English.setOnClickListener(){
            share = getSharedPreferences("Language", MODE_PRIVATE).edit()
            share.putString("Lann", "en")
            share.commit()

            val intent = Intent(this, Home::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()

        }
    }

    private fun openArabic() {
        Btn_Arabic.setOnClickListener(){
            share = getSharedPreferences("Language", MODE_PRIVATE).edit()
            share.putString("Lann", "ar")
            share.commit()

            val intent = Intent(this, Home::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun showLanguage() {
        if(DeviceLang.equals("ar")){
            Btn_English.setBackgroundResource(R.drawable.bc_white)
            Btn_English.setTextColor(Color.parseColor("#000000"))
            Btn_Arabic.setBackgroundResource(R.drawable.bc_btsignin)
            Btn_Arabic.setTextColor(Color.parseColor("#ffffff"))

        }else {
            Btn_Arabic.setBackgroundResource(R.drawable.bc_white)
            Btn_Arabic.setTextColor(Color.parseColor("#000000"))
            Btn_English.setBackgroundResource(R.drawable.bc_btsignin)
            Btn_English.setTextColor(Color.parseColor("#ffffff"))

        }
    }

    private fun showInfo() {
        profile= ViewModelProvider.NewInstanceFactory().create(
            Profile_ViewModel::class.java)

        this.applicationContext?.let {
            profile.getData(UserToken,"en", it).observe(this,
                Observer<Profile_Response> { loginmodel ->
                    if(loginmodel!=null) {
                        E_Full_Name.setText(loginmodel.data.fullName)
                        E_Phone.setText(loginmodel.data.phone)
                        E_Email_Register.setText(loginmodel.data.email)
                        Glide.with(this)
                            .load(loginmodel.data.image_path)
                            .into(Img_Profile)

                    }
                })
        }

    }

    fun Confirm(view: View) {
        if (!ValidateEmailRegister() or !ValidatePhoneRegister() or !ValidateRegister()) {
            return
        }
        if(isConnected){
            var RegisterViewModel =  ViewModelProvider.NewInstanceFactory().create(
                EditProfile_ViewModel::class.java)
            progressBarLogin.visibility= View.VISIBLE
            view.isEnabled=false
            view.hideKeyboard()
            if(file.toString().equals("")){
                file=null
            }
            RegisterViewModel.getData(file,E_Email_Register.text.toString(),E_Phone.text.toString(),E_Full_Name.text.toString(),UserToken, applicationContext).observe(this,
                Observer<EditProfile_Response> { loginmodel ->
                    view.isEnabled=true
                    progressBarLogin.visibility = View.GONE
                    if (loginmodel != null) {
                    Toast.makeText(this,resources.getString(R.string.updatesuccess),Toast.LENGTH_LONG).show()
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

    private fun Choose_Image() {
        Img_Profile.setOnClickListener(){
            val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)

            if (permission != PackageManager.PERMISSION_GRANTED) {
                makeRequest()
            }else {
                showPictureDialog()

            }
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_WRITE_PERMISSION)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_PERMISSION -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

//                    Log.i(TAG, "Permission has been denied by user")
                } else {
//                    Log.i(TAG, "Permission has been granted by user")
                    showPictureDialog()


                }
            }
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
//        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
//            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
//            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }
    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
    }
    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data!!.data
                val filePath = getRealPathFromURIPath(contentURI!!, this@Edit_Profile)
                file = File(filePath)
//                Toast.makeText(this@Details_Product, file.name+"", Toast.LENGTH_SHORT).show()

                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val path = saveImage(bitmap)
//                    Toast.makeText(this@Details_Product, path+"", Toast.LENGTH_SHORT).show()
                    Img_Profile!!.setImageBitmap(bitmap)

                }
                catch (e: IOException) {
                    e.printStackTrace()
//                    Toast.makeText(this@Details_Product, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data?.extras?.get("data") as Bitmap
            Img_Profile!!.setImageBitmap(thumbnail)
            saveImage(thumbnail)
//            Toast.makeText(this@Details_Product, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRealPathFromURIPath(contentURI: Uri, activity: Activity): String {
        val cursor = activity.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            return contentURI.getPath()!!
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return cursor.getString(idx)
        }
    }
    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }
    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(resources.getString(R.string.selectoption))
        val pictureDialogItems = arrayOf(resources.getString(R.string.selectallery),resources.getString(R.string.capturephoto))
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
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
