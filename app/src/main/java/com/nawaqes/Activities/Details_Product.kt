package com.nawaqes.Activities

import android.Manifest
import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.nawaqes.Adapter.Slider_Adapter
import com.nawaqes.Model.Banners_Response
import com.nawaqes.R
import com.nawaqes.ViewModel.Cities_ViewModel
import com.nawaqes.ViewModel.SliderHome_ViewModel
import kotlinx.android.synthetic.main.activity_details__product.*
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.nawaqes.MainActivity
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.nawaqes.Model.AddRequest_Response
import com.nawaqes.Model.Register_Model
import com.nawaqes.ViewModel.AddReques_ViewModel
import com.nawaqes.ViewModel.Register_ViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.Btn_login
import kotlinx.android.synthetic.main.activity_register.progressBarLogin


@SuppressLint("ByteOrderMark")
class Details_Product : AppCompatActivity() , ActivityCompat.OnRequestPermissionsResultCallback{
     var Cat_Id:Int=0
     var Sub_Id:String?= String()
    lateinit var Cat_Name:String
    var swipeTimer:Timer?=null
    private var currentPage = 0
    private var NUM_PAGES = 0
    lateinit var UserToken: String
    private lateinit var DataSaver: SharedPreferences
    private val GALLERY = 1
    private val CAMERA = 2
     var file: File? = File("")
    var REQUEST_WRITE_PERMISSION:Int=786
    val handler = Handler()
    val Update = Runnable {
        if (currentPage == NUM_PAGES) {
            currentPage = 0
        }
        vp_slider!!.setCurrentItem(currentPage++, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details__product)

        getData()
        getSlider()
        Choose_Image()
        Send_Request()
//        setupPermissions()
        openHelp()

    }

    private fun openHelp() {
        Img_Help.setOnClickListener(){
            val intent = Intent(this, Help::class.java)
            startActivity(intent)

        }
    }

    private fun Send_Request() {
        Btn_Send.setOnClickListener(){
            if(isConnected){
                var RequestViewModel =  ViewModelProvider.NewInstanceFactory().create(
                    AddReques_ViewModel::class.java)
                progressBarLogin.visibility= View.VISIBLE
                if(!E_Message.text.toString().isEmpty()) {
                    Btn_Send.isEnabled=false
                    Btn_Send.hideKeyboard()
                    if(file.toString().equals("")){
                        file=null
                    }

                        RequestViewModel.getData(
                            file,
                            E_Message.text.toString(),
                            Home.CityId!!,
                            Home.StateId!!,
                            Cat_Id.toString(),
                            Sub_Id.toString(),
                            UserToken,
                            applicationContext
                        ).observe(this,
                            Observer<AddRequest_Response> { loginmodel ->
                                Btn_Send.isEnabled = true
                                progressBarLogin.visibility = View.GONE
                                if (loginmodel != null) {
                                    Toast.makeText(
                                        this,
                                        resources.getString(R.string.requestsent),
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {

                                }
                            })
                }else{
                    Toast.makeText(this,"Please Make Sure that you Enter description",Toast.LENGTH_LONG).show()

                }
            }else {
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(R.string.nointernet),
                    Toast.LENGTH_LONG
                ).show()
            }

            }
    }

    private fun Choose_Image() {
        Img_Camera.setOnClickListener(){
            val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)

            if (permission != PackageManager.PERMISSION_GRANTED) {
                makeRequest()
            }else {
                showPictureDialog()

            }
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

    private fun getData() {
        DataSaver = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        UserToken = DataSaver.getString("token", null)!!
        Cat_Id =intent.getIntExtra("cat_id",0)
        Cat_Name=intent.getStringExtra("cat_name")!!
        Sub_Id=intent.getStringExtra("sub_id")
        T_Title.text=Cat_Name
    }


    fun getSlider(){
        var SliderHome: SliderHome_ViewModel =
            ViewModelProvider.NewInstanceFactory().create(SliderHome_ViewModel::class.java)
      applicationContext?.let {
            SliderHome.getData(Cat_Id.toString(),UserToken,"en", it)?.observe(this, Observer<Banners_Response> { loginmodel ->
                if(loginmodel!=null) {
                    vp_slider!!.adapter =
                        Slider_Adapter(
                            applicationContext,
                        loginmodel.data as ArrayList<Banners_Response.Data>)
                view_pager_circle_indicator.setViewPager(vp_slider)

                NUM_PAGES = loginmodel.data!!.size
                swipeTimer = Timer()
                swipeTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        handler.post(Update)
                    }
                }, 3000, 3000)
            }

            })
        }
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle(resources.getString(R.string.selectoption))
        val pictureDialogItems = arrayOf(resources.getString(R.string.selectallery), resources.getString(R.string.capturephoto))
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
//        val galleryIntent = Intent(Intent.ACTION_PICK)
//        galleryIntent.setType("image/*");
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }
    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (resultCode == RESULT_OK && null != data) {

            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data!!.data
                    val filePath = getRealPathFromURIPath(contentURI!!, this@Details_Product)
                    file = File(filePath)
                    Glide.with(this).load("file:" + file).into(Img_ShowCamera);


//                    try {
//                        val bitmap =
//                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                        val path = saveImage(bitmap)
////                    Img_ShowCamera!!.setImageBitmap(bitmap)
//
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }

                }
            }

        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            Img_ShowCamera!!.setImageBitmap(thumbnail)
            saveImage(thumbnail)
//            Toast.makeText(this@Details_Product, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRealPathFromURIPath(contentURI: Uri, activity: Activity): String {
        val filePathColumn=arrayOf(MediaStore.Images.Media.DATA )
        val cursor = activity.contentResolver.query(contentURI,filePathColumn, null, null, null, null)
        if (cursor == null) {
            return contentURI.getPath()!!
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(filePathColumn[0])
            return cursor.getString(idx)

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

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }


    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),
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
            }
