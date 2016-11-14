package com.xbx123.freedom.jsonparse;

import com.xbx123.freedom.beans.FrequeCotactsBean;
import com.xbx123.freedom.beans.GuideDetailBean;
import com.xbx123.freedom.beans.GuideReBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/10/10.
 */

public class GuideParse {
    public static GuideReBean getGuideList(String json) {
        GuideReBean guideReBean = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            guideReBean = new GuideReBean();
            if (UtilParse.checkTag(jsonObject, "totalPage"))
                guideReBean.setTotalSize(jsonObject.getInt("totalPage"));
            if (UtilParse.checkTag(jsonObject, "currentPage"))
                guideReBean.setCurrentPage(jsonObject.getInt("currentPage"));
            if (UtilParse.checkTag(jsonObject, "pageSize"))
                guideReBean.setPageSize(jsonObject.getInt("pageSize"));
            if (!UtilParse.checkTag(jsonObject, "data"))
                return null;
            List<GuideReBean> guideReBeanList = null;
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if (jsonArray != null && jsonArray.length() != 0) {
                guideReBeanList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    GuideReBean grb = new GuideReBean();
                    JSONObject job = (JSONObject) jsonArray.get(i);
                    if (UtilParse.checkTag(job, "name"))
                        grb.setGuideName(job.getString("name"));
                    if (UtilParse.checkTag(job, "sex"))
                        grb.setGuideSex(job.getString("sex"));
                    if (UtilParse.checkTag(job, "dayprice"))
                        grb.setGuideDayPrice(job.getDouble("dayprice"));
                    if (UtilParse.checkTag(job, "hourprice"))
                        grb.setGuideHourPrice(job.getDouble("hourprice"));
                    if (UtilParse.checkTag(job, "language"))
                        grb.setGuideLanguage(job.getString("language"));
                    if (UtilParse.checkTag(job, "jobtime"))
                        grb.setGuideJobTime(job.getString("jobtime"));
                    if (UtilParse.checkTag(job, "guidecode"))
                        grb.setGuideCardNum(job.getString("guidecode"));
                    if (UtilParse.checkTag(job, "corpname"))
                        grb.setGuideCompany(job.getString("corpname"));
                    if (UtilParse.checkTag(job, "corpcode"))
                        grb.setGuideCompanydNum(job.getString("corpcode"));
                    if (UtilParse.checkTag(job, "leadercode"))
                        grb.setGuideLeadercode(job.getString("leadercode"));
                    if (UtilParse.checkTag(job, "age"))
                        grb.setGuideAge(job.getInt("age"));
                    if (UtilParse.checkTag(job, "star"))
                        grb.setGuideStars(job.getInt("star"));
                    if (UtilParse.checkTag(job, "photo"))
                        grb.setGuideHead(job.getString("photo"));
                    guideReBeanList.add(grb);
                }
                guideReBean.setGuideReBeanList(guideReBeanList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return guideReBean;
    }

    public static GuideDetailBean getGuideDetailInfo(String json) {
        GuideDetailBean guideDetailBean = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            guideDetailBean = new GuideDetailBean();
            if (UtilParse.checkTag(jsonObject, "name"))
                guideDetailBean.setGuideName(jsonObject.getString("name"));
            if (UtilParse.checkTag(jsonObject, "headpic"))
                guideDetailBean.setGuideHead(jsonObject.getString("headpic"));
            if (UtilParse.checkTag(jsonObject, "language"))
                guideDetailBean.setGuideLanguage(jsonObject.getString("language"));
            if (UtilParse.checkTag(jsonObject, "sex"))
                guideDetailBean.setGuideSex(jsonObject.getString("sex"));
            if (UtilParse.checkTag(jsonObject, "introduction"))
                guideDetailBean.setGuideIntroduce(jsonObject.getString("introduction"));
            if (UtilParse.checkTag(jsonObject, "age"))
                guideDetailBean.setGuideAge(jsonObject.getInt("age"));
            if (UtilParse.checkTag(jsonObject, "star"))
                guideDetailBean.setGuideStar(jsonObject.getString("star"));
            if (UtilParse.checkTag(jsonObject, "guidecode"))
                guideDetailBean.setGuideNumber(jsonObject.getString("guidecode"));
            if (UtilParse.checkTag(jsonObject, "jobtime"))
                guideDetailBean.setGuideJobTimes(jsonObject.getString("jobtime"));
            if (UtilParse.checkTag(jsonObject, "dayprice"))
                guideDetailBean.setGuideDayPrice(jsonObject.getDouble("dayprice"));
            if (UtilParse.checkTag(jsonObject, "hourprice"))
                guideDetailBean.setGuideHourPrice(jsonObject.getDouble("hourprice"));
            if (UtilParse.checkTag(jsonObject, "livepics"))
                guideDetailBean.setGuideAlbum(jsonObject.getString("livepics"));
            if (UtilParse.checkTag(jsonObject, "idcard"))
                guideDetailBean.setGuideIdCard(jsonObject.getString("idcard"));
            if (UtilParse.checkTag(jsonObject, "specialskill"))
                guideDetailBean.setGuideSpecial(jsonObject.getString("specialskill"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return guideDetailBean;
    }

    public static List<FrequeCotactsBean> getListOfFreContact(String json) {
        List<FrequeCotactsBean> contactsList = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray != null && jsonArray.length() > 0) {
                contactsList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    FrequeCotactsBean frequeCotactsBean = new FrequeCotactsBean();
                    if (UtilParse.checkTag(jsonObject, "id"))
                        frequeCotactsBean.setContactId(jsonObject.getString("id"));
                    if (UtilParse.checkTag(jsonObject, "realname"))
                        frequeCotactsBean.setContactName(jsonObject.getString("realname"));
                    if (UtilParse.checkTag(jsonObject, "phone"))
                        frequeCotactsBean.setContactPhone(jsonObject.getString("phone"));
                    contactsList.add(frequeCotactsBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contactsList;
    }
}
