package com.dinhhoang.pasukankun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        MyOpenHelper helper = new MyOpenHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();

        final EditText nameText = (EditText) findViewById(R.id.editName);
        final EditText ageText = (EditText) findViewById(R.id.editAge);
        Button entryButton = (Button) findViewById(R.id.insert);

        entryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String age = ageText.getText().toString();
                ContentValues insertValues = new ContentValues();
                insertValues.put("user", name);
                insertValues.put("pass", age);
                //long id = db.insert("person_table", name, insertValues);
                long id = db.insert("acc_table", name, insertValues);
                Toast.makeText(DetailActivity.this, "DEBUG:insert " + name + age + " id=" + id, Toast.LENGTH_SHORT).show();

            }
        });
        Button updateButton = (Button) findViewById(R.id.update);
        updateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String age = ageText.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(DetailActivity.this, "名前を入力してください。", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues updateValues = new ContentValues();
                    updateValues.put("pass", age);
                    db.update("acc_table", updateValues, "user=?", new String[]{name});
                }
            }
        });
        Button deleteButton = (Button) findViewById(R.id.delete);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String age = ageText.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(DetailActivity.this, "名前を入力してください。", Toast.LENGTH_SHORT).show();
                } else {
                    db.delete("acc_table", "user=?", new String[]{name});
                }
            }
        });
        Button deleteAllButton = (Button) findViewById(R.id.deleteAll);
        deleteAllButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String age = ageText.getText().toString();
                db.delete("acc_table", null, null);
            }
        });


    }
}
