package com.zt.capacity.jinan_zwt.request.realtimeposition;

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
import com.zt.capacity.jinan_zwt.bean.Certificates;
import com.zt.capacity.jinan_zwt.bean.StatisticsInfo;
import com.zt.capacity.jinan_zwt.data.JN_ZWT_Url;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
            mRealTimePositionCarRuntime = new RealTimePosition(JN_ZWT_Url.SELECTALLMYCAR);
            return (IRealTimePosition) mRealTimePositionCarRuntime;
        }
        if (state == 2) {
            mRealTimePositionNunberByCarRuntime = new RealTimePosition(JN_ZWT_Url.SELECTALLMYCAR);
            return (IRealTimePosition) mRealTimePositionNunberByCarRuntime;
        }
        if (state == 3) {
            mRealTimePositionCar = new RealTimePosition(JN_ZWT_Url.CARS);
            return (IRealTimePosition) mRealTimePositionCar;
        }
        if (state == 8) {
            //实时位置统计
            mRealTimePositionCar = new RealTimePosition(JN_ZWT_Url.STATISTICSINFOTOTAL);
            return (IRealTimePosition) mRealTimePositionCar;
        }
        if (state == 9) {
            //济南实时位置离线车辆查询
            mRealTimePositionCar = new RealTimePosition(JN_ZWT_Url.CARRUNTIMEALL);
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

    /**
     * 按车牌号查询车辆实时位置
     *
     * @param carNumber
     * @param listener
     */
    @Override
    public void carRuntimeByCarNumber(String carNumber, final OnNetResultListener listener) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("carNumber", carNumber);
        body.put("current", 1);
        body.put("size", 5000);
        body.put("isOnline", "0");
        postQueryJson(body, new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                Log.e("dataxxxxxxxxxxxxx", jData);
                if (code == 0) {
                    try {
                        CarRuntimes carRuntimes = (CarRuntimes) gson.fromJson(jData, CarRuntimes.class);
                        listener.onResult(code, message, carRuntimes.getRecords());
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
                Log.e("fragment2", e.toString());
            }
        });
    }


    //统计信息
    @Override
    public void getStatisticsJN(final OnNetResultListener listener) {

        getQuery(new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                Log.d("dataxxxxxxxxxxxxx", jData);
                if (code == 0) {
                    try {
                        StatisticsInfo statisticsInfo = gson.fromJson(jData, StatisticsInfo.class);
                        listener.onResult(code, message, statisticsInfo);
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

    @Override
    public void getOfflineCarRuntime(final OnNetResultListener listener) {
        //分页信息

        getQuery(new MyNetCall() {
            @Override
            public void success(Integer code, Object data, String message, String token) {
                String jData = data.toString();
                Log.d("dataxxxxxxxxxxxxx", jData);
                if (code == 0) {
                    try {
                        List<CarRuntime> carRuntimes = gson.fromJson(jData, new TypeToken<List<CarRuntime>>() {
                        }.getType());
                        listener.onResult(code, message, carRuntimes);
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
