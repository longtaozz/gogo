package com.zt.capacity.jinan_yunshu.request.instruction;


import com.zt.capacity.common.data.listener.OnNetResultListener;

/**
 * Created by Administrator on 2018-04-18.
 */

public interface IInstructionManager {

    /**
     * 获取指令下发信息
     * @param carNumber：车牌
     * @param listener
     */
    void getHistoryInstruction(String carNumber, OnNetResultListener listener);

    /**
     * 下发指令
     * @param carPassPhoneNumber：车牌加SIM卡号（湘A1233,32342342）多个用;隔开
     * @param actionType：指令类型
     * @param actionValue：下发值
     * @param sendRemark：备注
     * @param listener
     */
    void sendInstruction(String carPassPhoneNumber, Integer actionType, Integer actionValue, String sendRemark, OnNetResultListener listener);

}
