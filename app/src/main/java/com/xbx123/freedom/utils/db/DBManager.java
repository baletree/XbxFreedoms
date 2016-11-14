package com.xbx123.freedom.utils.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.mapapi.model.LatLng;
import com.xbx123.freedom.app.App;
import com.xbx123.freedom.utils.constant.Constant;
import com.xbx123.freedom.utils.tool.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/8/10.
 */
public class DBManager {
    private static DBManager dbMgr = new DBManager();
    private DbOpenHelper dbHelper;

    private DBManager() {
        dbHelper = DbOpenHelper.getInstance(App.getContext());
    }

    public static synchronized DBManager getInstance() {
        if (dbMgr == null)
            dbMgr = new DBManager();
        return dbMgr;
    }

    /**
     * 保存服务人员当前订单行走的经纬度
     *
     * @param latLng
     */
    synchronized public void saveServerLatLng(LatLng latLng) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(Constant.DB.ROWLATITUDE, latLng.latitude);
            values.put(Constant.DB.ROWLONGTITUDE, latLng.longitude);
            long insertCount = db.insert(Constant.DB.LATLNGTABLENAME, null, values);
        }
    }

    /**
     * @return 经纬度的列表
     */
    synchronized public List<LatLng> getLatLngList() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<LatLng> listLatLng = null;
        if (db.isOpen()) {
            listLatLng = new ArrayList<>();
            Cursor cursor = db.query(Constant.DB.LATLNGTABLENAME, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                LatLng latLng = new LatLng(cursor.getDouble(cursor.getColumnIndex(Constant.DB.ROWLATITUDE)), cursor.getDouble(cursor.getColumnIndex(Constant.DB.ROWLONGTITUDE)));
                listLatLng.add(latLng);
            }
        }
        return listLatLng;
    }

    /**
     * 清空表中数据
     */
    synchronized public void deleteTableLatLng() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen())
            db.delete(Constant.DB.LATLNGTABLENAME, null, null);
    }
}
