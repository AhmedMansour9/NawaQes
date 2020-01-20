package com.nawaqes

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.FacebookActivity
import com.facebook.FacebookSdk

class App  :Application(){

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(this)

    }
}