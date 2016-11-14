package com.xbx123.freedom.utils.sp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by EricYuan on 2016/3/30.
 */
public class SpHelper {

    private Context context;
    private String spName;

    public SpHelper(Context context,String spName){
        this.context = context;
        this.spName = spName;
    }

    public void setSP(String sKey, String sValue) {
        if(context == null)
            return;
        SharedPreferences s = context.getSharedPreferences(spName, 0);
        SharedPreferences.Editor editor = s.edit();
        editor.putString(sKey, sValue);
        editor.commit();
    }

    public void setSP(String sKey, int sValue) {
        if(context == null)
            return;
        SharedPreferences s = context.getSharedPreferences(spName, 0);
        SharedPreferences.Editor editor = s.edit();
        editor.putInt(sKey, sValue);
        editor.commit();
    }

    public void setSP(String sKey, long sValue) {
        if(context == null)
            return;
        SharedPreferences s = context.getSharedPreferences(spName, 0);
        SharedPreferences.Editor editor = s.edit();
        editor.putLong(sKey, sValue);
        editor.commit();
    }

    public void setSP(String sKey, boolean sValue) {
        if(context == null)
            return;
        SharedPreferences s = context.getSharedPreferences(spName, 0);
        SharedPreferences.Editor editor = s.edit();
        editor.putBoolean(sKey, sValue);
        editor.commit();
    }

    public String getSPStr(String sKey) {
        if(context == null)
            return "";
        String str;
        SharedPreferences s = context.getSharedPreferences(spName, 0);
        str = s.getString(sKey, "");
        return str;
    }

    public int getSPInt(String sKey) {
        if(context == null)
            return 0;
        int value;
        SharedPreferences s = context.getSharedPreferences(spName, 0);
        value = s.getInt(sKey, 0);
        return value;
    }

    public long getSPLong(String sKey) {
        if(context == null)
            return 0;
        long value;
        SharedPreferences s = context.getSharedPreferences(spName, 0);
        value = s.getLong(sKey, 0);
        return value;
    }

    public boolean getSPBoolean(String key){
        if(context == null)
            return false;
        SharedPreferences s = context.getSharedPreferences(spName, 0);
        return s.getBoolean(key, false);
    }
}
