/** 
 *@Project: jinanzf 
 *@Author: YangXiao
 *@Date: 2018年11月23日 
 *@Copyright: ©2016-2022 http://www.zhtkj.com Inc. All rights reserved. 
 */    
package com.zt.capacity.jinan_zwt.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: PushRecordVo 
 * @Description: TODO
 * @author YangXiao
 * @date 2018年11月23日
 */

public class PushRecord implements Serializable{
    private Integer pushRecordId;//id
    private Integer pushGenreId;//推送类型id
    private String pushGenreName;//推送类型名字
    private String pushRecordTitle;//标题
    private String pushRecordContent;//内容
    private Long pushRecordTime;//推送时间
    private Integer userId;//推送人
	private Integer quiltUserId;//被推送人id
    private Integer pushState;//0、未读1、已读
    private Integer pushDataId;//推送详细内容id

	public Integer getQuiltUserId() {
		return quiltUserId;
	}

	public void setQuiltUserId(Integer quiltUserId) {
		this.quiltUserId = quiltUserId;
	}

	public Integer getPushRecordId() {
		return pushRecordId;
	}
	
	public void setPushRecordId(Integer pushRecordId) {
		this.pushRecordId = pushRecordId;
	}
	
	public Integer getPushGenreId() {
		return pushGenreId;
	}
	
	public void setPushGenreId(Integer pushGenreId) {
		this.pushGenreId = pushGenreId;
	}
	
	public String getPushGenreName() {
		return pushGenreName;
	}
	
	public void setPushGenreName(String pushGenreName) {
		this.pushGenreName = pushGenreName;
	}
	
	public String getPushRecordTitle() {
		return pushRecordTitle;
	}
	
	public void setPushRecordTitle(String pushRecordTitle) {
		this.pushRecordTitle = pushRecordTitle;
	}
	
	public String getPushRecordContent() {
		return pushRecordContent;
	}
	
	public void setPushRecordContent(String pushRecordContent) {
		this.pushRecordContent = pushRecordContent;
	}
	
	public Long getPushRecordTime() {
		return pushRecordTime;
	}
	
	public void setPushRecordTime(Long pushRecordTime) {
		this.pushRecordTime = pushRecordTime;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	

	public Integer getPushState() {
		return pushState;
	}
	
	public void setPushState(Integer pushState) {
		this.pushState = pushState;
	}
	
	public Integer getPushDataId() {
		return pushDataId;
	}
	
	public void setPushDataId(Integer pushDataId) {
		this.pushDataId = pushDataId;
	}
    
    
}
