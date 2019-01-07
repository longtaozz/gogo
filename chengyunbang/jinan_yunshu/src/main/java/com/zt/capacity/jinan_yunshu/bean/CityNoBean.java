package com.zt.capacity.jinan_yunshu.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018-05-09.
 */

public class CityNoBean implements Serializable {

       private String reason;
       private CityBean result;
       private String error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public CityBean getResult() {
        return result;
    }

    public void setResult(CityBean result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
}
