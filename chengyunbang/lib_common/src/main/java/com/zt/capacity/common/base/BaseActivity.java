package com.zt.capacity.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hjq.umeng.UmengHelper;
import com.zt.capacity.common.broad.OutBaseActiviyBroad;

/**
 * 通用的activit设置
 * @author lt
 *
 **/
public class BaseActivity extends Activity {

    OutBaseActiviyBroad outBroad;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //动态注册广播
//        outBroad = new OutBaseActiviyBroad();
//        IntentFilter intentFilter = new IntentFilter("com.zt.capacity.base.BaseActivity");
//        registerReceiver(outBroad, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengHelper.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengHelper.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(outBroad);//注销广播
    }
}
