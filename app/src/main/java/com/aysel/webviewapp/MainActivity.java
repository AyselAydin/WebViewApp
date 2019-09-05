package com.aysel.webviewapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView mwebview;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mwebview = findViewById(R.id.web);
        mProgressBar = findViewById(R.id.progressBar);

        mProgressBar.setVisibility(View.VISIBLE);
        mwebview.getSettings().setJavaScriptEnabled(true);
        mwebview.getSettings().setDomStorageEnabled(true);
        mwebview.loadUrl("https://gelecegiyazanlar.turkcell.com.tr");
        mwebview.setWebViewClient(new Browser_home());
    }

    @Override
    public void onBackPressed() {
        if (mwebview != null && mwebview.canGoBack())
            mwebview.goBack();
        else
            super.onBackPressed();
    }

    private class Browser_home extends WebViewClient {
        Browser_home() {

        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mwebview.loadUrl("file:///android_asset/error.html");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("whatsapp")) {
                try {
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Uygulama yüklü değil", Toast.LENGTH_LONG).show();
                }
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}