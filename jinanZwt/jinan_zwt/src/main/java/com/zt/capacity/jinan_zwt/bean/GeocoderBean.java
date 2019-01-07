package com.zt.capacity.jinan_zwt.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 逆地址信息
 * Created by Administrator on 2018/5/16.
 */

public class GeocoderBean implements Serializable {
    private String formatted_address;	///结构化地址信息
    private String business;	//所在商圈信息，如 "人民大学,中关村,苏州街"
    private String sematic_description;//位置描述
    private Integer cityCode;//城市编号
    private List<PoiBean> pois;  //附近poi

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getSematic_description() {
        return sematic_description;
    }

    public void setSematic_description(String sematic_description) {
        this.sematic_description = sematic_description;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public List<PoiBean> getPois() {
        return pois;
    }

    public void setPois(List<PoiBean> pois) {
        this.pois = pois;
    }
}
