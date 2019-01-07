package com.zt.capacity.jinan_yunshu.request.ViolatedApi;


import com.zt.capacity.common.data.listener.OnNetResultListener;
import com.zt.capacity.jinan_yunshu.bean.CarBean;

/**
 * Created by Administrator on 2018/5/8.
 */

public interface IViolatedManager {


    //交通违章
    void peccancyApi(CarBean car, String city, OnNetResultListener listener);

    /**
     * nuber:前缀如：湘A
     * @param nuber
     */
    void peccancyNuber(String nuber, OnNetResultListener listener);



}
