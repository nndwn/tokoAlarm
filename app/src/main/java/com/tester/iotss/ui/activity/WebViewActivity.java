package com.tester.iotss.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.tester.iotss.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private CircularProgressIndicator progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // Initialize WebView
        webView = findViewById(R.id.webview);

        // Initialize ProgressBar
        progressBar = findViewById(R.id.progress_bar);

        // Enable JavaScript (optional)
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Set WebViewClient to handle internal navigation
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Load clicked URL in WebView itself
                view.loadUrl(url);
                return true; // return true to indicate WebView will handle the URL loading
            }

            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                // Show loading indicator
                progressBar.setVisibility(ProgressBar.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Delay hiding the ProgressBar to ensure the animation is visible
                new Handler().postDelayed(() -> progressBar.setVisibility(View.GONE), 100); // Adjust delay time as needed
            }
        });

        // Get URL or HTML content from Intent extras
        String url = getIntent().getStringExtra("URL");

        if (url != null) {
            // Load URL
            webView.loadUrl(url);
        } else {
            // Alternatively, load HTML content
            String htmlContent = getIntent().getStringExtra("HTML_CONTENT");
            if (htmlContent != null) {
                webView.loadData(htmlContent, "text/html", "UTF-8");
            }
        }
    }

    // Override onBackPressed to handle WebView navigation history
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack(); // Navigate back within the WebView's history
        } else {
            super.onBackPressed(); // Default back button behavior (close activity)
        }
    }
}