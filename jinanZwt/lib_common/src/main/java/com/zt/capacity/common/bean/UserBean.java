package com.zt.capacity.common.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/17.
 */

public class UserBean implements Serializable {
    private int userId;//主键id
    private String username;//用户名
    private String password;//密码
    private String mobile;//	手机号
    private Integer cityId;//城市id
    private String name;//姓名
    private Integer role;//权限1：政府 2：工地 3：消纳场 4：运输公司 5：司机
    private Integer platformUid;//id，1：超级管理员；2：普通用户
    private Integer sex;//	性别 0：男，1，女

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getPlatformUid() {
        return platformUid;
    }

    public void setPlatformUid(Integer platformUid) {
        this.platformUid = platformUid;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
