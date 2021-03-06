package com.zt.capacity.jinan_zwt.request.baidmap;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.MyNetCall;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.jinan_zwt.bean.BaseBaiduReturn;

import java.io.IOException;

import okhttp3.Call;

/**
 * 百度api
 */

public class BaiDu extends Web implements IBaiDu {
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public static IBaiDu getInterface(Integer state) {
        IBaiDu baidu = null;
        if (state == 1) {
            //百度静态全景?ak=您的ak&width=512&height=256&location=116.313393,40.04778&fov=180
            baidu = new BaiDu(Web.baiImage);
        }
        if (state == 2) {
            //逆地址解析location="116.313393,40.04778"&pois=0";
            baidu = new BaiDu(Web.geocoder);
        }
        return (IBaiDu) baidu;
    }


    /**
     * code为1链接接有参数拦截，2没有参数拦截，3不拦截
     *
     * @param url
     */
    protected BaiDu(String url) {
        super(url, 4);
    }

    /**
     * ak=evj7lVGGWbAmiw3sG5ppb8G9kcBG5nh6&width=512&height=256&location=116.313393,40.04778&fov=180
     * location String 116.313393,40.04778
     *
     * @param location
     */
    @Override
    public void postPanoramaImage(String location, final OnNetResultListener listener) {
        String url = "width=190&height=160&location=" + location + "&fov=180";

        getQueryIamge(url, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {

                Log.e("dddddddddddddaaa", data.toString());
                if (code == 0) {

                    byte[] image = (byte[]) data;
                    Log.e("dddddddddddddaaa", image + "");
                    Log.e("dddddddddddddaaa", image.length + "");
                    if (image.length > 500) {
                        listener.onResult(0, message, image);
                    } else {
                        listener.onResult(2, "没请求到图片", "");
                    }
                } else {
                    listener.onResult(code, message, data);
                }
            }

            @Override
            public void failed(Call call, IOException e) {
                listener.onResult(1, "请求错误", "");
            }
        });


    }

    @Override
    public void geocoder(String location, Integer pois, final OnNetResultListener listener) {
        String url = "location=" + location + "&pois=" + pois;
        getQuery(url, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                if (code == 0) {
                    Log.e("datadddd", data.toString());
                    try {
                        BaseBaiduReturn baseBaiduReturn = (BaseBaiduReturn) gson.fromJson(data.toString(),BaseBaiduReturn.class);
                        Log.e("jiexi11111111",baseBaiduReturn.getResult().toString());
                        listener.onResult(code, message, baseBaiduReturn);
                    } catch (Exception e) {
                        Log.e("msg", e.getMessage());
                        listener.onResult(1, "解析错误", "");
                    }
                } else {
                    listener.onResult(1, message, "");
                }
            }

            @Override
            public void failed(Call call, IOException e) {
                listener.onResult(1, "请求错误", "");
            }
        });
    }
}
