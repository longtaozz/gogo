package com.zt.capacity.jinan_zwt.request.history;


import com.zt.capacity.common.data.listener.OnNetResultListener;

/**
 * Created by Administrator on 2018-04-18.
 */

public interface IHistoryManager {
    /**
     *    获取车辆历史轨迹
     * @param carNumber 车牌号码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param listener
     */
    void getHistory(String carNumber, String startTime, String endTime, OnNetResultListener listener);

    /**
     *    获取车辆历史轨迹
     * @param carNumber 车牌号码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param listener
     */
    void getHistory(String carNumber, String startTime, String endTime, Integer hour, OnNetResultListener listener);

    /**
     * 获取所有车辆信息
     * @param listener
     */
    void selectAllMyCar(OnNetResultListener listener);

    /**
     * 疑点查车
     * @param startTime：开始时间
     * @param endTime：结束时间
     * @param longitude：经度
     * @param latitude ：纬度
     * @param raidus：半径（米）
     * @param listener
     */
    void circular(String startTime, String endTime, Double longitude, Double latitude, Double raidus, OnNetResultListener listener);

    /**
     * 获取所有车辆信息
     * @param listener
     */
    void selectAllMyCarCS(OnNetResultListener listener);
}
