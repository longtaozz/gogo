package com.zt.capacity.jinan_zwt.bean;


import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the zt_card database table.
 * 准运证
 */
public class Card implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer cardId;

	private Integer cityId;

	//施工单位id
	private int conId; 

	//施工地址
	private String constructionAddress; 

	//车辆行驶路线id
	private String gpsroadid;

	//运输结束时间
	private String licEndtime;

	//运输开始时间
	private String licStarttime;

	//工程名称
	private String proName;

	//运输车辆id
	private String tranCarids;

	//证件有效期止
	private String tranEndtime;

	//运输单位id
	private int tranId;

	//证件有效期起
	private String tranStarttime;

	//许可证类型
	private String cardType;

	//联运车辆id
	private String combinedCarids;

	//关联大证id
	private String consappId;

	//路线名称
	private String gpsroadName;

	//消纳场围栏Id
	private String xiaonaFenceId;

	//Guid
	private String certificateGUID;

	//添加时间
	private Date createTime;

	//核实状态
	private Integer checkstatus;

	//核准证后面的编号
	private String uplicNumber;

	//是否打印
	private Integer isprint;

	//是否显示
	private Integer modeltype;

	//核准证编号
	private String licNumber;

	//与交警同步过来的路线信息
	private String loadInfo;

	//0代表位同步，1代表已同步
	private Integer loadState;

	//消纳场名
	private String xiaonaFencename;

	//工地围栏Id
	private String gongFenceid;

	//工地名
	private String gongFencename;

	//工程地址
	private String proAddress;

	//车牌号
	private String carNumber;
	
	private String abbreviation;

	//建设单位
	private Buildunit buildunit;

	//车辆信息
	private Car car;

	//运输企业
	private TransportEnterprises transportEnterprises;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public int getConId() {
		return conId;
	}

	public void setConId(int conId) {
		this.conId = conId;
	}

	public String getConstructionAddress() {
		return constructionAddress;
	}

	public void setConstructionAddress(String constructionAddress) {
		this.constructionAddress = constructionAddress;
	}

	public String getGpsroadid() {
		return gpsroadid;
	}

	public void setGpsroadid(String gpsroadid) {
		this.gpsroadid = gpsroadid;
	}

	public String getLicEndtime() {
		return licEndtime;
	}

	public void setLicEndtime(String licEndtime) {
		this.licEndtime = licEndtime;
	}

	public String getLicStarttime() {
		return licStarttime;
	}

	public void setLicStarttime(String licStarttime) {
		this.licStarttime = licStarttime;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getTranCarids() {
		return tranCarids;
	}

	public void setTranCarids(String tranCarids) {
		this.tranCarids = tranCarids;
	}

	public String getTranEndtime() {
		return tranEndtime;
	}

	public void setTranEndtime(String tranEndtime) {
		this.tranEndtime = tranEndtime;
	}

	public int getTranId() {
		return tranId;
	}

	public void setTranId(int tranId) {
		this.tranId = tranId;
	}

	public String getTranStarttime() {
		return tranStarttime;
	}

	public void setTranStarttime(String tranStarttime) {
		this.tranStarttime = tranStarttime;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCombinedCarids() {
		return combinedCarids;
	}

	public void setCombinedCarids(String combinedCarids) {
		this.combinedCarids = combinedCarids;
	}

	public String getConsappId() {
		return consappId;
	}

	public void setConsappId(String consappId) {
		this.consappId = consappId;
	}

	public String getGpsroadName() {
		return gpsroadName;
	}

	public void setGpsroadName(String gpsroadName) {
		this.gpsroadName = gpsroadName;
	}

	public String getXiaonaFenceId() {
		return xiaonaFenceId;
	}

	public void setXiaonaFenceId(String xiaonaFenceId) {
		this.xiaonaFenceId = xiaonaFenceId;
	}

	public String getCertificateGUID() {
		return certificateGUID;
	}

	public void setCertificateGUID(String certificateGUID) {
		this.certificateGUID = certificateGUID;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getCheckstatus() {
		return checkstatus;
	}

	public void setCheckstatus(Integer checkstatus) {
		this.checkstatus = checkstatus;
	}

	public String getUplicNumber() {
		return uplicNumber;
	}

	public void setUplicNumber(String uplicNumber) {
		this.uplicNumber = uplicNumber;
	}

	public Integer getIsprint() {
		return isprint;
	}

	public void setIsprint(Integer isprint) {
		this.isprint = isprint;
	}

	public Integer getModeltype() {
		return modeltype;
	}

	public void setModeltype(Integer modeltype) {
		this.modeltype = modeltype;
	}

	public String getLicNumber() {
		return licNumber;
	}

	public void setLicNumber(String licNumber) {
		this.licNumber = licNumber;
	}

	public String getLoadInfo() {
		return loadInfo;
	}

	public void setLoadInfo(String loadInfo) {
		this.loadInfo = loadInfo;
	}

	public Integer getLoadState() {
		return loadState;
	}

	public void setLoadState(Integer loadState) {
		this.loadState = loadState;
	}

	public String getXiaonaFencename() {
		return xiaonaFencename;
	}

	public void setXiaonaFencename(String xiaonaFencename) {
		this.xiaonaFencename = xiaonaFencename;
	}

	public String getGongFenceid() {
		return gongFenceid;
	}

	public void setGongFenceid(String gongFenceid) {
		this.gongFenceid = gongFenceid;
	}

	public String getGongFencename() {
		return gongFencename;
	}

	public void setGongFencename(String gongFencename) {
		this.gongFencename = gongFencename;
	}

	public String getProAddress() {
		return proAddress;
	}

	public void setProAddress(String proAddress) {
		this.proAddress = proAddress;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public Buildunit getBuildunit() {
		return buildunit;
	}

	public void setBuildunit(Buildunit buildunit) {
		this.buildunit = buildunit;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public TransportEnterprises getTransportEnterprises() {
		return transportEnterprises;
	}

	public void setTransportEnterprises(TransportEnterprises transportEnterprises) {
		this.transportEnterprises = transportEnterprises;
	}
}