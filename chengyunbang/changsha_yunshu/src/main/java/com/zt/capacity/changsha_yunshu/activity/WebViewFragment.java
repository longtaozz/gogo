package com.zt.capacity.changsha_yunshu.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.zt.capacity.changsha_yunshu.util.JavaScriptinterface;
import com.zt.capacity.common.R;
import com.zt.capacity.common.base.BaseWebViewFragment;


/**
 * Created by Administrator on 2018-04-12.
 */

public class WebViewFragment extends BaseWebViewFragment {
    public WebView webView;
    private View view;

    private String url;
    Object javaScrip = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.base_web_view, null);

        webView = view.findViewById(R.id.myweb);

        if (getArguments() != null) {
            url = getArguments().getString("url");
            javaScrip = getArguments().getSerializable("javaScriptinterface");
        }
        Log.e("webView......",url);

//        Web.getUser().getUnloadingId()
        webViewCreat(url, webView, new JavaScriptinterface(getActivity()));
        return view;
    }


    public static WebViewFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
