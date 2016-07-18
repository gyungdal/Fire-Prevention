package com.codezero.fireprevention.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by GyungDal on 2016-07-18.
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name,
                    SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table" + DBConfig.TABLE_NAME +"(" +
                "productKey integer primary key, " +
                "name text, " +
                "lng double, " +
                "lat double);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists sensor";
        db.execSQL(sql);
        onCreate(db); // 테이블을 지웠으므로 다시 테이블을 만들어주는 과정
    }
}