package com.zt.capacity.changsha_yunshu.request.push;


import com.zt.capacity.common.data.listener.OnNetResultListener;

/**
 * 推送请求
 */

public interface IPushManager {
    /**
     * 检查用户是否能接收通知
     * @param userId
     * @param listener
     */
    void hwTokenCheck(Integer userId, String pushIdentify, OnNetResultListener listener);

    /**
     * 请求通知记录
     * @param current   页数
     * @param size      个数
     * @param listener
     */
    void getPushRecord(Integer current, Integer size, OnNetResultListener listener);

    /**
     * 将状态标记为已读
     * @param pushRecordId
     * @param listener
     */
    void putRead(Integer pushRecordId, Integer pushState, OnNetResultListener listener);

    /**
     * 查询未读数量
     * @param listener
     */
    void getUnreadNumber(OnNetResultListener listener);
}
