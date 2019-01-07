package com.debug;


import android.app.Application;

import com.zt.capacity.lib.jpush.push.JpushHelper;


/**
 * 使用示例
 * @author lt
 * @time 2018/12/17 13:10
 *
 **/
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //极光初始化
        JpushHelper.init(this);
    }
}
