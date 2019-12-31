package com.nawaqes.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.preference.PreferenceManager
import com.nawaqes.R
import java.util.*

class Splash : AppCompatActivity() {
    private lateinit var DataSaver: SharedPreferences
    internal lateinit var shared: SharedPreferences

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
        setContentView(R.layout.activity_splash)

        DataSaver= PreferenceManager.getDefaultSharedPreferences(this)

        Handler().postDelayed({
            val UserToken: String? =DataSaver.getString("token",null);
            if(UserToken!=null) {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()
            }else {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)

    }
}
