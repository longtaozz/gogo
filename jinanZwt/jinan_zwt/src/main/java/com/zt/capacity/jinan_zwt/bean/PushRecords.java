package com.zt.capacity.jinan_zwt.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 通知记录对象
 */
public class PushRecords{
    private List<PushRecord> records;

    public List<PushRecord> getRecords() {
        return records;
    }

    public void setRecords(List<PushRecord> records) {
        this.records = records;
    }
}