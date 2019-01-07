package com.zt.capacity.baidumap.request.baidmap;


import com.zt.capacity.common.data.listener.OnNetResultListener;

/**
 * Created by Administrator on 2018/5/9.
 */

public interface IBaiDu {

    /**
     * location:如"116.313393,40.04778"
     * 获取全景静态地图
     */
    void postPanoramaImage(String location, OnNetResultListener listener);

    /**
     * 逆地址解析接口
     * location:如"116.313393,40.04778"
     * pois：0为不需要周边，1为需要周边
     * @param pois
     * @param listener
     */
    void geocoder(String location, Integer pois, OnNetResultListener listener);
}
