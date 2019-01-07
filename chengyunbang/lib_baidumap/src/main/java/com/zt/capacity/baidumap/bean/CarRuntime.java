package com.zt.capacity.baidumap.bean;

import java.io.Serializable;


public class CarRuntime implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**开关厢状态*/
	private byte boxClose;
	/**空重状态*/
	private byte boxEmpty;
	/**举升状态*/
	private byte boxUp;
	/**车牌号码*/
	private String carNumber;
	//违规状态
	private String carState;
	//司机id
	private String driverId;
	//是否处于管控
	private Integer isControl;
	/**方向*/
	private float gpsDirect;
	/**经度*/
	private Double gpsPosX;
	/**纬度*/
	private Double gpsPosY;
	/**速度*/
	private float gpsSpeed;
	/**举升状态 1平放，2举升*/
	private String boxUpStatus;
	/**载货状态 1空车，2重车*/
	private String boxEmptyStatus;
	/** 开关厢状态 1关闭，2开厢*/
	private String boxCloseStatus;
	/**手机号*/
	private String devPhone;
	/**最后上报时间*/
	private String sendDatetime;
	/**状态*/
	private String onlineState;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public byte getBoxClose() {
		return boxClose;
	}

	public void setBoxClose(byte boxClose) {
		this.boxClose = boxClose;
	}

	public byte getBoxEmpty() {
		return boxEmpty;
	}

	public void setBoxEmpty(byte boxEmpty) {
		this.boxEmpty = boxEmpty;
	}

	public byte getBoxUp() {
		return boxUp;
	}

	public void setBoxUp(byte boxUp) {
		this.boxUp = boxUp;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getCarState() {
		return carState;
	}

	public void setCarState(String carState) {
		this.carState = carState;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public Integer getIsControl() {
		return isControl;
	}

	public void setIsControl(Integer isControl) {
		this.isControl = isControl;
	}

	public float getGpsDirect() {
		return gpsDirect;
	}

	public void setGpsDirect(float gpsDirect) {
		this.gpsDirect = gpsDirect;
	}

	public Double getGpsPosX() {
		return gpsPosX;
	}

	public void setGpsPosX(Double gpsPosX) {
		this.gpsPosX = gpsPosX;
	}

	public Double getGpsPosY() {
		return gpsPosY;
	}

	public void setGpsPosY(Double gpsPosY) {
		this.gpsPosY = gpsPosY;
	}

	public float getGpsSpeed() {
		return gpsSpeed;
	}

	public void setGpsSpeed(float gpsSpeed) {
		this.gpsSpeed = gpsSpeed;
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

	public String getDevPhone() {
		return devPhone;
	}

	public void setDevPhone(String devPhone) {
		this.devPhone = devPhone;
	}

	public String getSendDatetime() {
		return sendDatetime;
	}

	public void setSendDatetime(String sendDatetime) {
		this.sendDatetime = sendDatetime;
	}

	public String getOnlineState() {
		return onlineState;
	}

	public void setOnlineState(String onlineState) {
		this.onlineState = onlineState;
	}


}
