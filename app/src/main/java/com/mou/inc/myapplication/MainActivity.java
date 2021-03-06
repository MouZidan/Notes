package com.mou.inc.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;


    Context mContext;
    private String mFileName;
    private Note mLoadedNote = null;

    private ListView mListNotes;
    private ArrayAdapter<String> listAdapter ;

    public static String listviewTitle;
    public static String listviewSummary;
    public static int clickedPosition;

    public static Object clickedNote;

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
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));



        mListNotes = (ListView) findViewById(R.id.main_listview);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        myToolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.colorPrimary));

        String uInput0 =PreferenceManager.getDefaultSharedPreferences(this).getString("uInput0","");
        if (uInput0.isEmpty())getSupportActionBar().setTitle("Notes");else getSupportActionBar().setTitle(uInput0);

        checkAndroidVersion();

        //function: x^2+x=0
















        //startActivity(new Intent(MainActivity.this, FirstUseActivity.class));


        /**first time use
         *
         */
        final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {

            // launched for first time, do something
            startActivity(new Intent(MainActivity.this, FirstUseActivity.class));

            Log.d("Comments", "First time");



            // recordAudio the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }








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


    String[] items = new String[]{"Text Note", "Audio Note", "Checklist"};
    Integer[] icons={R.drawable.ic_text, R.drawable.ic_mic, R.drawable.ic_checklist};


    CreateListAdapter adapter = new CreateListAdapter(MainActivity.this , items,icons);

    ListView lv =(ListView)newCreate.findViewById(R.id.create_list);
    lv.setAdapter(adapter);

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position==0){
                Intent i=new Intent(MainActivity.this,NoteActivity.class);
                startActivity(i);
                newCreate.cancel();
            }
            if(position==1){
                Intent i=new Intent(MainActivity.this,RecorderActivity.class);
                startActivity(i);
                newCreate.cancel();
            }

            if(position==2){
                Intent i=new Intent(MainActivity.this,ToDoActivity.class);
                startActivity(i);
                newCreate.cancel();
            }

        }
    });



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













                final Dialog dialogNoteItem = new Dialog(MainActivity.this, R.style.CustomDialogHorizontal);

                dialogNoteItem.setContentView(R.layout.options_dialog);
                dialogNoteItem.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                TextView dialogHeader =(TextView)dialogNoteItem.findViewById(R.id.dialog_header);
                String selected = mListview.getItemAtPosition(position).toString();

                String[] options = new String[]{"Delete", "Rename"};
                int[] optionsIcon = new int[]{R.drawable.ic_cancel ,R.drawable.ic_pen};
                final ListAdapter adapter = new ArrayAdapter<String>(MainActivity.this ,R.layout.view_list_dialog, R.id.textOptions, options);
                ListView lv =(ListView)dialogNoteItem.findViewById(R.id.lv_target);

                final ImageView oprtionsIcon =(ImageView)dialogNoteItem.findViewById(R.id.imageOptions);





                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {





                        if(position == 1){ //Remame

                            dialogNoteItem.cancel();



                            Dialog renameDialog =new Dialog(MainActivity.this, R.style.appCompatDialog);
                            setContentView(R.layout.rename_dialog_mainactivity);
                            renameDialog.show();





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


                                            Intent i =new Intent(MainActivity.this , MainActivity.class);
                                            startActivity(i);//refreshing list
                                            overridePendingTransition(0, 0);//canceling layout transition






                                        }
                                    })
                                    .setNegativeButton("NO", null);

                             dialogDelete.show();




                        }

                    }
                });
                final ArrayList<Note> notes = Utilities.getAllSavedNotes(getApplicationContext());
                        final ListView mListview = (ListView) findViewById(R.id.main_listview);












        dialogNoteItem.show();
                /**
                AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete")
                        .setMessage("  delete -" +  "- ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogNoteItem, int which) {

                                Dialog dialogNoteItem = new Dialog(Activity.this);
                                dialogNoteItem.setContentView(R.layout.list)








                                overridePendingTransition(0, 0);

                            }compare
                        })
                        .setNegativeButton("NO", null); //do nothing on clicking NO button :P

                dialogDelete.show();**/


                return true;
            }

        });

        if ( notes != null && notes.size() > 0) { //check if we have any notes!
            final NoteAdapter na = new NoteAdapter(this, R.layout.view_note_item_text, notes);
            mListNotes.setAdapter(na);

            //set click listener for items in the list, by clicking each item the note should be loaded into NoteActivity
            mListNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    clickedPosition = position;
                    //run the NoteActivity in view/edit mode
                    String fileName = ((Note) mListNotes.getItemAtPosition(position)).getDateTime()
                            + Utilities.FILE_EXTENSION;
                    int noteType = ((Note) mListview.getItemAtPosition(position)).getType();


                    if (noteType==Note.AUDIO){
                        Log.d("MainActivity", "fileType : AUDIO ");


                        clickedNote =(mListview.getItemAtPosition(position));



                        Intent i=new Intent(MainActivity.this,PlayAudioActivity.class);
                        startActivity(i);


                    }else if(noteType==Note.TEXT){

                        Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                        viewNoteIntent.putExtra(Utilities.EXTRAS_NOTE_FILENAME, fileName);
                        startActivity(viewNoteIntent);
                    }else Log.e("MainActivity", "ERORR: unknown note type. ");





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
    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();

        } else {
            // write your logic here
        }

    }
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.RECORD_AUDIO)) {

                Snackbar.make(this.findViewById(android.R.id.content),
                        "Please Grant Permissions to recordAudio audio,",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
        }
    }



}