package com.dinhhoang.pasukankun;

/**
 * Created by dinhhoang on 2016/07/22.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyOpenHelper extends SQLiteOpenHelper {
    final static private int DB_VERSION = 1;

    public MyOpenHelper(Context context) {
        //super(context, null, null, DB_VERSION);
        super(context, "MyAccDB", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // table create
        String CREATE_TABLE = "create table acc_table (" +
                "_id integer primary key autoincrement," +
                "category INTEGER DEFAULT 0," +
                "title text not null," +
                "user text," +
                "pass text," +
                "url text," +
                "memo text );";

        db.execSQL(CREATE_TABLE);

        //サンプルのカテゴリー
        //String sampleCatSQL = "insert into acc_table(category, title) values(0, 'サンプルのカテゴリー')";
        for (int cat_i = 0; cat_i < 5; cat_i++) {
            ContentValues insertValues = new ContentValues();
            insertValues.put("category", 0);
            insertValues.put("title", "サンプルのカテゴリー" + cat_i);
            long id = db.insert("acc_table", "サンプルのカテゴリー" + cat_i, insertValues);
            // table row insert
            for (int i = 0; i < 5; i++) {
                String mySQL = String.format("insert into acc_table" +
                        "(category, title, user,pass,url,memo) values " +
                        "(%d,'Tiêu đề %d%d', 'アカウント%d', 'パスワード%d','http://sample%d.jp','備考内容%d');", id, cat_i, i, i, i, i, i);
                db.execSQL(mySQL);
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        Log.v("LogSample", String.format(
                "onUpgrade: oldVersion=%d,newVersion=%d", oldVersion,
                newVersion));
        db.beginTransaction();
        try {
            db.execSQL(
                    "create table player_table(" +
                            "   name text not null," +
                            "   age text," +
                            "   team text default '無所属'" +
                            ");"
            );

            SQLiteStatement stmt = db
                    .compileStatement("insert into player_table(name,age) values (?, ?);");
            Cursor c = db.query("person_table", new String[]{"name", "age"},
                    null, null, null, null, null);

            boolean isEof = c.moveToFirst();
            while (isEof) {
                stmt.bindString(1, c.getString(0));
                stmt.bindLong(2, c.getInt(1));
                stmt.execute();
                isEof = c.moveToNext();
            }
            c.close();
            db.execSQL("drop table person_table;");

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        */
    }


}