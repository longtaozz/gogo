package com.zt.capacity.jinan_yunshu.bean;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the zt_data_action database table.
 *
 */
public class DataActionBeans implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<DataActionBean> records;

	public List<DataActionBean> getRecords() {
		return records;
	}

	public void setRecords(List<DataActionBean> records) {
		this.records = records;
	}
}