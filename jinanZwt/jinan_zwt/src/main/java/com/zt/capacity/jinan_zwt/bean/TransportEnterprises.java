package com.zt.capacity.jinan_zwt.bean;

import java.io.Serializable;


/**
 *运输企业
 */
public class TransportEnterprises implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//主键id
	private int enterpriseId;
	//城市ID
	private Integer cityId;
	//联系人电话
	private String contactPhone;
	//联系人
	private String contacts;
	//企业简称
	private String enterpriseAbbreviation;
	//运输车辆总数量
	private String enterpriseCarCount;
	//企业管理人员数量
	private String enterpriseManagerNumber;
	//企业名称
	private String enterpriseName;
	//停车场围栏附件
	private String fenceEnclosure;
	//总吨位
	private String grossTonnage;
	//法人联系电话
	private String legalPersonPhone;
	//法人代表
	private String legalRepresentative;
	//新车数量
	private String newcarNumber;
	//办公场地面积
	private String officeSpace;
	//旧车数量
	private String oldcarNumber;
	//停车场面积
	private String parkingArea;
	//停车场电子围栏
	private String parkingLotFence;

	private String region;//区域名
	
	private String districtId;//区域ID
	//注册资金
	private String registeredCapital;
	//公司地址
	private String address;
	//业务描述
	private String performanceDescription;
	
	private String enterpriseState; //企业状态 0：以准入 1：已变更 2：已注销
	
	private String rpprovalNumber;//核准证编号
	
	private District district;//区域
	
	private Integer CompanyCarCount;//运输公司车辆数

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getEnterpriseAbbreviation() {
		return enterpriseAbbreviation;
	}

	public void setEnterpriseAbbreviation(String enterpriseAbbreviation) {
		this.enterpriseAbbreviation = enterpriseAbbreviation;
	}

	public String getEnterpriseCarCount() {
		return enterpriseCarCount;
	}

	public void setEnterpriseCarCount(String enterpriseCarCount) {
		this.enterpriseCarCount = enterpriseCarCount;
	}

	public String getEnterpriseManagerNumber() {
		return enterpriseManagerNumber;
	}

	public void setEnterpriseManagerNumber(String enterpriseManagerNumber) {
		this.enterpriseManagerNumber = enterpriseManagerNumber;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getFenceEnclosure() {
		return fenceEnclosure;
	}

	public void setFenceEnclosure(String fenceEnclosure) {
		this.fenceEnclosure = fenceEnclosure;
	}

	public String getGrossTonnage() {
		return grossTonnage;
	}

	public void setGrossTonnage(String grossTonnage) {
		this.grossTonnage = grossTonnage;
	}

	public String getLegalPersonPhone() {
		return legalPersonPhone;
	}

	public void setLegalPersonPhone(String legalPersonPhone) {
		this.legalPersonPhone = legalPersonPhone;
	}

	public String getLegalRepresentative() {
		return legalRepresentative;
	}

	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}

	public String getNewcarNumber() {
		return newcarNumber;
	}

	public void setNewcarNumber(String newcarNumber) {
		this.newcarNumber = newcarNumber;
	}

	public String getOfficeSpace() {
		return officeSpace;
	}

	public void setOfficeSpace(String officeSpace) {
		this.officeSpace = officeSpace;
	}

	public String getOldcarNumber() {
		return oldcarNumber;
	}

	public void setOldcarNumber(String oldcarNumber) {
		this.oldcarNumber = oldcarNumber;
	}

	public String getParkingArea() {
		return parkingArea;
	}

	public void setParkingArea(String parkingArea) {
		this.parkingArea = parkingArea;
	}

	public String getParkingLotFence() {
		return parkingLotFence;
	}

	public void setParkingLotFence(String parkingLotFence) {
		this.parkingLotFence = parkingLotFence;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getRegisteredCapital() {
		return registeredCapital;
	}

	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPerformanceDescription() {
		return performanceDescription;
	}

	public void setPerformanceDescription(String performanceDescription) {
		this.performanceDescription = performanceDescription;
	}

	public String getEnterpriseState() {
		return enterpriseState;
	}

	public void setEnterpriseState(String enterpriseState) {
		this.enterpriseState = enterpriseState;
	}

	public String getRpprovalNumber() {
		return rpprovalNumber;
	}

	public void setRpprovalNumber(String rpprovalNumber) {
		this.rpprovalNumber = rpprovalNumber;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Integer getCompanyCarCount() {
		return CompanyCarCount;
	}

	public void setCompanyCarCount(Integer companyCarCount) {
		CompanyCarCount = companyCarCount;
	}
}
