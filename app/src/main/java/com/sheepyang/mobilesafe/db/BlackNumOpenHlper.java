package com.sheepyang.mobilesafe.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SheepYang on 2016/6/12 10:27.
 */
public class BlackNumOpenHlper extends SQLiteOpenHelper{
    //方便后期我们在实现数据库操作的时候能方便去使用表名,同时也方便后期去修改表名
    public static final String DB_NAME="info";

    public BlackNumOpenHlper(Context context) {
        super(context, "blacknum.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表结构   字段:   blacknum:黑名单号码     mode:拦截类型
        //参数:创建表结构sql语句
        db.execSQL("create table "+DB_NAME+"(_id integer primary key autoincrement,blacknum varchar(20),mode varchar(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
