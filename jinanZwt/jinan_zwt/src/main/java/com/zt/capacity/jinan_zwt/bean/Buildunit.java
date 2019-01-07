package com.zt.capacity.jinan_zwt.bean;

import java.io.Serializable;


import java.util.Date;



/**
 * The persistent class for the zt_buildunit database table.
 * 建设单位
 */
public class Buildunit implements Serializable {
	private static final long serialVersionUID = 1L;
	//主键ID
	private int buildId;
	//添加人
	private String buildAddPeople;
	//GUID
	private String buildGUID;
	//地址
	private String buildAddress;
	//添加时间
	private Date buildAddTime;
	//区域ID
	private int build_districtId;
	//文件地址
	private String buildFile;
	//名称
	private String buildName;
	//联系方式
	private String buildPhone;
	//法人代表
	private String buildRepresentative;
	//状态
	private String buildStatus;
	//修改人
	private String buildUpdatePeople;
	//修改时间
	private Date buildUpdateTime;
	//城市id，用来标识所属地区
	private Integer cityId;
	//备注
	private String remark;
	
	private String contacts;
	
	private String contactsPhone;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getBuildId() {
		return buildId;
	}

	public void setBuildId(int buildId) {
		this.buildId = buildId;
	}

	public String getBuildAddPeople() {
		return buildAddPeople;
	}

	public void setBuildAddPeople(String buildAddPeople) {
		this.buildAddPeople = buildAddPeople;
	}

	public String getBuildGUID() {
		return buildGUID;
	}

	public void setBuildGUID(String buildGUID) {
		this.buildGUID = buildGUID;
	}

	public String getBuildAddress() {
		return buildAddress;
	}

	public void setBuildAddress(String buildAddress) {
		this.buildAddress = buildAddress;
	}

	public Date getBuildAddTime() {
		return buildAddTime;
	}

	public void setBuildAddTime(Date buildAddTime) {
		this.buildAddTime = buildAddTime;
	}

	public int getBuild_districtId() {
		return build_districtId;
	}

	public void setBuild_districtId(int build_districtId) {
		this.build_districtId = build_districtId;
	}

	public String getBuildFile() {
		return buildFile;
	}

	public void setBuildFile(String buildFile) {
		this.buildFile = buildFile;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public String getBuildPhone() {
		return buildPhone;
	}

	public void setBuildPhone(String buildPhone) {
		this.buildPhone = buildPhone;
	}

	public String getBuildRepresentative() {
		return buildRepresentative;
	}

	public void setBuildRepresentative(String buildRepresentative) {
		this.buildRepresentative = buildRepresentative;
	}

	public String getBuildStatus() {
		return buildStatus;
	}

	public void setBuildStatus(String buildStatus) {
		this.buildStatus = buildStatus;
	}

	public String getBuildUpdatePeople() {
		return buildUpdatePeople;
	}

	public void setBuildUpdatePeople(String buildUpdatePeople) {
		this.buildUpdatePeople = buildUpdatePeople;
	}

	public Date getBuildUpdateTime() {
		return buildUpdateTime;
	}

	public void setBuildUpdateTime(Date buildUpdateTime) {
		this.buildUpdateTime = buildUpdateTime;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactsPhone() {
		return contactsPhone;
	}

	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}
}