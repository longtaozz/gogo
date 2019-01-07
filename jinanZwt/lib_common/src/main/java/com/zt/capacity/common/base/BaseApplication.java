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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.mapapi.SDKInitializer;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.zt.capacity.common.data.listener.MyLocationListener;
import com.zt.capacity.common.service.LocationService;
import com.zt.capacity.common.util.JurisdictionUtil;



public class BaseApplication extends Application {
    private static BaseApplication ins;

    private static BaseApplication bApplication;
    public static Activity mActivity;


    public static String identify;//唯一标识
    public static String brand;//设备厂商


    public static SharedPreferences sharedPreferences;
    public static Boolean rememberState = false;//记住密码选中状态


    public static LocationService locationService;
    public Vibrator mVibrator;

    public static String versionName="0";


    public static BaseApplication getContext() {
        return bApplication;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    //全景
    public BMapManager mBMapManager = null;

    @Override
    public void onCreate() {
        bApplication = this;



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

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());

        initEngineManager(this);

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivity = activity;

                //安装权限
//                JurisdictionUtil.REQUEST_INSTALL_PACKAGES(mActivity);
//                JurisdictionUtil.GETWRITEEXTERNAL(mActivity);
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });


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


    public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(new MyGeneralListener())) {
            Toast.makeText(BaseApplication.getContext().getApplicationContext(), "BMapManager  初始化错误!",
                    Toast.LENGTH_LONG).show();
        }
        Log.d("ljx", "initEngineManager");
    }


    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public static class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetPermissionState(int iError) {
            // 非零值表示key验证未通过
//            if (iError != 0) {
//                // 授权Key错误：
//                Toast.makeText(MyApplication.getContext().getApplicationContext(),
//                        "请在AndoridManifest.xml中输入正确的授权Key,并检查您的网络连接是否正常！error: " + iError, Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(MyApplication.getContext().getApplicationContext(), "key认证成功", Toast.LENGTH_LONG)
//                        .show();
//            }
        }
    }



    //进行一次定位并设为公共变量
    public static MyLocationListener myLocationListener = new MyLocationListener();//定位监听



    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        locationService.unregisterListener(myLocationListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onTerminate();
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
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            MultiDex.install(this);
    }


}
