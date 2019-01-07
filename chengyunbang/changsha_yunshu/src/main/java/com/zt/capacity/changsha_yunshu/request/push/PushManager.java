package com.zt.capacity.changsha_yunshu.request.push;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zt.capacity.changsha_yunshu.bean.PushRecords;
import com.zt.capacity.changsha_yunshu.data.ChangSha_YunShu_Url;
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
        super(url, 4);
    }


    public static IPushManager getInterface(Integer state) {
        if (state == 1) {
            //检查用户是否能接收通知
            pushManager = new PushManager(ChangSha_YunShu_Url.HWTOKENCHECK);
            return (IPushManager) pushManager;
        }
        if (state == 2) {
            //请求通知记录
            pushManager = new PushManager(ChangSha_YunShu_Url.GETPUSHRECORDVO);
            return (IPushManager) pushManager;
        }
        if (state == 3) {
            //修改阅读状态
            pushManager = new PushManager(ChangSha_YunShu_Url.UPPUSHRECORD);
            return (IPushManager) pushManager;
        }
        if (state == 4) {
            //查询未读数量
            pushManager = new PushManager(ChangSha_YunShu_Url.GETPUSHRECORDCOUNT);
            return (IPushManager) pushManager;
        }
        return null;
    }

    //检查推送用户
    @Override
    public void hwTokenCheck(Integer userId,String pushIdentify, final OnNetResultListener listener) {
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("userId", userId);
        page.put("pushIdentify", pushIdentify);

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

    //请求通知记录
    @Override
    public void getPushRecord(Integer current, Integer size, final OnNetResultListener listener) {
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("current", current);
        page.put("size", size);

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                if (code == 0) {
                    try {
                        PushRecords pushRecords = gson.fromJson(jData,PushRecords.class);
                        listener.onResult(code, message, pushRecords.getRecords());
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
    public void putRead(Integer pushRecordId,Integer pushState, final OnNetResultListener listener) {
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("pushRecordId", pushRecordId);
        page.put("pushState", pushState);

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                if (code == 0) {
                    listener.onResult(code, message, jData);
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
    public void getUnreadNumber(final OnNetResultListener listener) {
        Map<String, Object> page = new HashMap<String, Object>();
        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                if (code == 0) {
                    listener.onResult(code, message, jData);
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
