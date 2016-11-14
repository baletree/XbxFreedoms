package com.xbx123.freedom.beans;

/**
 * Created by EricPeng on 2016/10/5.
 * 常用联系人JavaBean
 */
public class FrequeCotactsBean {
    private String contactId;
    private String contactName;
    private String contactPhone;
    private String contactDefault;

    public String getContactDefault() {
        return contactDefault;
    }

    public void setContactDefault(String contactDefault) {
        this.contactDefault = contactDefault;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
