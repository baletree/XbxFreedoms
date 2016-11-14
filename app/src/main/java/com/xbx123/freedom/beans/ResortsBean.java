package com.xbx123.freedom.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by EricYuan on 2016/6/2.
 * 景区-讲解员
 */
public class ResortsBean implements Serializable{
    private String resortsId;
    private String resortsName;
    private String resortsCode;
    private double lat;
    private double lon;
    private int resortsHourPrice;
    private int resortsTimePrice;
    private List<ServerBean> resortsServerList;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getResortsId() {
        return resortsId;
    }

    public void setResortsId(String resortsId) {
        this.resortsId = resortsId;
    }

    public String getResortsName() {
        return resortsName;
    }

    public void setResortsName(String resortsName) {
        this.resortsName = resortsName;
    }

    public String getResortsCode() {
        return resortsCode;
    }

    public void setResortsCode(String resortsCode) {
        this.resortsCode = resortsCode;
    }

    public int getResortsHourPrice() {
        return resortsHourPrice;
    }

    public void setResortsHourPrice(int resortsHourPrice) {
        this.resortsHourPrice = resortsHourPrice;
    }

    public int getResortsTimePrice() {
        return resortsTimePrice;
    }

    public void setResortsTimePrice(int resortsTimePrice) {
        this.resortsTimePrice = resortsTimePrice;
    }

    public List<ServerBean> getResortsServerList() {
        return resortsServerList;
    }

    public void setResortsServerList(List<ServerBean> resortsServerList) {
        this.resortsServerList = resortsServerList;
    }
}
