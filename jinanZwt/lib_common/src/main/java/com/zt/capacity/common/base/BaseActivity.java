package com.zt.capacity.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;


/**
 * woaini
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //动态注册广播
//        outBroad = new OutBaseActiviyBroad();
//        IntentFilter intentFilter = new IntentFilter("com.zt.capacity.base.BaseActivity");
//        registerReceiver(outBroad, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(outBroad);//注销广播
    }
}