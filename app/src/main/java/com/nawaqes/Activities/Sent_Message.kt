package com.nawaqes.Activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.nawaqes.Model.AddRequest_Response
import com.nawaqes.Model.SentMessage_Response
import com.nawaqes.R
import com.nawaqes.ViewModel.AddMessage_ViewModel
import com.nawaqes.ViewModel.AddReques_ViewModel
import kotlinx.android.synthetic.main.activity_details__product.*
import kotlinx.android.synthetic.main.activity_details__product.progressBarLogin
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_details__product.E_Message
import kotlinx.android.synthetic.main.activity_details__product.Img_ShowCamera
import kotlinx.android.synthetic.main.activity_sent__message.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class Sent_Message : AppCompatActivity() ,ActivityCompat.OnRequestPermissionsResultCallback{
    lateinit var UserToken: String
    private lateinit var DataSaver: SharedPreferences
    private val GALLERY = 1
    private val CAMERA = 2
    var file: File? = File("")
    var REQUEST_WRITE_PERMISSION:Int=786
    lateinit var shop_id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sent__message)
        init()
        getData()
        Choose_Image()
        Send_Request()



    }
    private fun init() {
        DataSaver = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        UserToken = DataSaver.getString("token", null)!!

    }


    private fun getData() {
         shop_id=intent.getStringExtra("id")
        val shop_name=intent.getStringExtra("name")
        Title.text=resources.getString(R.string.to)+" "+shop_name
        phone.text=resources.getString(R.string.mobile)+" "+intent.getStringExtra("phone")
        address.text=resources.getString(R.string.address)+" "+intent.getStringExtra("address")

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

    private fun Send_Request() {
        Btn_Sent.setOnClickListener(){
            if(isConnected){
                var RequestViewModel =  ViewModelProvider.NewInstanceFactory().create(
                    AddMessage_ViewModel::class.java)
                progressBarLogin.visibility= View.VISIBLE
                if(!E_Messages.text.toString().isEmpty()) {
                    Btn_Sent.isEnabled=false
                    Btn_Sent.hideKeyboard()
                    if(file.toString().equals("")){
                        file=null
                    }

                    RequestViewModel.getData(
                        file,
                        E_Messages.text.toString(),
                        shop_id,
                        UserToken,
                        applicationContext
                    ).observe(this,
                        Observer<SentMessage_Response> { loginmodel ->
                            Btn_Sent.isEnabled = true
                            progressBarLogin.visibility = View.GONE
                            if (loginmodel != null) {
                                E_Messages.setText(null)
                                Toast.makeText(
                                    this,
                                    resources.getString(R.string.requestsent),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {

                            }
                        })
                }else{
                    Toast.makeText(this,"Please Make Sure that you Enter description", Toast.LENGTH_LONG).show()

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
        Img_Cameras.setOnClickListener(){
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
                val filePath = getRealPathFromURIPath(contentURI!!, this@Sent_Message)
                file = File(filePath)
//                Toast.makeText(this@Details_Product, file.name+"", Toast.LENGTH_SHORT).show()

                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val path = saveImage(bitmap)
//                    Toast.makeText(this@Details_Product, path+"", Toast.LENGTH_SHORT).show()
                    Img_ShowCamera!!.setImageBitmap(bitmap)

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
            Img_ShowCamera!!.setImageBitmap(thumbnail)
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
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }


}
