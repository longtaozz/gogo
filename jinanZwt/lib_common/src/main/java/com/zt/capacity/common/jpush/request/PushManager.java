package com.zt.capacity.common.jpush.request;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.MyNetCall;
import com.zt.capacity.common.data.listener.OnNetResultListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 推送请求
 */

public class PushManager extends Web implements IPushManager {
    private static PushManager pushManager = null;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * code为1链接接有参数拦截，2没有参数拦截，3不拦截
     *
     * @param url
     */
    protected PushManager(String url) {
        super(url, 3);
    }


    public static IPushManager getInterface(Integer state) {
        if (state == 1) {
            //添加用户
            pushManager = new PushManager(Web.projectJN + "push/addPushUser");
            return (IPushManager) pushManager;
        }
        return null;
    }


    //添加推送用户
    @Override
    public void addPushUser(String hwToken,Integer userId,String pushIdentify,String pushBrand, final OnNetResultListener listener) {
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("userId", userId);
        page.put("hwToken", hwToken);
        page.put("pushIdentify", pushIdentify);
        page.put("pushBrand", pushBrand);

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                if (code == 0) {
                    try {
                        listener.onResult(code, message, jData);
                    } catch (Exception e) {
                        Log.e("msg", "解析错误");
                        listener.onResult(1, "解析错误", null);
                    }


                } else {
                    Log.e("msg", jData);
                    listener.onResult(code, message, jData);
                }

            }

            @Override
            public void failed(Call call, IOException e) {
                Log.e("err", e.toString());
                listener.onResult(1, "服务器错误", "");
            }
        });
    }
}
