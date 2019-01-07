package com.zt.capacity.jinan_zwt.request.periphery;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.MyNetCall;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.jinan_zwt.bean.CarRuntime;
import com.zt.capacity.jinan_zwt.bean.GpsfenceParameter;
import com.zt.capacity.jinan_zwt.bean.SaoCard;
import com.zt.capacity.jinan_zwt.data.JN_ZWT_Url;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/4/17.
 * 周边违规
 */

public class PeripheryManager extends Web implements IPeripheryManager {
    private static PeripheryManager peripheryManager = null;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * code为1链接接有参数拦截，2没有参数拦截，3不拦截
     *
     * @param url
     */
    protected PeripheryManager(String url) {
        super(url, 3);
    }


    public static IPeripheryManager getInterface(Integer state) {
        if (state == 1) {
            //周边违规信息查询
            peripheryManager = new PeripheryManager(JN_ZWT_Url.GETAROUNDCARILLEGALINFO);
            return (IPeripheryManager) peripheryManager;
        }
        return null;
    }

    //距离查询周边违规数据
    @Override
    public void peripheryData(Integer km, LatLng latLng, final OnNetResultListener listener) {
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("redius", km * 1000);
        page.put("lng", latLng.longitude);
        page.put("lat", latLng.latitude);

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                if (code == 0) {
                    try {
                        List<CarRuntime> carRuntimes = gson.fromJson(jData,new TypeToken<List<CarRuntime>>(){}.getType());
                        listener.onResult(code, message, carRuntimes);
                    } catch (Exception e) {
                        Log.e("msg", e.toString());
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
