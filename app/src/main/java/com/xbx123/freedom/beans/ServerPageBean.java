package com.xbx123.freedom.beans;

import java.util.List;

/**
 * Created by EricYuan on 2016/6/15.
 */
public class ServerPageBean {
    private String serverName;
    private String serverHead;
    private String serverTimes;
    private String serverIntro;
    private String serverStandard;
    private String serverIdCard;
    private String serverStar;
    private List<ServerCommentBean> commentList;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerHead() {
        return serverHead;
    }

    public void setServerHead(String serverHead) {
        this.serverHead = serverHead;
    }

    public String getServerTimes() {
        return serverTimes;
    }

    public void setServerTimes(String serverTimes) {
        this.serverTimes = serverTimes;
    }

    public String getServerIntro() {
        return serverIntro;
    }

    public void setServerIntro(String serverIntro) {
        this.serverIntro = serverIntro;
    }

    public String getServerStandard() {
        return serverStandard;
    }

    public void setServerStandard(String serverStandard) {
        this.serverStandard = serverStandard;
    }

    public String getServerIdCard() {
        return serverIdCard;
    }

    public void setServerIdCard(String serverIdCard) {
        this.serverIdCard = serverIdCard;
    }

    public String getServerStar() {
        return serverStar;
    }

    public void setServerStar(String serverStar) {
        this.serverStar = serverStar;
    }

    public List<ServerCommentBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<ServerCommentBean> commentList) {
        this.commentList = commentList;
    }
}
