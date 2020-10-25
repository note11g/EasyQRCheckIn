package com.note11.easyqrcheckin;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.note11.easyqrcheckin.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private WebView mWebView;
    private WebSettings mWebSettings;
    private ProgressBar progressBar1;
    private String jsQuery = "javascript:document.getElementsByTagName('button')[1].onclick"
            + " = function(){ BtnRec.getHtml('button clicked') }";
    private String allUrl = "https://nid.naver.com/login/privacyQR";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //App Screen Brightness Maximum Setting
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 1.0f;
        getWindow().setAttributes(params);

        //DataBinding Using WebView Setting
        mWebView = binding.webMain;
        mWebSettings = mWebView.getSettings();
        progressBar1 = binding.progressBar1;

        setWebView();//loading WebView Function

//        initializeBubblesManager();
//
//        binding.button3.setOnClickListener(view -> addNewBubble());

    }

    private void setWebView() {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.equals(allUrl)) view.loadUrl(jsQuery); //set Query go To Function
            }
        });
        mWebView.addJavascriptInterface(new MyJavascriptInterface(), "BtnRec");
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportMultipleWindows(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebSettings.setDomStorageEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {
                    progressBar1.setVisibility(ProgressBar.VISIBLE);
                } else if (progress == 100) {
                    progressBar1.setVisibility(ProgressBar.GONE);
                }
                progressBar1.setProgress(progress);
            }
        });
        mWebView.loadUrl(allUrl);
    }


    public void start(View v) {
        Intent intent = new Intent(this, ScreenService.class);

        startService(intent);
        Toast.makeText(this, "starting..", Toast.LENGTH_SHORT).show();
    }

    public void finishS(View v) {
        Intent intent = new Intent(this, ScreenService.class);

        stopService(intent);
        Toast.makeText(this, "ended.", Toast.LENGTH_SHORT).show();
    }




    @Override
    protected void onRestart() {
        super.onRestart();
        mWebView.reload();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        bubblesManager.recycle();
//    }

    public class MyJavascriptInterface {
        @JavascriptInterface
        public void getHtml(String html) {
            finish();//if Click "취소" button, exit this App.
        }
    }
}