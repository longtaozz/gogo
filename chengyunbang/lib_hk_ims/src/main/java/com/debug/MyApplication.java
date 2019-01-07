package com.debug;


import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;

import com.hik.mcrsdk.MCRSDK;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.hik.mcrsdk.talk.TalkClientSDK;
import com.hikvision.sdk.VMSNetSDK;


/**
 * 单独测试开发用application
 * 也是一个使用示例
 * @author lt
 * @time 2018/12/14 11:34
 *
 **/
public class MyApplication extends Application {
    private static MyApplication ins;

    @Override
    public void onCreate() {
        super.onCreate();


        //海康视频初始化
        ins = this;
        MCRSDK.init();
        // 初始化RTSP
        RtspClient.initLib();
        MCRSDK.setPrint(1, null);
        // 初始化语音对讲
        TalkClientSDK.initLib();
        // SDK初始化
        VMSNetSDK.init(this);
    }


    public static MyApplication getIns() {
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
}
