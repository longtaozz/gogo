package com.zt.capacity.jinan_zwt.bean;

import java.io.Serializable;
import java.util.Date;



public class Unloading implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer unloadingId;

	private String buildunitId;

	private Integer cityId;

	private String districtId;
	
	private String unloadingGUID;

	private String enclosureId;

	private String unloadingType;
	
	private String unloadingAddpeople;

	private String unloadingAddress;

	private String unloadingAddtime;

	private String unloadingBackfill;

	private String unloadingBegintime;

	private String unloadingCelaningcar;

	private String unloadingCertificate;

	private String unloadingEndtime;

	private String unloadingFile;

	private String unloadingHydropower;

	private String unloadingName;

	private String unloadingRailing;

	private String unloadingRoad;

	private String unloadingSize;

	private String unloadingSquirtnumber;

	private String unloadingStatus;
	
	private String gpsAddress;
	
	/*新加字段：
	1.消纳场编号：X+年+月+区号+001，如：X170901001* unloading_num
	2.公示时间* publicity_time
	//3.启用时间（相当于开始时间）* “按原来字段”
	//4.封场时间（相当于结束时间） “按原来字段”
	5.设计容量（数字，单位：m3）* design_capacity
	6.运距（数字，单位：km）* distance
	7.占地面积（数字，单位：亩）* area_covered
	8.远程喷淋设备（数字，单位：台）* sprinkler_equipment
	9.视频监测设备（数字，单位：台）* video_equipment
	10.扬尘监测设备（数字，单位：台）* dust_equipment
	11.裸土覆盖（绿化）（数字，单位：亩）bare_cover
	12.可用消纳量（数字，单位m3）able_capacity
	 */
	
	public String unloadingNum;
	
	public String publicityTime;
	
	public String designCapacity;
	
	public String distance;
	
	public String areaCovered;
	
	public String sprinklerEquipment;
	
	public String videoEquipment;
	
	public String dustEquipment;
	
	public String bareCover;
	
	public String ableCapacity;
	
	//负责人
	private String buildRepresentative;
		
	//联系电话
	private String buildPhone;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getUnloadingId() {
		return unloadingId;
	}

	public void setUnloadingId(Integer unloadingId) {
		this.unloadingId = unloadingId;
	}

	public String getBuildunitId() {
		return buildunitId;
	}

	public void setBuildunitId(String buildunitId) {
		this.buildunitId = buildunitId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getUnloadingGUID() {
		return unloadingGUID;
	}

	public void setUnloadingGUID(String unloadingGUID) {
		this.unloadingGUID = unloadingGUID;
	}

	public String getEnclosureId() {
		return enclosureId;
	}

	public void setEnclosureId(String enclosureId) {
		this.enclosureId = enclosureId;
	}

	public String getUnloadingType() {
		return unloadingType;
	}

	public void setUnloadingType(String unloadingType) {
		this.unloadingType = unloadingType;
	}

	public String getUnloadingAddpeople() {
		return unloadingAddpeople;
	}

	public void setUnloadingAddpeople(String unloadingAddpeople) {
		this.unloadingAddpeople = unloadingAddpeople;
	}

	public String getUnloadingAddress() {
		return unloadingAddress;
	}

	public void setUnloadingAddress(String unloadingAddress) {
		this.unloadingAddress = unloadingAddress;
	}

	public String getUnloadingAddtime() {
		return unloadingAddtime;
	}

	public void setUnloadingAddtime(String unloadingAddtime) {
		this.unloadingAddtime = unloadingAddtime;
	}

	public String getUnloadingBackfill() {
		return unloadingBackfill;
	}

	public void setUnloadingBackfill(String unloadingBackfill) {
		this.unloadingBackfill = unloadingBackfill;
	}

	public String getUnloadingBegintime() {
		return unloadingBegintime;
	}

	public void setUnloadingBegintime(String unloadingBegintime) {
		this.unloadingBegintime = unloadingBegintime;
	}

	public String getUnloadingCelaningcar() {
		return unloadingCelaningcar;
	}

	public void setUnloadingCelaningcar(String unloadingCelaningcar) {
		this.unloadingCelaningcar = unloadingCelaningcar;
	}

	public String getUnloadingCertificate() {
		return unloadingCertificate;
	}

	public void setUnloadingCertificate(String unloadingCertificate) {
		this.unloadingCertificate = unloadingCertificate;
	}

	public String getUnloadingEndtime() {
		return unloadingEndtime;
	}

	public void setUnloadingEndtime(String unloadingEndtime) {
		this.unloadingEndtime = unloadingEndtime;
	}

	public String getUnloadingFile() {
		return unloadingFile;
	}

	public void setUnloadingFile(String unloadingFile) {
		this.unloadingFile = unloadingFile;
	}

	public String getUnloadingHydropower() {
		return unloadingHydropower;
	}

	public void setUnloadingHydropower(String unloadingHydropower) {
		this.unloadingHydropower = unloadingHydropower;
	}

	public String getUnloadingName() {
		return unloadingName;
	}

	public void setUnloadingName(String unloadingName) {
		this.unloadingName = unloadingName;
	}

	public String getUnloadingRailing() {
		return unloadingRailing;
	}

	public void setUnloadingRailing(String unloadingRailing) {
		this.unloadingRailing = unloadingRailing;
	}

	public String getUnloadingRoad() {
		return unloadingRoad;
	}

	public void setUnloadingRoad(String unloadingRoad) {
		this.unloadingRoad = unloadingRoad;
	}

	public String getUnloadingSize() {
		return unloadingSize;
	}

	public void setUnloadingSize(String unloadingSize) {
		this.unloadingSize = unloadingSize;
	}

	public String getUnloadingSquirtnumber() {
		return unloadingSquirtnumber;
	}

	public void setUnloadingSquirtnumber(String unloadingSquirtnumber) {
		this.unloadingSquirtnumber = unloadingSquirtnumber;
	}

	public String getUnloadingStatus() {
		return unloadingStatus;
	}

	public void setUnloadingStatus(String unloadingStatus) {
		this.unloadingStatus = unloadingStatus;
	}

	public String getGpsAddress() {
		return gpsAddress;
	}

	public void setGpsAddress(String gpsAddress) {
		this.gpsAddress = gpsAddress;
	}

	public String getUnloadingNum() {
		return unloadingNum;
	}

	public void setUnloadingNum(String unloadingNum) {
		this.unloadingNum = unloadingNum;
	}

	public String getPublicityTime() {
		return publicityTime;
	}

	public void setPublicityTime(String publicityTime) {
		this.publicityTime = publicityTime;
	}

	public String getDesignCapacity() {
		return designCapacity;
	}

	public void setDesignCapacity(String designCapacity) {
		this.designCapacity = designCapacity;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getAreaCovered() {
		return areaCovered;
	}

	public void setAreaCovered(String areaCovered) {
		this.areaCovered = areaCovered;
	}

	public String getSprinklerEquipment() {
		return sprinklerEquipment;
	}

	public void setSprinklerEquipment(String sprinklerEquipment) {
		this.sprinklerEquipment = sprinklerEquipment;
	}

	public String getVideoEquipment() {
		return videoEquipment;
	}

	public void setVideoEquipment(String videoEquipment) {
		this.videoEquipment = videoEquipment;
	}

	public String getDustEquipment() {
		return dustEquipment;
	}

	public void setDustEquipment(String dustEquipment) {
		this.dustEquipment = dustEquipment;
	}

	public String getBareCover() {
		return bareCover;
	}

	public void setBareCover(String bareCover) {
		this.bareCover = bareCover;
	}

	public String getAbleCapacity() {
		return ableCapacity;
	}

	public void setAbleCapacity(String ableCapacity) {
		this.ableCapacity = ableCapacity;
	}

	public String getBuildRepresentative() {
		return buildRepresentative;
	}

	public void setBuildRepresentative(String buildRepresentative) {
		this.buildRepresentative = buildRepresentative;
	}

	public String getBuildPhone() {
		return buildPhone;
	}

	public void setBuildPhone(String buildPhone) {
		this.buildPhone = buildPhone;
	}
}