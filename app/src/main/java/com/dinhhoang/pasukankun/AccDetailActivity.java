package com.dinhhoang.pasukankun;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AccDetailActivity extends AppCompatActivity {

    TextView view_acc_title, view_acc_user, view_acc_pass, view_acc_url, view_acc_memo;
    Button btn_edit, btn_del;
    String acc_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setViews();

        // Intent を取得する
        Intent intent = getIntent();
        // アカウントIDをうけとる(データがない場合、第２引数の -1 が返る)
        acc_id = intent.getStringExtra("ACC_ID");

        //tv_acc_title.setText("DEBUB: アカウントID="+ acc_id);

        loadData();


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@TODO: idを渡して編集アクティビティを起動
                // インテントのインスタンス生成
                Intent intent = new Intent(AccDetailActivity.this, AccEditActivity.class);
                intent.putExtra("ACC_ID", acc_id);
                // 次画面のアクティビティ起動
                startActivity(intent);
                //finish();
            }
        });
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view_acc_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2Clipboard((TextView) v);
            }
        });
        view_acc_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2Clipboard((TextView) v);
            }
        });
        view_acc_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2Clipboard((TextView) v);
            }
        });
        view_acc_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2Clipboard((TextView) v);
            }
        });

    }

    private void loadData() {
        //データベースから呼び出す
        MyOpenHelper helper = new MyOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        //Cursor c = db.rawQuery("select _id, acc, pass, url, memo from acc_table", null);
        Cursor c = db.rawQuery("select title, user, pass, url, memo FROM acc_table WHERE _id=" + acc_id, null);
        boolean isEof = c.moveToFirst();
        int cat_i = 0;
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
        db.close();
    }

    private void setViews() {
        btn_edit = (Button) findViewById(R.id.btn2);
        btn_del = (Button) findViewById(R.id.btn3);
        view_acc_title = (TextView) findViewById(R.id.textView1);
        view_acc_user = (TextView) findViewById(R.id.editText1);
        view_acc_pass = (TextView) findViewById(R.id.editText2);
        view_acc_url = (TextView) findViewById(R.id.editText4);
        view_acc_memo = (TextView) findViewById(R.id.editText5);
    }

    private void text2Clipboard(TextView v) {

        //クリップボードに格納するItemを作成
        ClipData.Item item = new ClipData.Item(v.getText());

        //MIMETYPEの作成
        String[] mimeType = new String[1];
        mimeType[0] = ClipDescription.MIMETYPE_TEXT_PLAIN;

        //クリップボードに格納するClipDataオブジェクトの作成
        ClipData cd = new ClipData(new ClipDescription(getString(R.string.app_name), mimeType), item);

        //クリップボードにデータを格納
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cm.setPrimaryClip(cd);

        Toast.makeText(getApplicationContext(),
                R.string.msg_coppied, Toast.LENGTH_SHORT)
                .show();
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

    @Override
    protected void onResume() {
        super.onResume();
        loadData();

    }
}
