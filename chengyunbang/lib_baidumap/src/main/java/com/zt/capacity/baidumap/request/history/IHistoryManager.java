package com.zt.capacity.baidumap.request.history;


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


}
