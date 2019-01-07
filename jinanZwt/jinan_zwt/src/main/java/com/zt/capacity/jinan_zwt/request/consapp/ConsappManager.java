package com.zt.capacity.jinan_zwt.request.consapp;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.MyNetCall;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.common.util.map.TransformationUtil;
import com.zt.capacity.jinan_zwt.bean.Consapp;
import com.zt.capacity.jinan_zwt.bean.ConsappVoS;
import com.zt.capacity.jinan_zwt.bean.GpsfenceBean;
import com.zt.capacity.jinan_zwt.bean.Unloading;
import com.zt.capacity.jinan_zwt.data.JN_ZWT_Url;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 工地
 * Created by Administrator on 2018/4/17.
 */

public class ConsappManager extends Web implements IConsappManager {
    private static ConsappManager consappManager = null;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * code为1链接接有参数拦截，2没有参数拦截，3不拦截
     *
     * @param url
     */
    protected ConsappManager(String url) {
        super(url, 3);
    }


    public static IConsappManager getInterface(Integer state) {
        if (state == 1) {
            //位置采集
            consappManager = new ConsappManager(JN_ZWT_Url.GETCONSAPPBYPOSITION);
            return (IConsappManager) consappManager;
        }
        if (state == 2) {
            //保存工地位置
            consappManager = new ConsappManager(JN_ZWT_Url.SAVECONSAPPBYPOSITION);
            return (IConsappManager) consappManager;
        }
        if (state == 3) {
            //id获取工地信息
            consappManager = new ConsappManager(JN_ZWT_Url.GETCONSAPPINFO);
            return (IConsappManager) consappManager;
        }
        if (state == 4) {
            //id获取消纳场信息
            consappManager = new ConsappManager(JN_ZWT_Url.GETUNLOADINGINFO);
            return (IConsappManager) consappManager;
        }
        if (state == 5) {
            //保存消纳场信息
            consappManager = new ConsappManager(JN_ZWT_Url.SAVEUNLOADBYPOSITION);
            return (IConsappManager) consappManager;
        }
        if (state == 6) {
            //gspAdress获取消纳场工地保存的位置信息
            consappManager = new ConsappManager(JN_ZWT_Url.GETCONSAPPPOS);
            return (IConsappManager) consappManager;
        }
        return null;
    }


    //JN位置采集信息查询
    @Override
    public void getGatherListConsapp(double redius, final OnNetResultListener listener) {

        LatLng latLng = getPoint();
        latLng = TransformationUtil.baiduToGps(latLng);
        //分页信息
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("current", 1);
        page.put("size", 1000);
        page.put("lng", latLng.longitude);
        page.put("lat", latLng.latitude);
//        page.put("lng", 116.892796);
//        page.put("lat", 36.584413);
        page.put("redius", redius);

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                if (code == 0) {
                    try {
                        ConsappVoS consappVos = gson.fromJson(jData, ConsappVoS.class);
                        listener.onResult(code, message, consappVos.getRecords());
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

    //消纳场位置上报
    @Override
    public void inputUnlodGatherData(String cityId, String unloadingId, String licNumber, String posX, String PosY, final OnNetResultListener listener) {
        String centerPos = posX + "," + PosY;
        //分页信息
        Map<String, Object> page = new HashMap<String, Object>();
        Log.e("cityId.......", cityId);
        page.put("cityId", cityId == null ? "" : cityId);
        page.put("unloadingId", unloadingId);
        page.put("centerPos", centerPos);
        page.put("licNumber", licNumber);

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                if (code == 0) {
                    listener.onResult(code, message, data.toString());
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


    //JN位置采集信息上传
    @Override
    public void inputGatherData(String cityId, String consappId, String licNumber, String posX, String PosY, final OnNetResultListener listener) {

        String centerPos = posX + "," + PosY;
        //分页信息
        Map<String, Object> page = new HashMap<String, Object>();
        Log.e("cityId.......", cityId);
        page.put("cityId", cityId == null ? "" : cityId);
        page.put("consappId", consappId);
        page.put("centerPos", centerPos);
        page.put("licNumber", licNumber);

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                if (code == 0) {
                    listener.onResult(code, message, data.toString());
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

    //id获取工地信息
    @Override
    public void getConsappInfo(Integer consappId, final OnNetResultListener listener) {
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("consappId", consappId);

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                if (code == 0) {
                    try {
                        Consapp consapp = gson.fromJson(jData, Consapp.class);
                        listener.onResult(code, message, consapp);
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

    //id获取消纳场信息
    @Override
    public void getUnloadingInfo(final Integer unloadingId, final OnNetResultListener listener) {
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("unloadingId", unloadingId);

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                if (code == 0) {
                    try {
                        Unloading unloading = gson.fromJson(jData, Unloading.class);
                        listener.onResult(code, message, unloading);
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

    //查询位置采集上传的位置信息
    @Override
    public void getConsappPos(String gspAdress, final OnNetResultListener listener) {
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("gpsAddress", gspAdress);

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                if (code == 0) {
                    try {
                        GpsfenceBean gpsfence = gson.fromJson(jData, GpsfenceBean.class);
                        listener.onResult(code, message, gpsfence);
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
