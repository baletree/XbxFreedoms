package com.xbx123.freedom.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xbx123.freedom.utils.constant.Constant;

/**
 * Created by EricYuan on 2016/8/10.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static DbOpenHelper instance;

    public DbOpenHelper(Context context) {
        super(context, Constant.DB.DBNAME, null, 1);
    }

    public static DbOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + Constant.DB.LATLNGTABLENAME + "("
                + "_id integer primary key autoincrement,"
                + Constant.DB.ROWLATITUDE + " REAL,"
                + Constant.DB.ROWLONGTITUDE + " REAL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Constant.DB.LATLNGTABLENAME);
    }
}
