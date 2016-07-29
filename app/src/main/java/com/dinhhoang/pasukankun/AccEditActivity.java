package com.dinhhoang.pasukankun;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AccEditActivity extends AppCompatActivity {
    String acc_id;
    EditText view_acc_title, view_acc_user, view_acc_pass, view_acc_url, view_acc_memo;
    Button btn_cancel, btn_save;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(R.string.label_edit);

        setViews();

        // Intent を取得する
        Intent intent = getIntent();
        // アカウントIDをうけとる
        acc_id = intent.getStringExtra("ACC_ID");
        //データベースから呼び出す
        MyOpenHelper helper = new MyOpenHelper(this);
        //db = helper.getReadableDatabase();
        db = helper.getWritableDatabase();
        //Cursor c = db.rawQuery("select _id, acc, pass, url, memo from acc_table", null);
        Cursor c = db.rawQuery("select title, user, pass, url, memo FROM acc_table WHERE _id=" + acc_id, null);
        boolean isEof = c.moveToFirst();
        if (isEof) {
            String strAccTitle = c.getString(0);
            view_acc_title.setText(strAccTitle);
            String strAccUser = c.getString(1);
            view_acc_user.setText(strAccUser);
            String strAccPass = c.getString(2);
            view_acc_pass.setText(strAccPass);
            String strAccURL = c.getString(3);
            view_acc_url.setText(strAccURL);
            String strAccMemo = c.getString(4);
            view_acc_memo.setText(strAccMemo);
        }
        c.close();


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strAccTitle = view_acc_title.getText().toString();
                if (strAccTitle.equals("")) {
                    view_acc_title.setHint(R.string.label_title);
                    view_acc_title.requestFocus();
                    Toast.makeText(AccEditActivity.this, R.string.error_title_empty, Toast.LENGTH_SHORT).show();
                } else {
                    //@TODO:保存処理
                    ContentValues updateValues = new ContentValues();
                    updateValues.put("title", strAccTitle);
                    //db.update("acc_table", updateValues, "_id=", new String[]{acc_id});
                    //db.update("acc_table", updateValues,"_id="+ acc_id,null);
                    db.rawQuery("UPDATE acc_table SET title=" + strAccTitle + " WHERE _id=" + acc_id, null);
                    Log.v("HOANG_SAVE", "ここまで");
                    finish();
                }


            }
        });
        db.close();
    }

    private void setViews() {
        view_acc_title = (EditText) findViewById(R.id.editTitle);
        view_acc_user = (EditText) findViewById(R.id.editName);
        view_acc_pass = (EditText) findViewById(R.id.editAge);
        view_acc_url = (EditText) findViewById(R.id.editURL);
        view_acc_memo = (EditText) findViewById(R.id.editMeno);
        btn_cancel = (Button) findViewById(R.id.btn2);
        btn_save = (Button) findViewById(R.id.btn3);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
