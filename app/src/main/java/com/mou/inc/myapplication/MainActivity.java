package com.mou.inc.myapplication;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {

    Context mContext;
    private String mFileName;
    private Note mLoadedNote = null;

    private ListView mListNotes;
    private ArrayAdapter<String> listAdapter ;

    public static String listviewTitle;
    public static String listviewSummary;

    public static Drawable getResBackground(Drawable resBackground) {
        return resBackground;
    }

    public static String getListviewTitle() {

        return listviewTitle;
    }

    public static String getListviewSummary() {
        return listviewSummary;
    }

    public static int [] prgmImages={R.drawable.ic_delete,R.drawable.ic_pen};
    public static String [] prgmNameList={"Delete","Rename "};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();
        setContentView(R.layout.activity_main);
        NotificationEventReceiver.setupAlarm(getApplicationContext());
        getWindow().getAttributes().windowAnimations = R.style.Fade;







        /**first time use
         *
         */
        final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {

            // launched for first time, do something
            startActivity(new Intent(MainActivity.this, FirstUseActivity.class));

            Log.d("Comments", "First time");



            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }



        mListNotes = (ListView) findViewById(R.id.main_listview);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.background_material_light));
        myToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.colorPrimary));


        //Check if password verification enabled


        }


/**Test**/
public void onSendNotificationsButtonClick(View view) {
    NotificationEventReceiver.setupAlarm(getApplicationContext());
}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    // To prevent crash on resuming activity  : interaction with fragments allowed only after Fragments Resumed or in OnCreate
    // http://www.androiddesignpatterns.com/2013/08/fragment-transaction-commit-state-loss.html
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        // handleIntent();
    }


//Maim
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_create: //run NoteActivity in new note mode
                //startActivity(new Intent(MainActivity.this, NoteActivity.class));

                newCreate();


                break;


            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));



                //TODO show settings activity
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {


        super.onPause();
    }


public void newCreate(){
    final Dialog newCreate = new Dialog(MainActivity.this,R.style.CustomDialogVertical);


    newCreate.setContentView(R.layout.new_create_dialog);
    newCreate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    newCreate.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    newCreate.setCancelable(true);
    newCreate.getWindow().setGravity(Gravity.TOP);


    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    Window window = newCreate.getWindow();
    lp.copyFrom(window.getAttributes());
    //This makes the dialog take up the full width
    lp.width = WindowManager.LayoutParams.FILL_PARENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    window.setAttributes(lp);


    String[] items = new String[]{"Note", "TODO-list"};

    Integer[] icons={R.drawable.ic_cancel ,R.drawable.ic_pen};

    CreateListAdapter adapter = new CreateListAdapter(MainActivity.this , items,icons);
    ListView lv =(ListView)newCreate.findViewById(R.id.create_list);
    final ImageView createIconView =(ImageView)newCreate.findViewById(R.id.image_list_create_dialog);
    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position==0){
                Intent i=new Intent(MainActivity.this,NoteActivity.class);
                startActivity(i);
                newCreate.cancel();
            }
            if(position==1){


                newCreate.cancel();
            }
        }
    });

    lv.setAdapter(adapter);


    newCreate.show();

}





    @Override
    protected void onResume() {


        super.onResume();


        // changing app theme error
        String userName = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("userName", null);



        //load saved notes into the listview ***ERROR FIXED**
      mListNotes.setAdapter(null);
        final ArrayList<Note> notes = Utilities.getAllSavedNotes(getApplicationContext());
        final ListView mListview = (ListView) findViewById(R.id.main_listview);

        final Handler handler = new Handler();
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListview.animate().translationX(0);
            }
        }, 300);*/






        //sort notes from new to old ***ERROR FIXED**

        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note lhs, Note rhs) {
                if(lhs.getDateTime() > rhs.getDateTime()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });



        mListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {













                final Dialog dialog = new Dialog(MainActivity.this, R.style.CustomDialogHorizontal);

                dialog.setContentView(R.layout.options_dialog);
                TextView dialogHeader =(TextView)dialog.findViewById(R.id.dialog_header);
                String selected = mListview.getItemAtPosition(position).toString();

                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                String[] options = new String[]{"Delete", "Rename"};
                int[] optionsIcon = new int[]{R.drawable.ic_cancel ,R.drawable.ic_pen};
                ListAdapter adapter = new ArrayAdapter<String>(MainActivity.this ,R.layout.view_list_dialog, R.id.textOptions, options);
                ListView lv =(ListView)dialog.findViewById(R.id.lv_target);

                final ImageView oprtionsIcon =(ImageView)dialog.findViewById(R.id.imageOptions);




                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {





                        if(position == 1){ //Remame



                            Dialog renameDialog =new Dialog(MainActivity.this );
                            setContentView(R.layout.rename_dialog_mainactivity);
                            renameDialog.show();

                            renameDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


                            renameDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            EditText mEtRename =(EditText)renameDialog.findViewById(R.id.rename_et);
                            //String renamed = mEtRename.getText().toString();

                            renameDialog.cancel();





                        }
                        if(position == 0){
                            AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Delete")
                                    .setMessage("  delete -" +  "- ?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogDelete, int which) {



                                            Toast.makeText(MainActivity.this, "Delete", Toast.LENGTH_SHORT).show();

                                            String fileName = ((Note) mListNotes.getItemAtPosition(position)).getDateTime()
                                                    + Utilities.FILE_EXTENSION;
                                            Utilities.deleteFile(getApplicationContext(), fileName);

                                            mListview.animate().cancel();
                                            Intent i =new Intent(MainActivity.this , MainActivity.class);startActivity(i);//refreshing list
                                            overridePendingTransition(0, 0);//canceling layout transition






                                        }
                                    })
                                    .setNegativeButton("NO", null); //do nothing on clicking NO button :P

                            dialogDelete.show();




                        }

                    }
                });
                final ArrayList<Note> notes = Utilities.getAllSavedNotes(getApplicationContext());
                        final ListView mListview = (ListView) findViewById(R.id.main_listview);












                dialog.show();
                /**
                AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete")
                        .setMessage("  delete -" +  "- ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Dialog dialog = new Dialog(Activity.this);
                                dialog.setContentView(R.layout.list)








                                overridePendingTransition(0, 0);

                            }
                        })
                        .setNegativeButton("NO", null); //do nothing on clicking NO button :P

                dialogDelete.show();**/


                return true;
            }

        });
        if ( notes != null && notes.size() > 0) { //check if we have any notes!
            final NoteAdapter na = new NoteAdapter(this, R.layout.view_note_item, notes);
            mListNotes.setAdapter(na);

            //set click listener for items in the list, by clicking each item the note should be loaded into NoteActivity
            mListNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //run the NoteActivity in view/edit mode
                    String fileName = ((Note) mListNotes.getItemAtPosition(position)).getDateTime()
                            + Utilities.FILE_EXTENSION;
                    Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                    viewNoteIntent.putExtra(Utilities.EXTRAS_NOTE_FILENAME, fileName);
                    startActivity(viewNoteIntent);
                }
            });

        } else { //remind user that we have no notes!





            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    newCreate();

                }
            }, 1200);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(mListNotes,"you have no saved notes !\ncreate some new notes :)" ,Snackbar.LENGTH_LONG).show();


                }
            },600);

        }
    }



}