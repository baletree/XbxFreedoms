package com.xbx123.freedom.utils.tool;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import com.baidu.mapapi.model.LatLng;
import com.xbx123.freedom.R;
import com.xbx123.freedom.beans.ServerBean;
import com.xbx123.freedom.ui.activity.UserInfoActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by EricYuan on 2016/6/7.
 */
public class StringUtil {
    private static String selectDateStr = "";

    public static String splitCityStr(Context context, String orStr) {
        String str = "";
        if (!Util.isNull(orStr) && orStr.contains(context.getString(R.string.appCity)))
            str = orStr.split(context.getString(R.string.appCity))[1];
        else
            str = orStr;
        return str;
    }

    public static String serverType(Context context, int serverT) {
        switch (serverT) {
            case 1:
                return context.getString(R.string.oLOrderNarrator);
            case 2:
                return context.getString(R.string.oLOrderNative);
            case 3:
                return context.getString(R.string.oLOrderGuide);
        }
        return "";
    }

    public static String orderStateStr(Context context, int stateType) {
        switch (stateType) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 9:
            case 13:
                return context.getString(R.string.oLOrderGoing);
            case 4:
                return context.getString(R.string.oLOrderPay);
            case 5:
                return context.getString(R.string.oLOrderComment);
            case 6:
                return context.getString(R.string.oLOrderFinish);
            case 7:
                return context.getString(R.string.oLOrderCancelPay);
            case 8:
                return context.getString(R.string.oLOrderClose);
            case 10:
                return context.getString(R.string.oLOrderClosed);
            case 11:
                return context.getString(R.string.oLOrderReservation);
            case 12:
                return context.getString(R.string.oLOrderWaitTrip);
            case 14://导游已取消退款中
                return context.getString(R.string.oLOrderExitFee);
            case 15://预约导游失败退款中
                return context.getString(R.string.oLOrderExitFee);
        }
        return "";
    }

    public static float getMapScaling2(double serverDistance) {
        if (serverDistance < 200)
            return 19f;
        else if (serverDistance < 500)
            return 18f;
        else if (serverDistance < 1000)
            return 17f;
        else if (serverDistance < 1500)
            return 16f;
        else
            return 15f;
    }

    /**
     * @param currentLatLng
     * @param serverList
     * @return
     */
    public static double calculateMapScaling2(LatLng currentLatLng, List<ServerBean> serverList) {
        double towPointDistance = 0;
        for (ServerBean serverBean : serverList) {
            if (serverBean != null) {
                double distance = Util.getDistance(currentLatLng.longitude, currentLatLng.latitude, serverBean.getServerLog(), serverBean.getServerLat());
                if (distance > towPointDistance)
                    towPointDistance = distance;
            }
        }
        return towPointDistance;
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */
    public static boolean isNameChinese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    /**
     * 将MMM d, yyyy K:m:s a的时间格式转换成时间戳
     *
     * @param sqlDate
     * @return
     */
    public static long getTimeMills(String sqlDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
        Date date = null;
        try {
            date = simpleDateFormat.parse(sqlDate);
            long timeStemp = date.getTime();
            return timeStemp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据年龄得出x0后
     *
     * @param age
     * @return
     */
    public static String getAgeLatter(int age) {
        Calendar calendar = Calendar.getInstance();
        int cYear = calendar.get(Calendar.YEAR);
        String cYearStr = String.valueOf(cYear - age);
        return cYearStr.substring(2, 3) + "0";
    }

    /**
     * 根据字符串时间转换日期得到工作年限
     *
     * @param jobStartTime
     * @return
     */
    public static String getJobTimes(String jobStartTime) {
        if (Util.isNull(jobStartTime))
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        int cYear = calendar.get(Calendar.YEAR);
        try {
            Date date = sdf.parse(jobStartTime);
            calendar.setTime(date);
            return (cYear - calendar.get(Calendar.YEAR)) + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(strDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                    + calendar.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 得倒明天的日期
     *
     * @return
     */
    public static String getNextDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.roll(java.util.Calendar.DAY_OF_YEAR, 1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 根据两个日期算出天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getDateDiffer(String startDate, String endDate) {
        if (Util.isNull(startDate))
            return "";
        if (Util.isNull(endDate))
            return "";
        //时间转换类
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date sDate = sdf.parse(startDate);
            Date eDate = sdf.parse(endDate);
            return daysBetween(sDate, eDate) + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据两个日期算出天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int daysBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days)) + 1;
    }

    /**
     * 下单时间加上两个小时的结果
     *
     * @param downOrderTime
     * @return
     */
    public static long getLastPayOrderTime(String downOrderTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date sDate = sdf.parse(downOrderTime);
            return sDate.getTime() + 2 * 60 * 60 * 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 相差的小时数
     *
     * @param time1
     * @param time2
     * @return
     */
    public static String hoursBetween(Context context, long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
//        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return hour + context.getString(R.string.appHour) + min + context.getString(R.string.appScore);
    }
}
