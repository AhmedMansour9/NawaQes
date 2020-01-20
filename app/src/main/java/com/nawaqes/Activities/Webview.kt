package com.nawaqes.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import com.nawaqes.R
import android.webkit.WebViewClient





class Webview : AppCompatActivity() {

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val webview = findViewById(R.id.webView) as WebView
        webview.settings.javaScriptEnabled = true
        val webViewSettings = webview.settings
        webViewSettings.javaScriptCanOpenWindowsAutomatically = true
        webViewSettings.javaScriptEnabled = true
        webViewSettings.builtInZoomControls = true
        webViewSettings.pluginState = WebSettings.PluginState.ON
        webview.loadUrl(intent.getStringExtra("type"))
        webview.setWebViewClient(MyWebViewClient())


    }
}
