package com.zt.capacity.jinan_zwt.bean;

import java.io.Serializable;

/**
 * 位置采集类
 */
public class ConsappVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**工地信息主键*/
	private Integer consappId;
	
	/**开始时间*/
	private String startTime;
	/**结束时间*/
	
	private String endTime;
	/**方量*/
	private String bulidGarbage;
	/**工地电子围栏*/
	private String gongGpsfence;
	/**施工单位Id*/
	private String constructUnitId;
	/**施工单位地址*/
	private String unitIdAddress;
	/**运输公司Id*/
	private Integer transId;
	/**路线fenceId*/
	private String luFenceId;
	/**消纳场fenceId*/
	private String xiaoNaFenceId;
	/**工地fenceId*/
	private String GongFenceId;
	/**工地主签或联运*/
	private String type;
	/**工地Guid*/
	private String gongGuidFence;
	/**路线GUid*/
	private String luGuidFence;
	/**消纳场Guid*/
	private String xiaoGuidfence;
	/**运输企业名称*/
	private String companyName;
	/**消纳场名称*/
	private String unloadingName;
	/**路线名称*/
	private String LuXianName;
	/**工程名称*/
	private String proName;
	/**工地地址*/
	private String proAddress;
	
	private String proType;//项目类型
	
	private String licType;//核准类型 licType 1：正式 2：临时
	
	private String cityId;//城市Id
	
	private String proBelong;//区域Id
	
	private String unitName;//施工单位
	
	private String licNumber;//运输备案编号
	
	private String buildId;//建筑ID
	
	private String buildName;//建筑单位名称
	
	private String buildRepresentative;//建筑单位负责人
	
	private String buildPhone;//单位电话
	
	private Integer constrId;
	
	private Integer responsId;
	

	private Integer emissionLoad;//排放量
	


	private String gdState;//项目状态 gdState 7：在建工地 8：停工工地 9：整顿工地 10：待建工地
	
	private GpsfenceBean gpsPos;//电子围栏

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getConsappId() {
		return consappId;
	}

	public void setConsappId(Integer consappId) {
		this.consappId = consappId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getBulidGarbage() {
		return bulidGarbage;
	}

	public void setBulidGarbage(String bulidGarbage) {
		this.bulidGarbage = bulidGarbage;
	}

	public String getGongGpsfence() {
		return gongGpsfence;
	}

	public void setGongGpsfence(String gongGpsfence) {
		this.gongGpsfence = gongGpsfence;
	}

	public String getConstructUnitId() {
		return constructUnitId;
	}

	public void setConstructUnitId(String constructUnitId) {
		this.constructUnitId = constructUnitId;
	}

	public String getUnitIdAddress() {
		return unitIdAddress;
	}

	public void setUnitIdAddress(String unitIdAddress) {
		this.unitIdAddress = unitIdAddress;
	}

	public Integer getTransId() {
		return transId;
	}

	public void setTransId(Integer transId) {
		this.transId = transId;
	}

	public String getLuFenceId() {
		return luFenceId;
	}

	public void setLuFenceId(String luFenceId) {
		this.luFenceId = luFenceId;
	}

	public String getXiaoNaFenceId() {
		return xiaoNaFenceId;
	}

	public void setXiaoNaFenceId(String xiaoNaFenceId) {
		this.xiaoNaFenceId = xiaoNaFenceId;
	}

	public String getGongFenceId() {
		return GongFenceId;
	}

	public void setGongFenceId(String gongFenceId) {
		GongFenceId = gongFenceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGongGuidFence() {
		return gongGuidFence;
	}

	public void setGongGuidFence(String gongGuidFence) {
		this.gongGuidFence = gongGuidFence;
	}

	public String getLuGuidFence() {
		return luGuidFence;
	}

	public void setLuGuidFence(String luGuidFence) {
		this.luGuidFence = luGuidFence;
	}

	public String getXiaoGuidfence() {
		return xiaoGuidfence;
	}

	public void setXiaoGuidfence(String xiaoGuidfence) {
		this.xiaoGuidfence = xiaoGuidfence;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUnloadingName() {
		return unloadingName;
	}

	public void setUnloadingName(String unloadingName) {
		this.unloadingName = unloadingName;
	}

	public String getLuXianName() {
		return LuXianName;
	}

	public void setLuXianName(String luXianName) {
		LuXianName = luXianName;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getProAddress() {
		return proAddress;
	}

	public void setProAddress(String proAddress) {
		this.proAddress = proAddress;
	}

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	public String getLicType() {
		return licType;
	}

	public void setLicType(String licType) {
		this.licType = licType;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getProBelong() {
		return proBelong;
	}

	public void setProBelong(String proBelong) {
		this.proBelong = proBelong;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getLicNumber() {
		return licNumber;
	}

	public void setLicNumber(String licNumber) {
		this.licNumber = licNumber;
	}

	public String getBuildId() {
		return buildId;
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
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

	public Integer getConstrId() {
		return constrId;
	}

	public void setConstrId(Integer constrId) {
		this.constrId = constrId;
	}

	public Integer getResponsId() {
		return responsId;
	}

	public void setResponsId(Integer responsId) {
		this.responsId = responsId;
	}

	public Integer getEmissionLoad() {
		return emissionLoad;
	}

	public void setEmissionLoad(Integer emissionLoad) {
		this.emissionLoad = emissionLoad;
	}

	public String getGdState() {
		return gdState;
	}

	public void setGdState(String gdState) {
		this.gdState = gdState;
	}

	public GpsfenceBean getGpsPos() {
		return gpsPos;
	}

	public void setGpsPos(GpsfenceBean gpsPos) {
		this.gpsPos = gpsPos;
	}
}
