package com.zt.capacity.baidumap.request.history;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zt.capacity.baidumap.bean.CarHistoryBean;
import com.zt.capacity.baidumap.data.Map_Url;
import com.zt.capacity.common.bean.Car;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.MyNetCall;
import com.zt.capacity.common.data.listener.OnNetResultListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 车辆信息
 */

public class HistoryManager extends Web implements IHistoryManager {
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public static IHistoryManager getInterface(Integer state) {
        HistoryManager historyManager = null;
        if (state == 1) {
            //查询历史轨迹
            historyManager = new HistoryManager(Map_Url.CARHISTORYS);
        }
        if (state == 2) {
            //查询所有车辆信息
            historyManager = new HistoryManager(Map_Url.SELECTNUMBERPLATE);
        }
        return (IHistoryManager) historyManager;
    }

    /**
     * code为1链接接有参数拦截，2没有参数拦截，3不拦截
     *
     * @param
     * @param
     */
    protected HistoryManager(String url) {
        super(url, 3);
    }

    @Override
    public void getHistory(String carNumber, String startTime, String endTime, final OnNetResultListener listener) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("carNumber", carNumber);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        postQueryJson(map, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {


                if (code == 0) {
                    Log.e("baseBean", data.toString());
                    List<CarHistoryBean> carHistoryBean = null;
                    try {
                         carHistoryBean =
                                gson.fromJson(data.toString(),
                                        new TypeToken<List<CarHistoryBean>>() {
                                        }.getType());
                    }catch (Exception ex){
                        ex.printStackTrace();
                        Log.e("ex",ex.toString());
                    };
//                    CarHistoryBeans carHistoryBean =
//                            gson.fromJson(data.toString(),
//                                    CarHistoryBeans.class);
                    listener.onResult(0, message, carHistoryBean);
                } else {

                    listener.onResult(1, message, null);
                }
            }

            @Override
            public void failed(Call call, IOException e) {
                Log.d("exe", "失败");
                listener.onResult(1, "失败", null);
            }
        });
    }

    @Override
    public void getHistory(String carNumber, String startTime, String endTime,Integer hour, final OnNetResultListener listener) {
        Map<String, String> map = new HashMap<String, String>();
        if(hour==0){
            map.put("hour", "");
        }else{
            map.put("hour", hour+"");
        }
        map.put("carNumber", carNumber);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        postQueryJson(map, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {


                if (code == 0) {
                    Log.e("baseBean", data.toString());
                    List<CarHistoryBean> carHistoryBean = null;
                    try {
                        carHistoryBean =
                                gson.fromJson(data.toString(),
                                        new TypeToken<List<CarHistoryBean>>() {
                                        }.getType());
                    }catch (Exception ex){
                        ex.printStackTrace();
                        Log.e("ex",ex.toString());
                    };
//                    CarHistoryBeans carHistoryBean =
//                            gson.fromJson(data.toString(),
//                                    CarHistoryBeans.class);
                    listener.onResult(0, message, carHistoryBean);
                } else {

                    listener.onResult(1, message, null);
                }
            }

            @Override
            public void failed(Call call, IOException e) {
                Log.d("exe", "失败");
                listener.onResult(1, "失败", null);
            }
        });
    }

    @Override
    public void selectAllMyCar(final OnNetResultListener listener) {
        Map<String, Integer> page = new HashMap<String, Integer>();
        page.put("current", 1);
        page.put("size", 4000);
        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                Log.e("data", jData);
                if (code == 0) {
                    try {
                        List<Car> cars=gson.fromJson(jData, new TypeToken<List<Car>>(){}.getType());
                        listener.onResult(code, message, cars);
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
                listener.onResult(1, "服务器错误", null);
            }
        });

    }
}
