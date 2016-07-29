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
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AccEditActivity extends AppCompatActivity {
    String acc_id;
    EditText view_acc_title, view_acc_user, view_acc_pass, view_acc_url, view_acc_memo;
    Button btn_cancel, btn_save;
    Spinner spinner;
    SQLiteDatabase db;
    MyOpenHelper helper;
    ArrayList<Integer> arrCatID = new ArrayList<>();
    ContentValues updateValues = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        helper = new MyOpenHelper(this);
        setViews();
        loadCatList();

        // Intent を取得する
        Intent intent = getIntent();
        // アカウントIDをうけとる
        acc_id = intent.getStringExtra("ACC_ID");

        if (acc_id.equals("AdNewAcc")) {
            getSupportActionBar().setSubtitle(R.string.label_addnew);
        } else {
            getSupportActionBar().setSubtitle(getString(R.string.label_edit));


            //データベースから呼び出す

            db = helper.getReadableDatabase();
            Cursor c = db.rawQuery("select title, user, pass, url, memo, category FROM acc_table WHERE _id=" + acc_id, null);
            boolean isEof = c.moveToFirst();
            if (isEof) {
                String strAccTitle = c.getString(c.getColumnIndex("title"));
                view_acc_title.setText(strAccTitle);
                String strAccUser = c.getString(c.getColumnIndex("user"));
                view_acc_user.setText(strAccUser);
                String strAccPass = c.getString(c.getColumnIndex("pass"));
                view_acc_pass.setText(strAccPass);
                String strAccURL = c.getString(c.getColumnIndex("url"));
                view_acc_url.setText(strAccURL);
                String strAccMemo = c.getString(c.getColumnIndex("memo"));
                view_acc_memo.setText(strAccMemo);
                int intCategory = c.getInt(c.getColumnIndex("category"));
                spinner.setSelection(arrCatID.indexOf(intCategory));
            }
            c.close();
            db.close();
        }


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
                String strAccUser = view_acc_user.getText().toString();
                String strAccPass = view_acc_pass.getText().toString();
                String strAccURL = view_acc_url.getText().toString();
                String strAccMemo = view_acc_memo.getText().toString();


                updateValues.put("title", strAccTitle);
                updateValues.put("user", strAccUser);
                updateValues.put("pass", strAccPass);
                updateValues.put("url", strAccURL);
                updateValues.put("memo", strAccMemo);


                if (strAccTitle.equals("")) {
                    view_acc_title.setHint(R.string.label_title);
                    view_acc_title.requestFocus();
                    Toast.makeText(AccEditActivity.this, R.string.error_title_empty, Toast.LENGTH_SHORT).show();
                } else if (acc_id.equals("AdNewAcc")) {
                    //新しいアカウント情報を追加
                    db = helper.getWritableDatabase();
                    db.insert("acc_table", strAccTitle, updateValues);
                    db.close();
                    Toast.makeText(AccEditActivity.this, R.string.msg_saved, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    db = helper.getWritableDatabase();
                    db.update("acc_table", updateValues, "_id=" + acc_id, null);
                    db.close();
                    Toast.makeText(AccEditActivity.this, R.string.msg_saved, Toast.LENGTH_SHORT).show();
                    finish();
                }


            }
        });

    }

    private void loadCatList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // アイテムを追加します

        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id,title FROM acc_table WHERE category=0", null);
        try {
            while (cursor.moveToNext()) {
                String strCatTitle = cursor.getString(cursor.getColumnIndex("title"));
                int intCatId = cursor.getInt(cursor.getColumnIndex("_id"));
                adapter.add(strCatTitle);
                //Log.v("HOANG_CAT", strCatTitle);
                arrCatID.add(intCatId);
            }
        } finally {
            cursor.close();
        }
        db.close();


        // アダプターを設定します
        spinner.setAdapter(adapter);
        // スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Spinner spinner = (Spinner) parent;
                // 選択されたアイテムを取得します
                //String item = (String) spinner.getSelectedItem();
                //Toast.makeText(getApplicationContext(), item + arrCatID.get(position), Toast.LENGTH_LONG).show();
                updateValues.put("category", arrCatID.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void setViews() {
        view_acc_title = (EditText) findViewById(R.id.editTitle);
        view_acc_user = (EditText) findViewById(R.id.editName);
        view_acc_pass = (EditText) findViewById(R.id.editAge);
        view_acc_url = (EditText) findViewById(R.id.editURL);
        view_acc_memo = (EditText) findViewById(R.id.editMeno);
        btn_cancel = (Button) findViewById(R.id.btn2);
        btn_save = (Button) findViewById(R.id.btn3);
        spinner = (Spinner) findViewById(R.id.spinner);
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
