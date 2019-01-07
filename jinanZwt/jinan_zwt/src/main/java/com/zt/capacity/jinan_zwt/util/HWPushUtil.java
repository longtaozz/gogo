package com.zt.capacity.jinan_zwt.util;

import android.os.Handler;
import android.os.Message;

import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.jinan_zwt.MyApplication;
import com.zt.capacity.jinan_zwt.request.push.IPushManager;
import com.zt.capacity.jinan_zwt.request.push.PushManager;


public class HWPushUtil {
    /**
     * 检查账户是否能够进行推送
     * @param handler
     */
    public static void TokenCheck(final Handler handler){
        IPushManager pushManager= PushManager.getInterface(1);
        pushManager.hwTokenCheck(Web.getgUserID(), MyApplication.identify, new OnNetResultListener() {
            @Override
            public void onResult(int state, String msg, Object body) {
                Message message=new Message();
                if(state == 0){
                    message.what=100001;
                    message.obj=body;
                }else {
                    message.what=1;
                    message.obj=msg;
                }
                handler.sendMessage(message);
            }
        });
    }
}
