package com.dinhhoang.pasukankun;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listAcc;

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

        /*my sql*/


        setViews();
        showListView();


    }

    private void addNewAcc() {
        // インテントのインスタンス生成
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        // 次画面のアクティビティ起動
        startActivity(intent);
        //db.execSQL("insert into person_table(name,age) values ('本田 圭佑', 24);");
    }

    private void setViews() {
        listAcc = (ListView) findViewById(R.id.listView);
    }

    private void showListView() {
        List<String> members = new ArrayList<String>();

        PersonOpenHelper helper = new PersonOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
/*
        Cursor c = db.query("person_table", new String[] { "name", "age" },
                null, null, null, null, null);
*/
        Cursor c = db.rawQuery("select _id, name, age from person_table", null);
        boolean isEof = c.moveToFirst();
        while (isEof) {
            //String test = String.format("%s : %d才", c.getString(0), c.getInt(1));
            String test = String.format("(%d)%s : %d才", c.getInt(0), c.getString(1), c.getInt(2));
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

    private void showListView2() {

        PersonOpenHelper helper = new PersonOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        //Cursor c = db.query("person_table", new String[] {"_id","name","age"}, null, null, null, null, null);
        Cursor c = db.rawQuery("select rowid as _id, name, age from person_table", null);
        startManagingCursor(c);

        ListAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, c,
                new String[]{"name", "age"},
                new int[]{android.R.id.text1, android.R.id.text2});


        listAcc.setAdapter(adapter);
        listAcc.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                int itemPosition = position;

                String itemValue = "@@@";// (String) listAcc.getItemAtPosition(position);
                //String[]  itemValue    = (String[]) listAcc.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }
        });
        /*
        c.close();

        db.close();
        */
    }


    @Override
    protected void onResume() {
        super.onResume();
        showListView();

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
            showListView();
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
