package com.zt.capacity.jinan_zwt.request.richscan;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.MyNetCall;
import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.jinan_zwt.bean.Car;
import com.zt.capacity.jinan_zwt.bean.CarRuntime;
import com.zt.capacity.jinan_zwt.bean.CarRuntimes;
import com.zt.capacity.jinan_zwt.bean.Card;
import com.zt.capacity.jinan_zwt.bean.Certificates;
import com.zt.capacity.jinan_zwt.bean.SaoCard;
import com.zt.capacity.jinan_zwt.bean.StatisticsInfo;
import com.zt.capacity.jinan_zwt.data.JN_ZWT_Url;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/4/17.
 * 扫一扫
 */

public class RichScanManager extends Web implements IRichScanManager {
    private static RichScanManager richScanManager = null;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * code为1链接接有参数拦截，2没有参数拦截，3不拦截
     *
     * @param url
     */
    protected RichScanManager(String url) {
        super(url, 3);
    }


    public static IRichScanManager getInterface(Integer state) {
        if (state == 1) {
            //二维码查询核准证信息
            richScanManager = new RichScanManager(JN_ZWT_Url.CERTIFICATEPOINTCAR);
            return (IRichScanManager) richScanManager;
        }
        return null;
    }

    @Override
    public void richScanQrCode(String cardId, final OnNetResultListener listener) {
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("cardId", cardId);

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                if (code == 0) {
                    try {
                        SaoCard card = (SaoCard) gson.fromJson(jData, SaoCard.class);
                        listener.onResult(code, message, card);
                    } catch (Exception e) {
                        Log.e("myerr", e.toString());
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
