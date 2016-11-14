package com.xbx123.freedom.beans;

/**
 * Created by EricYuan on 2016/4/14.
 * 服务于用户的服务者
 */
public class MyServerInfoBean {
    private String serverPhone;
    private String serverHeadImg;
    private String serverName;
    private double serverLon;
    private double serverLat;
    private String serverIdcard;
    private String serverStarts;
    private long startTime;
    private int isArriveNearby;
    private long serverTimeDiffer;
    private int serverUnitPrice;//服务的单价
    private int serviceType;//服务的类型、按小时还是按次

    public long getServerTimeDiffer() {
        return serverTimeDiffer;
    }

    public void setServerTimeDiffer(long serverTimeDiffer) {
        this.serverTimeDiffer = serverTimeDiffer;
    }

    public int getServerUnitPrice() {
        return serverUnitPrice;
    }

    public void setServerUnitPrice(int serverUnitPrice) {
        this.serverUnitPrice = serverUnitPrice;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getIsArriveNearby() {
        return isArriveNearby;
    }

    public void setIsArriveNearby(int isArriveNearby) {
        this.isArriveNearby = isArriveNearby;
    }

    public String getServerPhone() {
        return serverPhone;
    }

    public void setServerPhone(String serverPhone) {
        this.serverPhone = serverPhone;
    }

    public String getServerHeadImg() {
        return serverHeadImg;
    }

    public void setServerHeadImg(String serverHeadImg) {
        this.serverHeadImg = serverHeadImg;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public double getServerLon() {
        return serverLon;
    }

    public void setServerLon(double serverLon) {
        this.serverLon = serverLon;
    }

    public double getServerLat() {
        return serverLat;
    }

    public void setServerLat(double serverLat) {
        this.serverLat = serverLat;
    }

    public String getServerIdcard() {
        return serverIdcard;
    }

    public void setServerIdcard(String serverIdcard) {
        this.serverIdcard = serverIdcard;
    }

    public String getServerStarts() {
        return serverStarts;
    }

    public void setServerStarts(String serverStarts) {
        this.serverStarts = serverStarts;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
