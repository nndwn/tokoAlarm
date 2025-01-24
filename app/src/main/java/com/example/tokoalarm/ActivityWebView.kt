package com.example.tokoalarm

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("SetJavaScriptEnabled")
class ActivityWebView :AppCompatActivity(){
    private lateinit var webView: WebView
    private lateinit var loading: DialogLoading
    private lateinit var dialogError: DialogError
    private val handler  = Handler(Looper.getMainLooper())
    private val timeoutRunnable = Runnable {
        webView.stopLoading()
        loading.dismissDialog()
        dialogError.startDialog(getString(R.string.info), getString(R.string.trouble_connection))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView = findViewById(R.id.webview)
        loading = DialogLoading(this@ActivityWebView)
        dialogError = DialogError(this@ActivityWebView)

        val webSettings = webView.settings
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setBackgroundColor(getColor(R.color.primary_color))
        }
        loading.startLoadingDialog()
        webSettings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request : WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                handler.postDelayed(timeoutRunnable,  10000)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                handler.removeCallbacks(timeoutRunnable)
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