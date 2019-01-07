package com.zt.capacity.jinan_yunshu.request.car;


import com.zt.capacity.common.data.listener.OnNetResultListener;

/**
 * Created by Administrator on 2018-04-18.
 */

public interface ICarManager {

    /**
     * 获取车辆信息
     * @param carNumber：车牌
     * @param listener
     */
    void carByCarnumber(String carNumber, OnNetResultListener listener);


}
