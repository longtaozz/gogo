package com.zt.capacity;



import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.zt.capacity.common.base.BaseApplication;
import com.zt.capacity.lib.jpush.push.JpushHelper;


public class MyApplication extends BaseApplication {
    public static JpushHelper jpushHelper;

    @Override
    public void onCreate() {
//        if (Utils.isAppDebug()) {
//            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
//            ARouter.openDebug();
//            ARouter.openLog();
//        }
//        ARouter.init(this);


        Log.e("contex.......1",this+"");

        //极光初始化
        jpushHelper=JpushHelper.init(this);

        //初始化baiduMap
        SDKInitializer.initialize(getApplicationContext());


        //打包时候收集报错信息
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

        super.onCreate();
    }

}
