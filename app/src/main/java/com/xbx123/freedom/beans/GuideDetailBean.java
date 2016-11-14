package com.xbx123.freedom.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/25.
 * 导游详情
 */
public class GuideDetailBean implements Serializable{
    private String guideMobile;
    private String guideName;
    private String guideHead;
    private String guideLanguage;
    private String guideSex;
    private String guideStar;
    private int guideAge;
    private String guideNumber;
    private String guideJobTimes;
    private double guideHourPrice;
    private double guideDayPrice;
    private String guideIntroduce;
    private String guideAlbum;
    private String guideSpecial;
    private String guideIdCard;

    public double getGuideHourPrice() {
        return guideHourPrice;
    }

    public void setGuideHourPrice(double guideHourPrice) {
        this.guideHourPrice = guideHourPrice;
    }

    public String getGuideSpecial() {
        return guideSpecial;
    }

    public void setGuideSpecial(String guideSpecial) {
        this.guideSpecial = guideSpecial;
    }

    public String getGuideIdCard() {
        return guideIdCard;
    }

    public void setGuideIdCard(String guideIdCard) {
        this.guideIdCard = guideIdCard;
    }

    public String getGuideIntroduce() {
        return guideIntroduce;
    }

    public void setGuideIntroduce(String guideIntroduce) {
        this.guideIntroduce = guideIntroduce;
    }

    public String getGuideJobTimes() {
        return guideJobTimes;
    }

    public void setGuideJobTimes(String guideJobTimes) {
        this.guideJobTimes = guideJobTimes;
    }

    public int getGuideAge() {
        return guideAge;
    }

    public void setGuideAge(int guideAge) {
        this.guideAge = guideAge;
    }

    public String getGuideLanguage() {
        return guideLanguage;
    }

    public void setGuideLanguage(String guideLanguage) {
        this.guideLanguage = guideLanguage;
    }

    public String getGuideSex() {
        return guideSex;
    }

    public void setGuideSex(String guideSex) {
        this.guideSex = guideSex;
    }

    public String getGuideAlbum() {
        return guideAlbum;
    }

    public void setGuideAlbum(String guideAlbum) {
        this.guideAlbum = guideAlbum;
    }

    public String getGuideMobile() {
        return guideMobile;
    }

    public void setGuideMobile(String guideMobile) {
        this.guideMobile = guideMobile;
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

    public String getGuideNumber() {
        return guideNumber;
    }

    public void setGuideNumber(String guideNumber) {
        this.guideNumber = guideNumber;
    }

    public double getGuideDayPrice() {
        return guideDayPrice;
    }

    public void setGuideDayPrice(double guideDayPrice) {
        this.guideDayPrice = guideDayPrice;
    }

    public String getGuideStar() {
        return guideStar;
    }

    public void setGuideStar(String guideStar) {
        this.guideStar = guideStar;
    }
}
