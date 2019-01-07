/**
 * @Project: jinanzf-service
 * @Author: chenlijun
 * @Date: 2018年11月19日
 * @Copyright: ©2016-2022 http://www.zhtkj.com Inc. All rights reserved.
 */
package com.zt.capacity.jinan_zwt.bean;

import java.util.Date;

/**
 * ClassName: SaoCard 
 * @Description: 扫二维码获取的车辆核准证信息实体类
 * @author chenlijun
 * @date 2018年11月19日
 */
public class SaoCard {
    private Integer cardId;
    //工程名称
    private String proName;
    //运输单位id
    private int tranId;
    //运输单位企业名称
    private String enterpriseName;
    //工程地址
    private String proAddress;
    //车辆主键
    private Integer carId;
    //车牌号
    private String numberPlate;
    //城建号
    private String admitNumber;
    //证件有效期起
    private String tranStarttime;
    //证件有效期止
    private String tranEndtime;
    //与交警同步过来的路线信息
    private String loadInfo;
    //0代表位同步，1代表已同步
    private Integer loadState;
    //消纳场名
    private String xiaonaFencename;
    //工地名
    private String gongFencename;
    private String abbreviation;

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public int getTranId() {
        return tranId;
    }

    public void setTranId(int tranId) {
        this.tranId = tranId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getProAddress() {
        return proAddress;
    }

    public void setProAddress(String proAddress) {
        this.proAddress = proAddress;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getAdmitNumber() {
        return admitNumber;
    }

    public void setAdmitNumber(String admitNumber) {
        this.admitNumber = admitNumber;
    }

    public String getTranStarttime() {
        return tranStarttime;
    }

    public void setTranStarttime(String tranStarttime) {
        this.tranStarttime = tranStarttime;
    }

    public String getTranEndtime() {
        return tranEndtime;
    }

    public void setTranEndtime(String tranEndtime) {
        this.tranEndtime = tranEndtime;
    }

    public String getLoadInfo() {
        return loadInfo;
    }

    public void setLoadInfo(String loadInfo) {
        this.loadInfo = loadInfo;
    }

    public Integer getLoadState() {
        return loadState;
    }

    public void setLoadState(Integer loadState) {
        this.loadState = loadState;
    }

    public String getXiaonaFencename() {
        return xiaonaFencename;
    }

    public void setXiaonaFencename(String xiaonaFencename) {
        this.xiaonaFencename = xiaonaFencename;
    }

    public String getGongFencename() {
        return gongFencename;
    }

    public void setGongFencename(String gongFencename) {
        this.gongFencename = gongFencename;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
