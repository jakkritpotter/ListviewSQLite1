package com.android.mobile.listviewsqlite1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by 5N1P3R on 21/3/2560.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "BUD_DB.db";
    private static final int DB_VERSION = 1;
    //ค ำสั่งในกำรสร้ำง ตำรำงส ำหรับเก็บข้อมูล
    private static final String DB_CREATE = "" +
            "CREATE TABLE botany (" +
            "id INTEGER PRIMARY KEY, " +
            "t_name_b TEXT NOT NULL, " +
            "s_name_b TEXT NOT NULL, " +
            "detail_b TEXT NOT NULL, " +
            "bs_b TEXT NOT NULL, " +
            "image BLOB NOT NULL);";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //พิมพ์ Log เพื่อให้เห็นว่ำมีกำร Upgrade Database
        Log.w(DatabaseHelper.class.getName(),
                "Upgread database version from version" + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        //ลบตำรำง member ของเก่ำทิ้ง
        db.execSQL("DROP TABLE IF EXISTS botany");
        onCreate(db);
    }
}
