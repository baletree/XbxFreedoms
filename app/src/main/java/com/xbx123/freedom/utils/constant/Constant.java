package com.xbx123.freedom.utils.constant;

import android.os.Environment;

import java.io.File;

/**
 * Created by EricYuan on 2016/3/30.
 * 常量定义
 */
public class Constant {
    public static final String SPLOC_NAME = "location_spname";
    public static final String SPFirstUsr = "FirstUseSoft";
    public static final String SPUSER_PHONE = "user_phone";
    public static final String SPUSER_INFO = "user_info";
    public static final String SPPHONE_ID = "phone_id";
    public static final String JPush_ID = "jpushID";
    public static final String NarratorName = "narratorName";
    public static final String SPUSER_LATLNG = "user_latlng";
    public static final String actionOverOrder = "com.xbx.client.guide.over.order";//结束行程
    public static final String actionWxPayComplete = "com.xbx123.pay.wx.success";//结束行程
    public static final String actionLoginSuc = "com.xbx.client.login.suc";
    public static final String actionExitApp = "com.xbx.client.exit.app";
    public static final String actionCancelOrderSuc = "com.xbx.client.cancel.order.suc";
    public static final String actionDownAppCom = "com.xbx.client.app.down.finish";
    public static final int GuidesType = 3;
    public static final int NarratorType = 1;
    public static final int NativeType = 2;
    public static final String WXKey = "wx26cf10d12ab4aa2c";
    public static final int billingPeriod = 15; //服务计费周期
    /**
     * 存储根目录
     */
    public static final String APP_ROOT_PATH = Environment.getExternalStorageDirectory().toString();
    /**
     * 图片缓存路径
     */
    public static final String PICTURE_ALBUM_PATH = APP_ROOT_PATH + "/XbxFreedom/";
    public static final String PATH_PIC = PICTURE_ALBUM_PATH + File.separator + "Photo";
    /**
     * 系统图片存储路径
     */
    public static final String PHOTO_SYS_PATH = APP_ROOT_PATH + "/DCIM/Camera/";
    /**
     * 从Intent获取图片路径的KEY
     */
    public static final String KEY_PHOTO_PATH = "com.xbb.la.freedom.photo_path";

    public static String ROOT_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    /**
     * 更新apk的存放位置
     */
    public static String APK_PATH = ROOT_PATH + "/tutu/apk/";

    //    public static String APK_PATH = ROOT_PATH + "/tutu/apk/TutuFree.apk";
    public class DB {
        public static final String DBNAME = "LatLngDB";
        public static final String LATLNGTABLENAME = "tableLatLng";
        public static final String ROWLATITUDE = "rowLatitude";
        public static final String ROWLONGTITUDE = "rowLongitude";
    }
}
