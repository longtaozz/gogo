package com.zt.capacity.jinan_zwt.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.common.base.BaseWebViewFragment;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.ActivityJumpUtil;
import com.zt.capacity.jinan_zwt.MyApplication;
import com.zt.capacity.jinan_zwt.R;
import com.zt.capacity.jinan_zwt.activity.MainMyActivity;
import com.zt.capacity.jinan_zwt.activity.NotificationActivity;
import com.zt.capacity.jinan_zwt.activity.PreviewCodeActivity;
import com.zt.capacity.jinan_zwt.activity.VoiceMaskActivity;
import com.zt.capacity.jinan_zwt.request.push.IPushManager;
import com.zt.capacity.jinan_zwt.request.push.PushManager;
import com.zt.capacity.jinan_zwt.util.CacheUtil;
import com.zt.capacity.jinan_zwt.util.FileUtil;
import com.zt.capacity.jinan_zwt.util.JavaScriptinterface;

import java.io.File;

import pr.platerecognization.PlateRecognition;

/**
 * 济南首页页面
 */
public class MainHomeFragment extends BaseWebViewFragment implements View.OnClickListener {
    public WebView webView;
    private View view;

    private String url;

    //控件
    private ImageView sweep_jn;//扫一扫

    private ImageView voice_jn;//语音

    private ImageView home_fgmt_my;//我的

    private RelativeLayout notification;//消息

    private ImageView unread_number;//数量

    //当前fragment是否可见
    private static boolean b = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.jn_zwt_web_view, null);

        webView = view.findViewById(R.id.myweb);

        init();

        //加载模型之前隐藏掉扫一扫
        sweep_jn.setVisibility(View.GONE);
        //加载模型
        new Thread() {
            @Override
            public void run() {
                super.run();
                initRecognizer();
            }
        }.start();

        if (getArguments() != null) {
            url = getArguments().getString("url");
        }

//        Web.getUser().getUnloadingId()
        webViewCreat(url, webView, new JavaScriptinterface(getActivity()));

        return view;
    }

    public void initRecognizer() {
        Log.e("fuzhiwenjian....", 0 + "");
        String str = getDiskCacheDir(getActivity()) + "pr";
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        }
        if (CacheUtil.readString(MyApplication.getContext(), "MODEL") == null) {
            FileUtil.copyFilesFromAssets(getActivity(), "pr", str);
            CacheUtil.writeString(MyApplication.getContext(), "MODEL", "TRUE");
        }
        PlateRecognition.handle = PlateRecognition.InitPlateRecognizer(str + File.separator + "cascade.xml", str + File.separator + "cascade_double.xml", str, str + File.separator + "LPR_hkmc.mlz");

        Message message = new Message();
        message.what = 3;
        handler.sendMessage(message);

        Log.e("fuzhiwenjian....", 1 + "");
    }

    //当SD卡存在或者SD卡不可被移除的时候，就调用getExternalCacheDir()方法来获取缓存路径，否则就调用getCacheDir()方法来获取缓存路径
    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }


    private void init() {
        sweep_jn = view.findViewById(R.id.sweep_jn);
        sweep_jn.setOnClickListener(this);
        voice_jn = view.findViewById(R.id.voice_jn);//语音
        voice_jn.setOnClickListener(this);
        home_fgmt_my = view.findViewById(R.id.home_fgmt_my);//我的
        home_fgmt_my.setOnClickListener(this);

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
        if (i == R.id.sweep_jn) {
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNSaoMiao");
            ActivityJumpUtil.jumpActivity(getActivity(), PreviewCodeActivity.class);

//                ActivityJumpUtil.jumpActivity(getActivity(), CaptureActivity.class);

        } else if (i == R.id.voice_jn) {//语音
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNYuYin");
            ActivityJumpUtil.jumpActivity(getActivity(), VoiceMaskActivity.class);

        } else if (i == R.id.home_fgmt_my) {//我的
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNWoDe");
            ActivityJumpUtil.jumpActivity(getActivity(), MainMyActivity.class);

        } else if (i == R.id.notification) {//消息
            MobclickAgent.onEvent(BaseApplication.getContext(), "JNXiaoXi");
            ActivityJumpUtil.jumpActivity(getActivity(), NotificationActivity.class);

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
                case 3:
                    //加文件加载完成
                    if (b) {
                        sweep_jn.setVisibility(View.VISIBLE);
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


        MobclickAgent.onPageStart("JNShouYeYeMian"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        b = false;
        MobclickAgent.onPageEnd("JNShouYeYeMian");
    }
}
