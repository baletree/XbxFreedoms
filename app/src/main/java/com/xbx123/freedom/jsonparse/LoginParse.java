package com.xbx123.freedom.jsonparse;

import android.content.Context;

import com.xbx123.freedom.beans.UserInfo;
import com.xbx123.freedom.utils.sp.SharePrefer;
import com.xbx123.freedom.utils.tool.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EricYuan on 2016/6/1.
 */
public class LoginParse {
    /**
     * 登录信息
     *
     * @param json
     * @return
     */
    public static UserInfo getUserInfo(String json) {
        if (Util.isNull(json)) {
            return null;
        }
        UserInfo userInfo = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            userInfo = new UserInfo();
            if (UtilParse.checkTag(jsonObject, "uid")) {
                userInfo.setUid(jsonObject.getString("uid"));
            }
            if (UtilParse.checkTag(jsonObject, "login_token")) {
                userInfo.setLoginToken(jsonObject.getString("login_token"));
            }
            if (UtilParse.checkTag(jsonObject, "user_info")) {
                JSONObject job = jsonObject.getJSONObject("user_info");
                if (UtilParse.checkTag(job, "mobile"))
                    userInfo.setUserPhone(job.getString("mobile"));
                if (UtilParse.checkTag(job, "nickname"))
                    userInfo.setNickName(job.getString("nickname"));
                if (UtilParse.checkTag(job, "head_image"))
                    userInfo.setUserHead(job.getString("head_image"));
                if (UtilParse.checkTag(job, "birthday"))
                    userInfo.setUserBirthday(job.getString("birthday"));
                if (UtilParse.checkTag(job, "sex"))
                    userInfo.setUserSex(job.getInt("sex"));
                if (UtilParse.checkTag(job, "realn"))
                    userInfo.setUserRealName(job.getString("realn"));
                if (UtilParse.checkTag(job, "idcard"))
                    userInfo.setUserIdCard(job.getString("idcard"));
                if (UtilParse.checkTag(job, "email"))
                    userInfo.setUserEmail(job.getString("email"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    /**
     * 修改个人信息
     * @param json
     * @param userInfo
     * @return
     */
    public static UserInfo modifyUserInfo(String json, UserInfo userInfo) {
        if (Util.isNull(json))
            return null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "idcard"))
                userInfo.setUserIdCard(jsonObject.getString("idcard"));
            if (UtilParse.checkTag(jsonObject, "email"))
                userInfo.setUserEmail(jsonObject.getString("email"));
            if (UtilParse.checkTag(jsonObject, "phone"))
                userInfo.setUserPhone(jsonObject.getString("phone"));
            if (UtilParse.checkTag(jsonObject, "nickname"))
                userInfo.setNickName(jsonObject.getString("nickname"));
            if (UtilParse.checkTag(jsonObject, "realname"))
                userInfo.setUserRealName(jsonObject.getString("realname"));
            if (UtilParse.checkTag(jsonObject, "birthday"))
                userInfo.setUserBirthday(jsonObject.getString("birthday"));
            if (UtilParse.checkTag(jsonObject, "sex"))
                userInfo.setUserSex(jsonObject.getInt("sex"));
            if (UtilParse.checkTag(jsonObject, "head_image"))
                userInfo.setUserHead(jsonObject.getString("head_image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
