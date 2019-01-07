package com.zt.capacity.baidumap.bean;

import java.io.Serializable;

/**
 * 周边信息
 * Created by Administrator on 2018/5/16.
 */

public class PoiBean implements Serializable {
    private String addr;    //地址信息
    private String cp;    //数据来源
    private String distance;    //离坐标点距离
    private String name;    //poi名称
    private String poiType;    //poi类型，如’ 办公大厦,商务大厦’
    //     point;	//poi坐标{x,y}https://blog.csdn.net/zhangt85/article/details/8816383
    private String tel;    //电话
    private String uid;    //poi唯一标识
    private String zip;    //邮编

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoiType() {
        return poiType;
    }

    public void setPoiType(String poiType) {
        this.poiType = poiType;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
