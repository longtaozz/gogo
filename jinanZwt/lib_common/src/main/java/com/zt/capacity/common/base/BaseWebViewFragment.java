package com.zt.capacity.common.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zt.capacity.common.R;
import com.zt.capacity.common.util.BaseJavaScript;
import com.zt.capacity.common.util.UIUtils;

/**
 * Created by Administrator on 2018-04-12.
 */

public class BaseWebViewFragment extends BaseFragment {
    public WebView webView;


    /**
     * javaScriptinterface,js调android文件
     */
    @SuppressLint("JavascriptInterface")
    public void webViewCreat(String lodUrl, WebView viewWeb, Object javaScriptinterface) {
        webView = viewWeb;

        webViewSetting();

        //js调用android方法
        if (javaScriptinterface != null)
            webView.addJavascriptInterface(javaScriptinterface,
                    "android");
        else
            webView.addJavascriptInterface(new BaseJavaScript(getActivity()),
                    "android");
        UIUtils.showLoadingProgressDialog(getActivity(), R.string.loading_process_tip, false);

        //加载页面地址
        webView.loadUrl(lodUrl);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                UIUtils.cancelProgressDialog();
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("htmlnei.....",url);
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }else {
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }

    private void webViewSetting() {
        WebSettings webSettings = webView.getSettings();
        //支持js
        webSettings.setJavaScriptEnabled(true);
        //允许js弹出窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //打开页面时， 自适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        //设置支持获取手势焦点。
        webView.requestFocusFromTouch();
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//                AlertDialog.Builder b2 = new AlertDialog.Builder(FuelConsumptionActivity.this).setTitle(R.string.title).setMessage(message).setPositiveButton("ok", new AlertDialog.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.confirm();
//                    }
//                });
//                b2.setCancelable(false);
//                b2.create();
//                b2.show();
                return true;
            }


            //Android < 5.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooserImpl(uploadMsg);
            }

            //Android => 5.0
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
                onenFileChooseImpleForAndroid(uploadMsg);
                return true;
            }
        });

    }

    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    public ValueCallback<Uri> mUploadMessage;
    public final static int FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5 = 2;
    private final static int FILE_CHOOSER_RESULT_CODE = 1;// 表单的结果回调

    //* android 5.0 以下开启图片选择（原生）
    //      * 可以自己改图片选择框架。
    //     */
    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"),
                FILE_CHOOSER_RESULT_CODE);
    }

    /**
     * android 5.0(含) 以上开启图片选择（原生）
     * 可以自己改图片选择框架。
     */
    private void onenFileChooseImpleForAndroid(ValueCallback<Uri[]> filePathCallback) {
        mUploadMessageForAndroid5 = filePathCallback;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        startActivityForResult(chooserIntent, FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Uri result = (intent == null || resultCode != Activity.RESULT_OK) ? null : intent.getData();
        switch (requestCode) {
            case FILE_CHOOSER_RESULT_CODE:  //android 5.0以下 选择图片回调
                if (null == mUploadMessage)
                    return;
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
                break;
            case FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5:  //android 5.0(含) 以上 选择图片回调
                if (null == mUploadMessageForAndroid5)
                    return;
                if (result != null) {
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
                } else {
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
                }
                mUploadMessageForAndroid5 = null;
                break;
        }
    }


}
