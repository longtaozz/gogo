package com.zt.capacity.jinan_zwt.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-05-22.
 */

public class VersionBean implements Serializable {


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersionMilepost() {
        return versionMilepost;
    }

    public void setVersionMilepost(String versionMilepost) {
        this.versionMilepost = versionMilepost;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionCodeBefore() {
        return versionCodeBefore;
    }

    public void setVersionCodeBefore(String versionCodeBefore) {
        this.versionCodeBefore = versionCodeBefore;
    }

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }

    public String getVersionBig() {
        return versionBig;
    }

    public void setVersionBig(String versionBig) {
        this.versionBig = versionBig;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUpdateTitle() {
        return updateTitle;
    }

    public void setUpdateTitle(String updateTitle) {
        this.updateTitle = updateTitle;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public String getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(String statusNumber) {
        this.statusNumber = statusNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateParams() {
        return updateParams;
    }

    public void setUpdateParams(String updateParams) {
        this.updateParams = updateParams;
    }

      private String appId;///**主键ID*/
      private String appCode;  /**客户端设备id字符串，如：app.android.version.key*/
      private String appName; /**客户端设备名字*/
      private String versionMilepost;  /**是否是一个里程牌式的版本，默认为0，是则为1*/
      private String versionCode;  /**版本号*/
      private String versionCodeBefore;/**上一个版本号*/
      private String versionType;/**版本类型，0选择更新，1强制更新*/
      private String versionBig; /**新版本大小*/
      private String downloadUrl;  /**更新地址*/
      private String updateTitle; /**升级信息简要*/
      private String updateMessage;/**升级信息详情*/
      private String statusNumber;/**版本状态  1:最新版本，0：之前老版本*/
      private String createTime;  /**版本创建时间*/
      private String updateParams;/**添加扩展*/

}
