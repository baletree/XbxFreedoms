package com.xbx123.freedom.jsonparse;

import android.content.Context;

import com.xbx123.freedom.beans.ResortsBean;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.beans.UserStateBean;
import com.xbx123.freedom.beans.VersionBean;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/25.
 * 主页状态解析
 */
public class MainStateParse {
    /**
     * 重置Token
     *
     * @param context
     * @param json
     */
    public static void resetToken(Context context, String json) {
        UserInfo userInfo = SharePrefer.getUserInfo(context);
        if (userInfo == null)
            return;
        try {
            JSONObject object = new JSONObject(json);
            if (UtilParse.checkTag(object, "login_token"))
                userInfo.setLoginToken(object.getString("login_token"));
            SharePrefer.saveUserInfo(context, userInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String checkDataType(String json) {
        String dataType = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "user_unfinished_order")) {
                JSONObject job = jsonObject.getJSONObject("user_unfinished_order");
                if (UtilParse.checkTag(job, "data_type"))
                    dataType = job.getString("data_type");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataType;
    }

    public static UserStateBean checkUserState(String json, String key) {
        UserStateBean stateBean = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject job = jsonObject.getJSONObject("user_unfinished_order");
            if (UtilParse.checkTag(job, key)) {
                stateBean = new UserStateBean();
                JSONObject job2 = job.getJSONObject(key);
                if (UtilParse.checkTag(job2, "order_number"))
                    stateBean.setOrderNum(job2.getString("order_number"));
                if (UtilParse.checkTag(job2, "guide_type"))
                    stateBean.setGuideType(job2.getInt("guide_type"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stateBean;
    }

    /**
     * 是否需要支付
     *
     * @param json
     * @return
     */
    public static int getCancelIsPay(String json) {
        int isPay = -1;
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "is_pay"))
                isPay = jsonObject.getInt("is_pay");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isPay;
    }

    public static List<ResortsBean> getResorts(String json) {
        List<ResortsBean> resortsList = null;
        if (Util.isNull(json))
            return resortsList;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() > 0) {
                resortsList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    ResortsBean resortsBean = new ResortsBean();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    if(UtilParse.checkTag(jsonObject,"tourist_code"))
                        resortsBean.setResortsId(jsonObject.getString("tourist_code"));
                    if(UtilParse.checkTag(jsonObject,"tourist_name"))
                        resortsBean.setResortsName(jsonObject.getString("tourist_name"));
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

    public static VersionBean getVersionInfo(String json){
        VersionBean versionBean = new VersionBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "version_code"))
                versionBean.setVersionCode(jsonObject.getInt("version_code"));
            if (UtilParse.checkTag(jsonObject, "version_title"))
                versionBean.setVersionName(jsonObject.getString("version_title"));
            if (UtilParse.checkTag(jsonObject, "update_content"))
                versionBean.setVersionContent(jsonObject.getString("update_content"));
            if (UtilParse.checkTag(jsonObject, "version_url"))
                versionBean.setVersionUrl(jsonObject.getString("version_url"));
            if (UtilParse.checkTag(jsonObject, "must_update"))
                versionBean.setForceUpdate(jsonObject.getBoolean("must_update"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return versionBean;
    }
}
