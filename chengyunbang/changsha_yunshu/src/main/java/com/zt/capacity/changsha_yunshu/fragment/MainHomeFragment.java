package com.zt.capacity.changsha_yunshu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zt.capacity.changsha_yunshu.R;
import com.zt.capacity.changsha_yunshu.request.push.IPushManager;
import com.zt.capacity.changsha_yunshu.request.push.PushManager;
import com.zt.capacity.changsha_yunshu.util.JavaScriptinterface;
import com.zt.capacity.common.base.BaseWebViewFragment;
import com.zt.capacity.common.data.listener.OnNetResultListener;



/**
 * 济南运输公司首页
 * @author lt
 * @time 2018/12/14 17:43
 *
 **/
public class MainHomeFragment extends BaseWebViewFragment implements View.OnClickListener {
    public WebView webView;
    private View view;

    private String url;


    private RelativeLayout notification;//消息


    //当前fragment是否可见
    private static boolean b = true;

    private ImageView unread_number;//数量

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.changsha_yunshu_home_activity, null);

        webView = view.findViewById(R.id.myweb);

        init();


        if (getArguments() != null) {
            url = getArguments().getString("url");
        }

        Log.e("webView......",url);
//        Web.getUser().getUnloadingId()
        webViewCreat(url, webView, new JavaScriptinterface(getActivity()));

        return view;
    }



    private void init() {
        notification = view.findViewById(R.id.notification);//信息
        notification.setOnClickListener(this);

        unread_number = view.findViewById(R.id.unread_number);
    }

    public static MainHomeFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        MainHomeFragment fragment = new MainHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.notification) {//消息
            ARouter.getInstance().build("/jinan/yunshu/notification").navigation();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //未读数量
                    if (msg.arg1 > 0) {
                        unread_number.setVisibility(View.VISIBLE);
                    } else {
                        unread_number.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        b = true;
        IPushManager pushManager = PushManager.getInterface(4);
        pushManager.getUnreadNumber(new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message = new Message();
                if (state == 0) {
                    message.what = 0;
                    if (body != null) {
                        message.arg1 = Integer.parseInt(body + "");
                    } else {
                        message.arg1 = 0;
                    }
                }
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        b = false;
    }
}
