package com.zt.capacity.jinan_zwt.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.webkit.WebView;

import com.zt.capacity.common.R;
import com.zt.capacity.common.base.BaseWebViewActivity;
import com.zt.capacity.jinan_zwt.util.JavaScriptinterface;

/**
 * Created by Administrator on 2018-04-12.
 */

public class WebViewActivity extends BaseWebViewActivity {
    public WebView webView;
    String url = "";
    Object javaScrip = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_web_view);
        webView = findViewById(R.id.myweb);
        url = getIntent().getStringExtra("url");
        javaScrip = new JavaScriptinterface(this);
        if (url == null) {
            url = "";
        }
        Log.e("webView......",url);
        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                webViewCreat(url, webView, javaScrip);
            }
        }

    }


}
