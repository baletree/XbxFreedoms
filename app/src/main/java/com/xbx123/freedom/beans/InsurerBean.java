package com.xbx123.freedom.beans;

import java.io.Serializable;

/**
 * Created by EricYuan on 2016/6/17.
 */
public class InsurerBean implements Serializable {
    private String insurerId;
    private String insurerName;
    private String insurerIdcard;
    private String insurerPhone;

    public String getInsurerPhone() {
        return insurerPhone;
    }

    public void setInsurerPhone(String insurerPhone) {
        this.insurerPhone = insurerPhone;
    }

    public String getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(String insurerId) {
        this.insurerId = insurerId;
    }

    public String getInsurerName() {
        return insurerName;
    }

    public void setInsurerName(String insurerName) {
        this.insurerName = insurerName;
    }

    public String getInsurerIdcard() {
        return insurerIdcard;
    }

    public void setInsurerIdcard(String insurerIdcard) {
        this.insurerIdcard = insurerIdcard;
    }
}
