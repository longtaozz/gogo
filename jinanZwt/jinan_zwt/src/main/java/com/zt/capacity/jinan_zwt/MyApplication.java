package com.zt.capacity.jinan_zwt;


import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.zt.capacity.common.base.BaseApplication;
import cn.jpush.android.api.JPushInterface;


public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {

        //打包时候收集报错信息
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

        //获取设备唯一标识
        String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        identify = androidID + Build.SERIAL;

        //获取生产厂商
        brand = Build.MANUFACTURER;



        //友盟统计初始化
        /**注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，UMConfigure.init调用中appkey和channel参数请置为null）。
         */
        UMConfigure.init(this, "5bc979d8f1f55646200000d1", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setSecret(this, "5bc979d8f1f55646200000d1");
        // 将默认Session间隔时长改为40秒。
        MobclickAgent.setSessionContinueMillis(1000*40);
        MobclickAgent.openActivityDurationTrack(false);//禁止默认的页面统计功能，

        Log.e("pingpai.....",brand);

        //极光初始化

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
//        HMSAgent.connect(this, new ConnectHandler() {
//            @Override
//            public void onConnect(int rst) {
//                Log.e("huawei....","HMS connect end:"+rst);
//            }
//        });

        super.onCreate();
    }



    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
