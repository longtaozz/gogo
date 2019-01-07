package com.zt.capacity.jinan_zwt.bean;

import java.io.Serializable;

/**
 * 违规信息
 * Created by Administrator on 2018/5/14.
 */

public class AutoAnalysisdata implements Serializable {
    //违规id
    private Integer analysisId;
    //车牌号
    private String carNumber;
    //违规类型编号，违规类型（20008:未按线路，20001:超速行驶，20002:违规闯禁，20003:无证运输，20012:开箱重车，20004:违规卸土）
    private Integer eventType;
    //违规缩略图
    private String thumbnail;
    //
    private String eventTypeName;
    //违规时间
//    private Long vioStartime;
    //
    private Integer boxUp;
    //
    private Integer boxEmpty;
    //
    private Integer boxClose;
    //违规类型
    private String boxUpStatus;
    //
    private String boxEmptyStatus;
    //
    private String boxCloseStatus;

    public Integer getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Integer analysisId) {
        this.analysisId = analysisId;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public Integer getBoxUp() {
        return boxUp;
    }

    public void setBoxUp(Integer boxUp) {
        this.boxUp = boxUp;
    }

    public Integer getBoxEmpty() {
        return boxEmpty;
    }

    public void setBoxEmpty(Integer boxEmpty) {
        this.boxEmpty = boxEmpty;
    }

    public Integer getBoxClose() {
        return boxClose;
    }

    public void setBoxClose(Integer boxClose) {
        this.boxClose = boxClose;
    }

    public String getBoxUpStatus() {
        return boxUpStatus;
    }

    public void setBoxUpStatus(String boxUpStatus) {
        this.boxUpStatus = boxUpStatus;
    }

    public String getBoxEmptyStatus() {
        return boxEmptyStatus;
    }

    public void setBoxEmptyStatus(String boxEmptyStatus) {
        this.boxEmptyStatus = boxEmptyStatus;
    }

    public String getBoxCloseStatus() {
        return boxCloseStatus;
    }

    public void setBoxCloseStatus(String boxCloseStatus) {
        this.boxCloseStatus = boxCloseStatus;
    }
}
