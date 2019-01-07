package com.zt.capacity.jinan_zwt.request.consapp;


import com.zt.capacity.common.data.listener.OnNetResultListener;

/**
 * 工地信息
 * Created by Administrator on 2018/4/17.
 */

public interface IConsappManager {

    /**
     * JN位置采集信息查询
     * @param listener
     * redius半径（m）
     */
    void getGatherListConsapp(double redius, OnNetResultListener listener);


    /**
     * JN位置采集上传信息
     * @param listener
     */
    void inputGatherData(String cityId, String consappId, String licNumber, String posX, String PosY, OnNetResultListener listener);

    /**
     * JN位置采集上传消纳场信息
     * @param listener
     */
    void inputUnlodGatherData(String cityId,String unloadingId, String licNumber, String posX, String PosY, OnNetResultListener listener);
    /**
     * 获取工地信息
     * @param consappId
     * @param listener
     */
    void getConsappInfo(Integer consappId,OnNetResultListener listener);

    /**
     * 获取消纳场信息
     * @param unloadingId
     * @param listener
     */
    void getUnloadingInfo(Integer unloadingId,OnNetResultListener listener);


    /**
     * 消纳场工地关联信息查询位置信息
     * @param gspAdress
     * @param listener
     */
    void getConsappPos(String gspAdress,OnNetResultListener listener);

}
