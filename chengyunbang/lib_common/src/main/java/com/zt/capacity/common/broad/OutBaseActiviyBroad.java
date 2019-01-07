package com.zt.capacity.common.broad;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 退出广播
 */
public class OutBaseActiviyBroad extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //接收发送过来的广播内容
        int closeAll = intent.getIntExtra("closeAll", 0);
        if (closeAll == 1) {
            ((Activity)context).finish();//销毁BaseActivity
        }
    }
}
