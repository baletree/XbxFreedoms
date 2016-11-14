package com.xbx123.freedom.jsonparse;

import com.xbx123.freedom.beans.MyServerInfoBean;
import com.xbx123.freedom.utils.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EricYuan on 2016/6/4.
 */
public class CreateOrderParse {
    public static String getOrderNum(String json) {
        String orderNum = "";
        if (Util.isNull(json))
            return orderNum;
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "order_number"))
                orderNum = jsonObject.getString("order_number");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderNum;
    }

    /**
     * 获取服务于用户的服务者信息
     *
     * @param json
     * @return
     */
    public static MyServerInfoBean getServerInfo(String json) {
        MyServerInfoBean serverInfoBean = null;
        if (Util.isNull(json))
            return serverInfoBean;
        serverInfoBean = new MyServerInfoBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "realname"))
                serverInfoBean.setServerName(jsonObject.getString("realname"));
            if (UtilParse.checkTag(jsonObject, "phone"))
                serverInfoBean.setServerPhone(jsonObject.getString("phone"));
            if (UtilParse.checkTag(jsonObject, "head_image"))
                serverInfoBean.setServerHeadImg(jsonObject.getString("head_image"));
            if (UtilParse.checkTag(jsonObject, "idcard"))
                serverInfoBean.setServerIdcard(jsonObject.getString("idcard"));
            if (UtilParse.checkTag(jsonObject, "lon"))
                serverInfoBean.setServerLon(jsonObject.getDouble("lon"));
            if (UtilParse.checkTag(jsonObject, "lat"))
                serverInfoBean.setServerLat(jsonObject.getDouble("lat"));
            if (UtilParse.checkTag(jsonObject, "stars"))
                serverInfoBean.setServerStarts(jsonObject.getString("stars"));
            if (UtilParse.checkTag(jsonObject, "server_start_time"))
                serverInfoBean.setStartTime(jsonObject.getLong("server_start_time"));
            if (UtilParse.checkTag(jsonObject, "server_price"))
                serverInfoBean.setServerUnitPrice(jsonObject.getInt("server_price"));
            if (UtilParse.checkTag(jsonObject, "now_time_long"))
                serverInfoBean.setServerTimeDiffer(jsonObject.getLong("now_time_long"));
            if (UtilParse.checkTag(jsonObject, "charging_type"))
                serverInfoBean.setServiceType(jsonObject.getInt("charging_type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return serverInfoBean;
    }

    /**
     * 获取服务者经纬度
     *
     * @param json
     * @param serverInfoBean
     * @return
     */
    public static MyServerInfoBean getServerLatLng(String json, MyServerInfoBean serverInfoBean) {
        Util.pLog("parse getServerLatLng（） "+json);
        if (Util.isNull(json))
            return serverInfoBean;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "lon"))
                serverInfoBean.setServerLon(jsonObject.getDouble("lon"));
            if (UtilParse.checkTag(jsonObject, "lat"))
                serverInfoBean.setServerLat(jsonObject.getDouble("lat"));
            if (UtilParse.checkTag(jsonObject, "server_start_time"))
                serverInfoBean.setStartTime(jsonObject.getLong("server_start_time"));
            if (UtilParse.checkTag(jsonObject, "is_arrive"))
                serverInfoBean.setIsArriveNearby(jsonObject.getInt("is_arrive"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Util.pLog("parse:" + serverInfoBean.getServerLat() + "," + serverInfoBean.getServerLon());
        return serverInfoBean;
    }

}
