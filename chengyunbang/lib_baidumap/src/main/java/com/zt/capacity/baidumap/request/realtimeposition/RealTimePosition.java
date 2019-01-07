package com.zt.capacity.baidumap.request.realtimeposition;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zt.capacity.baidumap.bean.CarRuntimes;
import com.zt.capacity.baidumap.bean.Certificates;
import com.zt.capacity.baidumap.data.Map_Url;
import com.zt.capacity.common.bean.Car;
import com.zt.capacity.common.data.Web;
import com.zt.capacity.common.data.listener.MyNetCall;
import com.zt.capacity.common.data.listener.OnNetResultListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/4/17.
 */

public class RealTimePosition extends Web implements IRealTimePosition {
    private static RealTimePosition mRealTimePositionCarRuntime = null;
    private static RealTimePosition mRealTimePositionNunberByCarRuntime = null;
    private static RealTimePosition mRealTimePositionCar = null;
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * code为1链接接有参数拦截，2没有参数拦截，3不拦截
     *
     * @param url
     */
    protected RealTimePosition(String url) {
        super(url, 3);
    }


    public static IRealTimePosition LonginManager(Integer state) {
        if (state == 1) {
            mRealTimePositionCarRuntime = new RealTimePosition(Map_Url.SELECTALLMYCAR);
            return (IRealTimePosition) mRealTimePositionCarRuntime;
        }
        if (state == 2) {
            mRealTimePositionNunberByCarRuntime = new RealTimePosition(Map_Url.SELECTALLMYCAR);
            return (IRealTimePosition) mRealTimePositionNunberByCarRuntime;
        }
        if (state == 3) {
            mRealTimePositionCar = new RealTimePosition(Map_Url.CARS);
            return (IRealTimePosition) mRealTimePositionCar;
        }
        return null;
    }


    @Override
    public void getCarRuntime(final OnNetResultListener listener) {

        //分页信息
        Map<String, Object> page = new HashMap<String, Object>();
        page.put("current", 1);
        page.put("size", 5000);
        page.put("isOnline", "1");

        postQueryJson(page, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                Log.d("dataxxxxxxxxxxxxx", jData);
                if (code == 0) {
                    try {
                        CarRuntimes carRuntimes = (CarRuntimes) gson.fromJson(jData, CarRuntimes.class);
                        listener.onResult(code, message, carRuntimes.getRecords());
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

    /**
     * 按车牌号查询车辆实时位置
     *
     * @param carNumber
     * @param listener
     */
    @Override
    public void carRuntimeByCarNumber(String carNumber, String isOnline, final OnNetResultListener listener) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("carNumber", carNumber);
        body.put("current", 1);
        body.put("size", 5000);
        body.put("isOnline", isOnline);
        postQueryJson(body, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                Log.e("dataxxxxxxxxxxxxx", jData);
                if (code == 0) {
                    try {
                        if (jData != null) {
                            CarRuntimes carRuntimes = (CarRuntimes) gson.fromJson(jData, CarRuntimes.class);
                            listener.onResult(code, message, carRuntimes.getRecords());
                        }else{
                            listener.onResult(0, "无数据", null);
                        }
                    } catch (Exception e) {
                        Log.e("msg", e.toString());
                        listener.onResult(1, "无数据", null);
                    }


                } else {
                    Log.e("msg", jData);
                    listener.onResult(code, message, jData);
                }
            }

            @Override
            public void failed(Call call, IOException e) {
                listener.onResult(1, "服务器错误", "");
            }
        });
    }

    /**
     * 车牌查询车辆信息
     *
     * @param carNumber
     * @param listener
     */
    @Override
    public void carsByCarNumber(String carNumber, final OnNetResultListener listener) {

        //测试数据
//        listener.onResult(200, "成功", new Car(1,"湘AZ9230","123311111","432222","打算","dwqdqw","dadqwd",new Factory(1,"我的我的"),new Equipment(1,"防护未婚夫")));

        Map<String, String> body = new HashMap<String, String>();
        body.put("carNumber", carNumber);
        postQueryJson(body, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                Log.d("dataxxxxxxxxxxxxx", jData);
                if (code == 0) {
                    try {
                        Car car = (Car) gson.fromJson(jData, Car.class);
                        listener.onResult(code, message, car);
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
    public void paramByCarNumber(String carNumber, Integer geshu, final OnNetResultListener listener) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("current", 1);
        body.put("size", geshu);
        body.put("carNumber", carNumber);
        body.put("isValid", "1");
        postQueryJson(body, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                Log.d("dataxxxxxxxxxxxxx", jData);
                if (code == 0) {
                    try {
                        Certificates certificates = (Certificates) gson.fromJson(jData, Certificates.class);

                        listener.onResult(code, message, certificates.getRecords());

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
                Log.e("fragment2", e.toString());
                listener.onResult(1, "服务器错误", "");
            }
        });
    }

}
