package com.zt.capacity.common.base;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.view.Gravity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hjq.toast.IToastStyle;
import com.hjq.toast.ToastUtils;
import com.hjq.toast.style.ToastBlackStyle;
import com.zt.capacity.common.BuildConfig;
import com.zt.capacity.common.util.MyToastBlackStyle;


public class BaseApplication extends Application {
    private static BaseApplication ins;

    private static BaseApplication bApplication;
    public static Activity mActivity;


    public static SharedPreferences sharedPreferences;
    public static Boolean rememberState = false;//记住密码选中状态


    public Vibrator mVibrator;

    //根目录
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath();

    //图片储存地址
    public static String savePath = path + "/zwb/img/";


    public static BaseApplication getContext() {
        return bApplication;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public static String versionName="0";

    @Override
    public void onCreate() {
        bApplication = this;


        // 初始化吐司工具类
        ToastUtils.init(this,new MyToastBlackStyle());
        //是否记住密码判断
        sharedPreferences = getSharedPreferences("userInfo",
                Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(sharedPreferences.getString("password", null))) {
            rememberState = true;
        }


        try {
            versionName = getPackageManager().
                    getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //振动器
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);


        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
//            ARouter.printStackTrace();
        }
        ARouter.init(this);

        super.onCreate();
    }


    public static BaseApplication getIns() {
        return ins;
    }

    /**
     * 获取登录设备mac地址
     *
     * @return Mac地址
     */
    public String getMacAddress() {
        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wm.getConnectionInfo();
        String mac = connectionInfo.getMacAddress();
        return mac == null ? "02:00:00:00:00:00" : mac;
    }


    private void dingwei() {

    }


    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        super.onLowMemory();
    }


    //安装完成并初次启动APP的时候，5.0以下某些低端机会出现ANR或者长时间卡顿不进入引导页
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            MultiDex.install(this);
    }


}
