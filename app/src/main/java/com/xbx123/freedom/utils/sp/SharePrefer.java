package com.xbx123.freedom.utils.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.baidu.mapapi.model.LatLng;
import com.xbx123.freedom.beans.LocationBean;
import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.tool.Util;

/**
 * Created by EricYuan on 2016/3/30.
 */
public class SharePrefer {

    public static void setFistUseState(Context context, boolean isFistUser) {
        SpHelper spHelper = new SpHelper(context, Constant.SPFirstUsr);
        if (spHelper == null)
            return;
        spHelper.setSP("theFirstUse", isFistUser);
    }

    public static boolean getFistUseState(Context context) {
        SpHelper spHelper = new SpHelper(context, Constant.SPFirstUsr);
        if (spHelper == null)
            return false;
        return spHelper.getSPBoolean("theFirstUse");
    }

    public static void saveLocate(Context context, LocationBean locationBean) {
        SpHelper spHelper = new SpHelper(context, Constant.SPLOC_NAME);
        if (spHelper == null)
            return;
        spHelper.setSP("loc_lon", locationBean.getLon()); // 经度
        spHelper.setSP("loc_lat", locationBean.getLat()); // 经度
        spHelper.setSP("loc_city", locationBean.getCity()); // 城市
    }

    public static LocationBean getLocate(Context context) {
        LocationBean locationBean = new LocationBean();
        SpHelper spHelper = new SpHelper(context, Constant.SPLOC_NAME);
        if (spHelper == null)
            return null;
        locationBean.setLon(spHelper.getSPStr("loc_lon"));
        locationBean.setLat(spHelper.getSPStr("loc_lat"));
        locationBean.setCity(spHelper.getSPStr("loc_city"));
        return locationBean;
    }

    /**
     * 存储我当前定位的经纬度
     * @param context
     * @param longitude
     * @param latitude
     */
    public static void saveLatlng(Context context, String longitude, String latitude) {
        SpHelper spHelper = new SpHelper(context, Constant.SPUSER_LATLNG);
        if (spHelper == null)
            return;
        spHelper.setSP("user_loc_lon", longitude); // 经度
        spHelper.setSP("user_loc_lat", latitude); // 纬度
    }

    /**
     * 获取当前我的位置的经纬度
     * @param context
     * @return
     */
    public static LatLng getLatlng(Context context) {
        SpHelper spHelper = new SpHelper(context, Constant.SPUSER_LATLNG);
        if (spHelper == null)
            return null;
        String lon = spHelper.getSPStr("user_loc_lon");
        String lat = spHelper.getSPStr("user_loc_lat");
        if (!Util.isNull(lon) && !Util.isNull(lat)) {
            return new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        }
        return null;
    }

    public static void savePhone(Context context, String phone) {
        SpHelper spHelper = new SpHelper(context, Constant.SPUSER_PHONE);
        if (spHelper == null)
            return;
        spHelper.setSP("phone", phone); //用户的手机号码
    }

    public static String getUserPhone(Context context) {
        String phone = "";
        SpHelper spHelper = new SpHelper(context, Constant.SPUSER_PHONE);
        if (spHelper == null)
            return null;
        phone = spHelper.getSPStr("phone");
        return phone;
    }

    public static void saveUserInfo(Context context, UserInfo userInfo) {
        SpHelper spHelper = new SpHelper(context, Constant.SPUSER_INFO);
        if (spHelper == null)
            return;
        spHelper.setSP("userUid", userInfo.getUid());
        spHelper.setSP("userPhone", userInfo.getUserPhone());
        spHelper.setSP("userNickname", userInfo.getNickName());
        spHelper.setSP("userHead", userInfo.getUserHead());
        spHelper.setSP("userLoginToken", userInfo.getLoginToken());
        spHelper.setSP("userSexType", userInfo.getUserSex());
        spHelper.setSP("userBirthday", userInfo.getUserBirthday());
        spHelper.setSP("userRealName", userInfo.getUserRealName());
        spHelper.setSP("userIdCard", userInfo.getUserIdCard());
        spHelper.setSP("userEmail", userInfo.getUserEmail());
    }

    public static UserInfo getUserInfo(Context context) {
        UserInfo userInfo = new UserInfo();
        SpHelper spHelper = new SpHelper(context, Constant.SPUSER_INFO);
        if (spHelper == null)
            return userInfo;
        userInfo.setUid(spHelper.getSPStr("userUid"));
        userInfo.setUserPhone(spHelper.getSPStr("userPhone"));
        userInfo.setNickName(spHelper.getSPStr("userNickname"));
        userInfo.setUserHead(spHelper.getSPStr("userHead"));
        userInfo.setLoginToken(spHelper.getSPStr("userLoginToken"));
        userInfo.setUserSex(spHelper.getSPInt("userSexType"));
        userInfo.setUserBirthday(spHelper.getSPStr("userBirthday"));
        userInfo.setUserRealName(spHelper.getSPStr("userRealName"));
        userInfo.setUserIdCard(spHelper.getSPStr("userIdCard"));
        userInfo.setUserEmail(spHelper.getSPStr("userEmail"));
        return userInfo;
    }

    public static void savePhoneId(Context context, String phoneId) {
        SpHelper spHelper = new SpHelper(context, Constant.SPPHONE_ID);
        if (spHelper == null)
            return;
        spHelper.setSP("phoneId", phoneId);
    }

    public static String getPhoneId(Context context) {
        SpHelper spHelper = new SpHelper(context, Constant.SPPHONE_ID);
        if (spHelper == null)
            return "";
        return spHelper.getSPStr("phoneId");
    }

    public static void saveNarratorName(Context context, String narratorName) {
        SpHelper spHelper = new SpHelper(context, Constant.NarratorName);
        if (spHelper == null)
            return;
        spHelper.setSP("narratorNames", narratorName);
    }

    public static String getNarratorName(Context context) {
        SpHelper spHelper = new SpHelper(context, Constant.NarratorName);
        if (spHelper == null)
            return "";
        return spHelper.getSPStr("narratorNames");
    }

    public static void saveJPushId(Context context, String jPushId) {
        SpHelper spHelper = new SpHelper(context, Constant.JPush_ID);
        if (spHelper == null)
            return;
        spHelper.setSP("jPushId", jPushId);
    }

    public static String getJpushId(Context context) {
        SpHelper spHelper = new SpHelper(context, Constant.JPush_ID);
        if (spHelper == null)
            return "";
        return spHelper.getSPStr("jPushId");
    }
}
