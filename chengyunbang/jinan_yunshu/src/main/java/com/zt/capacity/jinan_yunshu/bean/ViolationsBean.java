package com.zt.capacity.jinan_yunshu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-05-08.
 */

public class ViolationsBean implements Serializable {

    private String date ; //违章时间
    private String area ; //违章地点
    private String archiveno; //  文书编号(不一定获取到)
    private String act; //违章行为
    private String code; //违章代码(仅供参考，不一定有值)
    private String fen; //  违章扣分(仅供参考，不一定有值)
    private String money; //违章罚款(仅供参考，不一定有值)
    private String handled; //  是否处理,1处理 0未处理 空未知
    private String wzcity; //违章城市(仅供参考，不一定有值)
    private long time; //违章城市(仅供参考，不一定有值)
    private String hphm;// 车牌号
    private String city;//城市
    private String province;//省份
    private String hpzl;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArchiveno() {
        return archiveno;
    }

    public void setArchiveno(String archiveno) {
        this.archiveno = archiveno;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getHandled() {
        return handled;
    }

    public void setHandled(String handled) {
        this.handled = handled;
    }

    public String getWzcity() {
        return wzcity;
    }

    public void setWzcity(String wzcity) {
        this.wzcity = wzcity;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getHphm() {
        return hphm;
    }

    public void setHphm(String hphm) {
        this.hphm = hphm;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getHpzl() {
        return hpzl;
    }

    public void setHpzl(String hpzl) {
        this.hpzl = hpzl;
    }
}