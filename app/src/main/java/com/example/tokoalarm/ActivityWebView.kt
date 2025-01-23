package com.example.tokoalarm

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("SetJavaScriptEnabled")
class ActivityWebView :AppCompatActivity(){
    private lateinit var webView: WebView
    private lateinit var loading: DialogLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView = findViewById(R.id.webview)
        loading = DialogLoading(this@ActivityWebView)

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request : WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                loading.startLoadingDialog()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                loading.dismissDialog()
            }
        }
        val url = intent.getStringExtra("URL")
        if (url != null) webView.loadUrl(url)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(webView.canGoBack()) {
                    webView.goBack()
                } else  {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }
}