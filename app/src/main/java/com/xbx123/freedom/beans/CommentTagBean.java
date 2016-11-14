package com.xbx123.freedom.beans;

import java.util.List;

/**
 * Created by EricYuan on 2016/6/30.
 * 评论标签的JavaBean
 */
public class CommentTagBean {
    List<ComItemTagBean> goodComList;
    List<ComItemTagBean> badComList;

    public List<ComItemTagBean> getGoodComList() {
        return goodComList;
    }

    public void setGoodComList(List<ComItemTagBean> goodComList) {
        this.goodComList = goodComList;
    }

    public List<ComItemTagBean> getBadComList() {
        return badComList;
    }

    public void setBadComList(List<ComItemTagBean> badComList) {
        this.badComList = badComList;
    }
}
