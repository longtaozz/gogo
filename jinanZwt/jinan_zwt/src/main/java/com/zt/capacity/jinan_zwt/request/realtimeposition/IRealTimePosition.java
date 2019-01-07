package com.zt.capacity.jinan_zwt.request.realtimeposition;


import com.zt.capacity.common.data.listener.OnNetResultListener;

/**
 * Created by Administrator on 2018/4/17.
 */

public interface IRealTimePosition {

    /**
     * 查询实时车辆位置信息
     * @param listener
     */
    void getCarRuntime(OnNetResultListener listener);

    /**
     * 按车牌号查询车辆实时位置
     * @param carNumber
     * @param listener
     */
    void carRuntimeByCarNumber(String carNumber, OnNetResultListener listener);


    /**
     * 车辆编号查询车辆信息
     * @param listener
     * @param carNumber
     */
    void carsByCarNumber(String carNumber, OnNetResultListener listener);
    /**
     * 车牌查询小证信息
     * @param carNumber
     * @param listener
     */
    void paramByCarNumber(String carNumber, Integer geshu, OnNetResultListener listener);





    /**
     * 查询统计信息
     * @param listener
     */
    void getStatisticsJN(OnNetResultListener listener);

    /**
     * 查询实时车辆离线位置信息
     * @param listener
     */
    void getOfflineCarRuntime(OnNetResultListener listener);
}
