package com.zt.capacity.jinan_zwt.request.gpsFence;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.MyNetCall;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.jinan_zwt.bean.GpsfenceParameter;
import com.zt.capacity.jinan_zwt.data.JN_ZWT_Url;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 围栏信息
 * Created by Administrator on 2018/4/17.
 */

public class GpsFenceManager extends Web implements IGpsFenceManager {
    private static GpsFenceManager gpsFenceManager = null;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * code为1链接接有参数拦截，2没有参数拦截，3不拦截
     *
     * @param url
     */
    protected GpsFenceManager(String url) {
        super(url, 3);
    }


    public static IGpsFenceManager gpsFenceManager(Integer state) {
        if (state == 1) {
            //消纳场围栏请求
            gpsFenceManager = new GpsFenceManager(JN_ZWT_Url.GETUNLOADINGGPSFENCE);
            return (IGpsFenceManager) gpsFenceManager;
        }
        if (state == 2) {
            //工地围栏请求
            gpsFenceManager = new GpsFenceManager(JN_ZWT_Url.GETCONSAPPGPSFENCE);
            return (IGpsFenceManager) gpsFenceManager;
        }
        return null;
    }

    @Override
    public void getUnloadingGpsfence(String unloadingName, final OnNetResultListener listener) {
        //分页信息
        Map<String, String> page = new HashMap<String, String>();
        page.put("unloadingName", unloadingName);

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                Log.d("dataxxxxxxxxxxxxx", jData);
                if (code == 0) {
                    try {
                        List<GpsfenceParameter> gpsfenceParameters = gson.fromJson(jData, new TypeToken<List<GpsfenceParameter>>() {
                        }.getType());
                        listener.onResult(code, message, gpsfenceParameters);
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

    @Override
    public void getConsappGpsfence(String proName, final OnNetResultListener listener) {
//分页信息
        Map<String, String> page = new HashMap<String, String>();
        page.put("proName", proName);

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                Log.d("dataxxxxxxxxxxxxx", jData);
                if (code == 0) {
                    try {
                        List<GpsfenceParameter> gpsfenceParameters = gson.fromJson(jData, new TypeToken<List<GpsfenceParameter>>() {
                        }.getType());
                        listener.onResult(code, message, gpsfenceParameters);
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
