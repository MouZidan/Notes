package com.mou.inc.myapplication;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ToDoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout rootView;
    private ListView listView;
    private String[] itemsStringArray;
    public static ArrayList<String> itemsArrayList = new ArrayList<String>();
    private int counter=0;
    private ArrayAdapter<String> listAdapter;
    private Runnable run;
    public static ArrayList<Boolean> b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccent));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.colorAccent));
        toolbar=(Toolbar) findViewById(R.id.checklist_toolbar);
        this.setSupportActionBar(toolbar);
        rootView =(LinearLayout)findViewById(R.id.activity_checklist_rootview);
        listView =(ListView)findViewById(R.id.activity_checklist_listview);


        itemsStringArray = itemsArrayList.toArray(new String[0]);

        b =new ArrayList<Boolean>(Collections.nCopies(itemsArrayList.size(), false));








        listAdapter=new ToDoListAdapter(this , itemsStringArray,b);
        //listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsArrayList);


        listView.setAdapter(listAdapter);





    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_checklist_activity, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_checklist_add:
                createNewItem();

                break;
            // action with ID action_settings was selected

            default:
                break;
        }

        return true;
    }


    public void createNewItem(){

        b =new ArrayList<Boolean>(Collections.nCopies(itemsArrayList.size()+1, false));



       /* ArrayList<String>  itemsArrayListnew = new ArrayList<String>();
                itemsArrayListnew.addAll(itemsArrayList);
                itemsArrayListnew.add("text"+counter);

        itemsStringArray=itemsArrayList.toArray(new String[0]);*/

        //listAdapter =new ToDoListAdapter(ToDoActivity.this , itemsStringArray , b);


        //listView.setAdapter(listAdapter);

        listAdapter.notifyDataSetInvalidated();




        counter++;


        //add new item






    }
}
