package com.xbx123.freedom.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;

/**
 * Created by EricYuan on 2016/6/2.
 * 景区的领路人和讲解员
 */
public class ServerBean implements Serializable {
    private String serverId;
    private String serverName;
    private double serverLat;
    private double serverLog;
    private String serverHeadImg;
    private String serverPrice;
    private String serverTime;
    private String serverStars;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Double getServerLat() {
        return serverLat;
    }

    public void setServerLat(double serverLat) {
        this.serverLat = serverLat;
    }

    public Double getServerLog() {
        return serverLog;
    }

    public void setServerLog(double serverLog) {
        this.serverLog = serverLog;
    }

    public String getServerHeadImg() {
        return serverHeadImg;
    }

    public void setServerHeadImg(String serverHeadImg) {
        this.serverHeadImg = serverHeadImg;
    }

    public String getServerPrice() {
        return serverPrice;
    }

    public void setServerPrice(String serverPrice) {
        this.serverPrice = serverPrice;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getServerStars() {
        return serverStars;
    }

    public void setServerStars(String serverStars) {
        this.serverStars = serverStars;
    }

}
