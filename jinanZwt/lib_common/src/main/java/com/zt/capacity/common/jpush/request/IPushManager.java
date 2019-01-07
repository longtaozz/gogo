package com.zt.capacity.common.jpush.request;


import com.zt.capacity.common.data.listener.OnNetResultListener;

/**
 * 推送请求
 */

public interface IPushManager {

    /**
     * 添加用户
     * @param userId
     * @param hwToken
     * @param listener
     */
    void addPushUser(String hwToken, Integer userId,String pushIdentify,String pushBrand, OnNetResultListener listener);
}
