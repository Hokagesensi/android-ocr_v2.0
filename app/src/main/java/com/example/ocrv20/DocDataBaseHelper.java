package com.example.ocrv20;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DocDataBaseHelper  extends SQLiteOpenHelper {

    public DocDataBaseHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句 并 执行
        String sql = "create table doc(id integer primary key autoincrement,typeInfo varchar(10),text varchar(1024), bitmap varchar(100),comment varchar(100),time varchar(100))";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}