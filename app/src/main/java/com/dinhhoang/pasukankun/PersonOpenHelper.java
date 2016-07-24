package com.dinhhoang.pasukankun;

/**
 * Created by dinhhoang on 2016/07/22.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class PersonOpenHelper extends SQLiteOpenHelper {
    final static private int DB_VERSION = 1;

    public PersonOpenHelper(Context context) {
        //super(context, null, null, DB_VERSION);
        super(context, "NameAgeDB", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // table create
        db.execSQL(
                "create table person_table(" +
                        "_id integer primary key autoincrement," +
                        "   name text not null," +
                        "   age text" +
                        ");"
        );

        // table row insert
        db.execSQL("insert into person_table(name,age) values ('本田 圭佑', 24);");
        db.execSQL("insert into person_table(name,age) values ('遠藤 保仁', 30);");
        db.execSQL("insert into person_table(name,age) values ('松井 大輔', 29);");
        db.execSQL("insert into person_table(name,age) values ('hoang', 27);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
    }


}