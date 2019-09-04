package com.aysel.webviewapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
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
        mwebview.getSettings().setAllowFileAccess(true);
        mwebview.setWebViewClient(new Browser_home());
        mwebview.getSettings().setDomStorageEnabled(true);
        mwebview.loadUrl("https://gelecegiyazanlar.turkcell.com.tr");
        mwebview.setWebChromeClient(new MyChrome());

        mwebview.getSettings().setTextZoom(100); // 100% is maximum here
        mwebview.getSettings().setAppCacheEnabled(true);
        mwebview.getSettings().setBuiltInZoomControls(true);
        mwebview.getSettings().setSaveFormData(true);

        mwebview.setWebViewClient(new WebViewClient() {

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
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mwebview.canGoBack()) {
                        mwebview.goBack();
                    }
                    return true;
            }
        } else {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class Browser_home extends WebViewClient {
        Browser_home() {

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mProgressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    }

    private class MyChrome extends WebChromeClient {

        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChrome() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }

}