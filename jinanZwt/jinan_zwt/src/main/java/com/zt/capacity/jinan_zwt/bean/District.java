package com.zt.capacity.jinan_zwt.bean;

import java.io.Serializable;

import java.util.Date;


/**
 * The persistent class for the zt_district database table.
 * 区域
 */
public class District implements Serializable {
	private static final long serialVersionUID = 1L;

	private int districtId;

	private Integer cityId;

	private Date district_addTime;

	private String districtName;

	private String districtStatus;

	private String remark;
	
	private Integer districtOrder;
	
	private Integer districtNumber;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Date getDistrict_addTime() {
		return district_addTime;
	}

	public void setDistrict_addTime(Date district_addTime) {
		this.district_addTime = district_addTime;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getDistrictStatus() {
		return districtStatus;
	}

	public void setDistrictStatus(String districtStatus) {
		this.districtStatus = districtStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDistrictOrder() {
		return districtOrder;
	}

	public void setDistrictOrder(Integer districtOrder) {
		this.districtOrder = districtOrder;
	}

	public Integer getDistrictNumber() {
		return districtNumber;
	}

	public void setDistrictNumber(Integer districtNumber) {
		this.districtNumber = districtNumber;
	}
}