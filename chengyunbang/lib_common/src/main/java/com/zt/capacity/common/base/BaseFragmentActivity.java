package com.zt.capacity.common.base;

import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.hjq.umeng.UmengHelper;
import com.zt.capacity.common.broad.OutBaseActiviyBroad;


/**
 * fragmentActivity
 * @author lt
 *
 **/
public class BaseFragmentActivity extends FragmentActivity {
    OutBaseActiviyBroad outBroad;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.activity_title_bar));

        outBroad = new OutBaseActiviyBroad();
        IntentFilter intentFilter = new IntentFilter("com.zt.capacity.base.BaseActivity");
        registerReceiver(outBroad, intentFilter);
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        MobclickAgent.onResume(this);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this);
//    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(outBroad);//注销广播
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

}
