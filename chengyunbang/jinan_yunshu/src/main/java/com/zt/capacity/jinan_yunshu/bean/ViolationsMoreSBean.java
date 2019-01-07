package com.zt.capacity.jinan_yunshu.bean;

/**
 * Created by Administrator on 2018-05-09.
 */

public class ViolationsMoreSBean {

    private String resultcode;
    private String reason;
    private ViolationsMoreBean result;
    private int error_code;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ViolationsMoreBean getResult() {
        return result;
    }

    public void setResult(ViolationsMoreBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}
