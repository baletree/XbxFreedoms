package com.xbx123.freedom.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/7.
 * 导游预约列表JavaBean
 */
public class GuideReBean implements Serializable{
    private int totalSize;
    private int currentPage;
    private int pageSize;
    private List<GuideReBean> guideReBeanList;
    //以下是导游个人信息
    private String guideCardNum; //导游证号
    private String guideCompanydNum; //导游公司编号
    private String guideLeadercode; //导游领队编号
    private String guideName;
    private String guideHead;
    private String guideSex;
    private double guideDayPrice;
    private double guideHourPrice;
    private String guideLanguage;
    private String guideJobTime;
    private String guideCompany;
    private int guideAge;
    private int guideStars;

    public String getGuideLeadercode() {
        return guideLeadercode;
    }

    public void setGuideLeadercode(String guideLeadercode) {
        this.guideLeadercode = guideLeadercode;
    }

    public String getGuideCompanydNum() {
        return guideCompanydNum;
    }

    public void setGuideCompanydNum(String guideCompanydNum) {
        this.guideCompanydNum = guideCompanydNum;
    }

    public List<GuideReBean> getGuideReBeanList() {
        return guideReBeanList;
    }

    public void setGuideReBeanList(List<GuideReBean> guideReBeanList) {
        this.guideReBeanList = guideReBeanList;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getGuideCardNum() {
        return guideCardNum;
    }

    public void setGuideCardNum(String guideCardNum) {
        this.guideCardNum = guideCardNum;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    public String getGuideHead() {
        return guideHead;
    }

    public void setGuideHead(String guideHead) {
        this.guideHead = guideHead;
    }

    public String getGuideSex() {
        return guideSex;
    }

    public void setGuideSex(String guideSex) {
        this.guideSex = guideSex;
    }

    public double getGuideDayPrice() {
        return guideDayPrice;
    }

    public void setGuideDayPrice(double guideDayPrice) {
        this.guideDayPrice = guideDayPrice;
    }

    public double getGuideHourPrice() {
        return guideHourPrice;
    }

    public void setGuideHourPrice(double guideHourPrice) {
        this.guideHourPrice = guideHourPrice;
    }

    public String getGuideLanguage() {
        return guideLanguage;
    }

    public void setGuideLanguage(String guideLanguage) {
        this.guideLanguage = guideLanguage;
    }

    public String getGuideJobTime() {
        return guideJobTime;
    }

    public void setGuideJobTime(String guideJobTime) {
        this.guideJobTime = guideJobTime;
    }

    public String getGuideCompany() {
        return guideCompany;
    }

    public void setGuideCompany(String guideCompany) {
        this.guideCompany = guideCompany;
    }

    public int getGuideAge() {
        return guideAge;
    }

    public void setGuideAge(int guideAge) {
        this.guideAge = guideAge;
    }

    public int getGuideStars() {
        return guideStars;
    }

    public void setGuideStars(int guideStars) {
        this.guideStars = guideStars;
    }
}
