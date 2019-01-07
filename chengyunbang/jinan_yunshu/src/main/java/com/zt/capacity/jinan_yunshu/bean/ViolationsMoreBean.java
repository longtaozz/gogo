package com.zt.capacity.jinan_yunshu.bean;

import java.util.List;

/**
 * Created by Administrator on 2018-05-08.
 */

public class ViolationsMoreBean {

    private String province;
    private String city;

    private String hphm;

    private String hpzl;

    private List<ViolationsBean> lists;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHphm() {
        return hphm;
    }

    public void setHphm(String hphm) {
        this.hphm = hphm;
    }

    public String getHpzl() {
        return hpzl;
    }

    public void setHpzl(String hpzl) {
        this.hpzl = hpzl;
    }

    public List<ViolationsBean> getLists() {
        return lists;
    }

    public void setLists(List<ViolationsBean> lists) {
        this.lists = lists;
    }



}
