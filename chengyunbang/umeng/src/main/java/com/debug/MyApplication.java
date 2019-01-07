package com.debug;


import android.app.Application;

import com.hjq.umeng.UmengHelper;


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

        //友盟初始化
        UmengHelper.init(this);
    }
}
