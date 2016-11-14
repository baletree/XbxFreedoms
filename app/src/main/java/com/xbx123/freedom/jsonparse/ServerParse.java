package com.xbx123.freedom.jsonparse;

import com.baidu.mapapi.model.LatLng;
import com.xbx123.freedom.beans.ResortsBean;
import com.xbx123.freedom.beans.ServerBean;
import com.xbx123.freedom.beans.ServerCommentBean;
import com.xbx123.freedom.beans.ServerPageBean;
import com.xbx123.freedom.utils.tool.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/6/2.
 */
public class ServerParse {
    /**
     * 解析讲解员
     *
     * @param json
     * @return
     */
    public static List<ResortsBean> getNarratorServer(String json) {
        List<ResortsBean> resortsList = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() > 0) {
                resortsList = new ArrayList<>();
                Util.pLog("getNarratorServer:" + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    ResortsBean resortsBean = new ResortsBean();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    if (UtilParse.checkTag(jsonObject, "tourist_id"))
                        resortsBean.setResortsId(jsonObject.getString("tourist_id"));
                    if (UtilParse.checkTag(jsonObject, "tourist_name"))
                        resortsBean.setResortsName(jsonObject.getString("tourist_name"));
                    if (UtilParse.checkTag(jsonObject, "tourist_code"))
                        resortsBean.setResortsCode(jsonObject.getString("tourist_code"));
                    if (UtilParse.checkTag(jsonObject, "hours_price"))
                        resortsBean.setResortsHourPrice(jsonObject.getInt("hours_price"));
                    if (UtilParse.checkTag(jsonObject, "times_price"))
                        resortsBean.setResortsTimePrice(jsonObject.getInt("times_price"));
                    if (UtilParse.checkTag(jsonObject, "_guide_info"))
                        resortsBean.setResortsServerList(getNativeServer(jsonObject.getString("_guide_info")));
                    if(UtilParse.checkTag(jsonObject,"lng_lat")){
                        String latLng = jsonObject.getString("lng_lat");
                        if(latLng.contains(",")){
                            String lon = latLng.split(",")[0];
                            if(!Util.isNull(lon))
                                resortsBean.setLon(Double.parseDouble(lon));
                            String lat = latLng.split(",")[1];
                            if(!Util.isNull(lat))
                                resortsBean.setLat(Double.parseDouble(lat));
                        }
                    }
                    resortsList.add(resortsBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resortsList;
    }

    /**
     * 解析领路人
     *
     * @param json
     * @return
     */
    public static List<ServerBean> getNativeServer(String json) {
        List<ServerBean> serverList = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() > 0) {
                serverList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    ServerBean serverBean = new ServerBean();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    if (UtilParse.checkTag(jsonObject, "uid"))
                        serverBean.setServerId(jsonObject.getString("uid"));
                    if (UtilParse.checkTag(jsonObject, "lat") && UtilParse.checkTag(jsonObject, "lon"))
                        serverBean.setServerLat(jsonObject.getDouble("lat"));
                    if (UtilParse.checkTag(jsonObject, "lon"))
                        serverBean.setServerLog(jsonObject.getDouble("lon"));
                    if (UtilParse.checkTag(jsonObject, "head_image"))
                        serverBean.setServerHeadImg(jsonObject.getString("head_image"));
                    if (UtilParse.checkTag(jsonObject, "realname"))
                        serverBean.setServerName(jsonObject.getString("realname"));
                    if (UtilParse.checkTag(jsonObject, "server_price"))
                        serverBean.setServerPrice(jsonObject.getString("server_price"));
                    if (UtilParse.checkTag(jsonObject, "server_times"))
                        serverBean.setServerTime(jsonObject.getString("server_times"));
                    if (UtilParse.checkTag(jsonObject, "stars"))
                        serverBean.setServerStars(jsonObject.getString("stars"));
                    serverList.add(serverBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return serverList;
    }

    /**
     * 服务者主页
     *
     * @param json
     * @return
     */
    public static ServerPageBean getServerPage(String json) {
        ServerPageBean pageBean = null;
        if (Util.isNull(json))
            return pageBean;
        pageBean = new ServerPageBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "realname"))
                pageBean.setServerName(jsonObject.getString("realname"));
            if (UtilParse.checkTag(jsonObject, "head_image"))
                pageBean.setServerHead(jsonObject.getString("head_image"));
            if (UtilParse.checkTag(jsonObject, "idcard"))
                pageBean.setServerIdCard(jsonObject.getString("idcard"));
            if (UtilParse.checkTag(jsonObject, "server_times"))
                pageBean.setServerTimes(jsonObject.getString("server_times"));
            if (UtilParse.checkTag(jsonObject, "self_introduce"))
                pageBean.setServerIntro(jsonObject.getString("self_introduce"));
            if (UtilParse.checkTag(jsonObject, "server_introduce"))
                pageBean.setServerStandard(jsonObject.getString("server_introduce"));
            if (UtilParse.checkTag(jsonObject, "stars"))
                pageBean.setServerStar(jsonObject.getString("stars"));
            if (UtilParse.checkTag(jsonObject, "comment_list")) {
                List<ServerCommentBean> commentList = null;
                JSONArray jsonArray = jsonObject.getJSONArray("comment_list");
                if (jsonArray.length() > 0) {
                    commentList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ServerCommentBean commentBean = new ServerCommentBean();
                        JSONObject job = (JSONObject) jsonArray.get(i);
                        if (UtilParse.checkTag(job, "nickname"))
                            commentBean.setUserName(job.getString("nickname"));
                        if (UtilParse.checkTag(job, "content"))
                            commentBean.setUserComment(job.getString("content"));
                        commentList.add(commentBean);
                    }
                }
                pageBean.setCommentList(commentList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Util.pLog("jsonArray:");
        return pageBean;
    }
}
