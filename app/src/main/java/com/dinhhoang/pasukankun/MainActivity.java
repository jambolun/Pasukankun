package com.dinhhoang.pasukankun;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listAcc;
    private static final String KEY1 = "key1";
    private static final String KEY2 = "key2";

    Map<String, String> accIDmap = new HashMap<>();// アカウントを保存するmap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addNewAcc();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setViews();
        //showListView();
        showExpandableListView();

        //loadMainContent();



    }

    private void loadMainContent() {

    }

    private void addNewAcc() {
        // インテントのインスタンス生成
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        // 次画面のアクティビティ起動
        startActivity(intent);
    }

    private void setViews() {
        //listAcc = (ListView) findViewById(R.id.listView);
        //linearLayoutMain = (LinearLayout) findViewById(R.id.linearViewMain);

    }

    private void showExpandableListView() {
        //データを準備する
        //prepareListData();
        // 設定する文字列のリスト
        List<Map<String, String>> groupData =
                new ArrayList<>();
        List<List<Map<String, String>>> childData =
                new ArrayList<>();

        MyOpenHelper helper = new MyOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        //Cursor c = db.rawQuery("select _id, acc, pass, url, memo from acc_table", null);
        Cursor c = db.rawQuery("select _id, title, user, pass, url, memo from acc_table WHERE category=0", null);
        boolean isEof = c.moveToFirst();
        int cat_i = 0;
        while (isEof) {
            //String test = String.format("(%d)[%s]%s : %s : %s : %s ", c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5));
            String strCatTitle = String.format("%s", c.getString(1));
            int catID = c.getInt(0);

            // 親要素の追加
            Map<String, String> curGroupMap =
                    new HashMap<>();
            groupData.add(curGroupMap);
            curGroupMap.put(KEY1, strCatTitle);
            curGroupMap.put(KEY2, "");

            List<Map<String, String>> children =
                    new ArrayList<>();

            Cursor c2 = db.rawQuery("select _id, title, user, pass, url, memo from acc_table WHERE category=" + catID, null);
            boolean isEof2 = c2.moveToFirst();
            int acc_i = 0;
            while (isEof2) {
                //String test = String.format("(%d)[%s]%s : %s : %s : %s ", c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5));
                String strAccTitle = String.format("%s", c2.getString(1));
                int accID = c2.getInt(0);

                //アカウントのidを覚えておく
                accIDmap.put(String.format("%d%d", cat_i, acc_i), String.valueOf(accID));


                // 子要素の追加
                Map<String, String> curChildMap =
                        new HashMap<>();
                children.add(curChildMap);
                curChildMap.put(KEY1, strAccTitle);
                curChildMap.put(KEY2, "moment");
                //次の要素へ
                acc_i++;
                isEof2 = c2.moveToNext();
            }
            childData.add(children);
            c2.close();

            //次の要素へ
            cat_i++;
            isEof = c.moveToNext();
        }

        c.close();
        db.close();


        // ExpandbleListAdapter の作成
        ExpandableListAdapter adapter =
                new SimpleExpandableListAdapter(
                        this,
                        groupData,
                        R.layout.list_item_category,
                        new String[]{KEY1, KEY2},
                        //new int[]{android.R.id.text1, android.R.id.text2},
                        new int[]{R.id.textCatTitle, android.R.id.text2},
                        childData,
                        R.layout.list_item_acc,
                        new String[]{KEY1, KEY2},
                        //new int[]{android.R.id.text1, android.R.id.text2}
                        new int[]{R.id.textAcc1, R.id.textAcc2}
                );

        ExpandableListView listView =
                (ExpandableListView) findViewById(R.id.expandableListView);
        // Adapter を設定
        listView.setAdapter(adapter);

        // グループがクリックされた時に呼び出されるコールバックを登録
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent,
                                        View v, int groupPosition, long id) {
                // クリックされた時の処理

                return false;
            }
        });

        // グループ内の項目がクリックされた時に呼び出されるコールバックを登録
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // クリックされた時の処理
                //Toast.makeText(MainActivity.this, String.format("groupPosition=%d,childPosition=%d,id=%d",groupPosition,childPosition,id), Toast.LENGTH_SHORT).show();

                Toast.makeText(
                        getApplicationContext(),
                        "id=" + accIDmap.get(String.format("%d%d", groupPosition, childPosition)), Toast.LENGTH_SHORT)
                        .show();

                return false;
            }
        });


    }

    private View.OnFocusChangeListener myEditTextFocus = new View.OnFocusChangeListener() {
        public void onFocusChange(View view, boolean gainFocus) {
            //onFocus
            if (gainFocus) {
                //set the text
                ((EditText) view).setText("In focus now");
            }
            //onBlur
            else {
                //clear the text
                ((EditText) view).setText("");
            }
        }


    };

    private void showListView99() {
        List<String> members = new ArrayList<String>();

        MyOpenHelper helper = new MyOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        //Cursor c = db.rawQuery("select _id, acc, pass, url, memo from acc_table", null);
        Cursor c = db.rawQuery("select _id, title, user, pass, url, memo from acc_table", null);
        boolean isEof = c.moveToFirst();
        while (isEof) {
            //String test = String.format("%s : %d才", c.getString(0), c.getInt(1));
            //String test = String.format("(%d)%s : %d才", c.getInt(0), c.getString(1), c.getInt(2));
            String test = String.format("(%d)[%s]%s : %s : %s : %s ", c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5));
            isEof = c.moveToNext();
            members.add(test);
        }
        c.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, members);
        listAcc.setAdapter(adapter);
        listAcc.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                int itemPosition = position;

                String itemValue = (String) listAcc.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();
            }
        });
        //Toast.makeText(MainActivity.this, "listviewが更新された", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        showExpandableListView();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_reload) {
            showExpandableListView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            addNewAcc();
        } else if (id == R.id.nav_import) {

        } else if (id == R.id.nav_export) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
