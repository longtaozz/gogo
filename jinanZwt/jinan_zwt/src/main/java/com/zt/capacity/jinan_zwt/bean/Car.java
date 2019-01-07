package com.zt.capacity.jinan_zwt.bean;

import java.io.Serializable;


/**
 * 车辆信息
 * ClassName: Car 
 * @Description: TODO
 * @author Administrator
 * @date 2018年5月7日
 */
public class Car implements Serializable{
	
	//车辆主键
	private Integer carId;
	//联系电话
	private String carOwnersContact;
	//驾驶员
	private String carOwnersName;
	//车辆类型
	private String carType;
	//车架号
	private String chassisNumber;
	//发动机号
	private String engineNumber;
	//车牌号
	private String numberPlate;
	//sim卡号
	private String simNumber;
	//车辆品牌类
	private String carFactory;
	//城建号
	private String admitNumber;
	//设备提供商id
	private String equipmentProviders;
	//设备提供商
	private String equipmentName;
	//所属公司
	private Integer enterpriseId;
	//所属公司名称
	private String enterpriseName;
	//所属公司简称
	private String enterpriseAbbreviation;
	
	private String drivername;//驾驶员姓名
	
	private String drivertel;//联系电话
	
	private String drivingLicenseNumber;//驾驶证号

	private String register;//注册日期(行驶证)0： 未注册 1：已注册  2:已注销  3: 临时注销       车辆状态

	private String identification;//车辆识别代号
	
	private String carRegister;//0： 未注册 1：已注册  2:已注销  3: 临时注销       车辆状态

	private String color;//车辆颜色

	private String certificate;//道路运输从业资格证书

	private String quality;//核定载质量(㎏)

	private String airtightype;//密闭类型
	
	private String createTime;//车辆创建时间

	private String carNote;//备注
	
	private String parkGUID;

	public Integer getCarId() {
		return carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	public String getCarOwnersContact() {
		return carOwnersContact;
	}

	public void setCarOwnersContact(String carOwnersContact) {
		this.carOwnersContact = carOwnersContact;
	}

	public String getCarOwnersName() {
		return carOwnersName;
	}

	public void setCarOwnersName(String carOwnersName) {
		this.carOwnersName = carOwnersName;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getChassisNumber() {
		return chassisNumber;
	}

	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	public String getEngineNumber() {
		return engineNumber;
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public String getSimNumber() {
		return simNumber;
	}

	public void setSimNumber(String simNumber) {
		this.simNumber = simNumber;
	}

	public String getCarFactory() {
		return carFactory;
	}

	public void setCarFactory(String carFactory) {
		this.carFactory = carFactory;
	}

	public String getAdmitNumber() {
		return admitNumber;
	}

	public void setAdmitNumber(String admitNumber) {
		this.admitNumber = admitNumber;
	}

	public String getEquipmentProviders() {
		return equipmentProviders;
	}

	public void setEquipmentProviders(String equipmentProviders) {
		this.equipmentProviders = equipmentProviders;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getEnterpriseAbbreviation() {
		return enterpriseAbbreviation;
	}

	public void setEnterpriseAbbreviation(String enterpriseAbbreviation) {
		this.enterpriseAbbreviation = enterpriseAbbreviation;
	}

	public String getDrivername() {
		return drivername;
	}

	public void setDrivername(String drivername) {
		this.drivername = drivername;
	}

	public String getDrivertel() {
		return drivertel;
	}

	public void setDrivertel(String drivertel) {
		this.drivertel = drivertel;
	}

	public String getDrivingLicenseNumber() {
		return drivingLicenseNumber;
	}

	public void setDrivingLicenseNumber(String drivingLicenseNumber) {
		this.drivingLicenseNumber = drivingLicenseNumber;
	}

	public String getRegister() {
		return register;
	}

	public void setRegister(String register) {
		this.register = register;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getCarRegister() {
		return carRegister;
	}

	public void setCarRegister(String carRegister) {
		this.carRegister = carRegister;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getAirtightype() {
		return airtightype;
	}

	public void setAirtightype(String airtightype) {
		this.airtightype = airtightype;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCarNote() {
		return carNote;
	}

	public void setCarNote(String carNote) {
		this.carNote = carNote;
	}

	public String getParkGUID() {
		return parkGUID;
	}

	public void setParkGUID(String parkGUID) {
		this.parkGUID = parkGUID;
	}
}
