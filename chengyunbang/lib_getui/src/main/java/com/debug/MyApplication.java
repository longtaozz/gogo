package com.debug;


import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zt.capacity.lib.getui.MainActivity;


/**
 * 单独测试开发用application
 *
 * @author lt
 * @time 2018/12/14 11:34
 **/
public class MyApplication extends Application {
    private static final String TAG = "GetuiSdkDemo";

    private static DemoHandler handler;
    public static MainActivity demoActivity;

    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "DemoApplication onCreate");

        if (handler == null) {
            handler = new DemoHandler();
        }
    }

    public static void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public static class DemoHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (demoActivity != null) {
                        payloadData.append((String) msg.obj);
                        payloadData.append("\n");
                    }
                    break;

                case 1:
                    if (demoActivity != null) {
                    }
                    break;
            }
        }
    }

}
