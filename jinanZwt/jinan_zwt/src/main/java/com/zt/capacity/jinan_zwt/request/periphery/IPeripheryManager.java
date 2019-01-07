package com.zt.capacity.jinan_zwt.request.periphery;


import com.baidu.mapapi.model.LatLng;
import com.zt.capacity.common.data.listener.OnNetResultListener;

/**
 * Created by Administrator on 2018/4/17.
 * 周边违规
 */

public interface IPeripheryManager {
    /**
     * 周边违规数据请求
     * @param km    半径（km）
     * @param latLng    坐标
     * @param listener
     */
    void peripheryData(Integer km, LatLng latLng, OnNetResultListener listener);
}
