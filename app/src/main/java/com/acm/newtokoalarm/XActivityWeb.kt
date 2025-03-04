package com.acm.newtokoalarm

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class XActivityWeb : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var dialog : GDialog
    private lateinit var alert : DialogData
    private val handler = Handler(Looper.getMainLooper())
    private val timeOutRunnable = Runnable {
        webView.stopLoading()
        dialog.dismissLoading()
        dialog.alert(alert){
            onBackPressedDispatcher.onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webView = findViewById(R.id.webview)
        webView.setBackgroundColor(getColor(R.color.white))

        alert = DialogData (
            title = getString(R.string.error),
            message = getString(R.string.connectionIssue),
            animation = R.raw.error,
            btnText = getString(R.string.kembali)
        )

        dialog = GDialog(this)
        dialog.loading(
            background = true
        )

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true


        webView.webViewClient = object :WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                handler.postDelayed(timeOutRunnable, 10000)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                handler.removeCallbacks(timeOutRunnable)
                dialog.dismissLoading()
            }
        }

        val url = intent.getStringExtra("URL")
        if (url != null) webView.loadUrl(url)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }
}