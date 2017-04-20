package com.mou.inc.myapplication;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoteActivity extends AppCompatActivity {

    private boolean mIsViewingOrUpdating;
    private long mNoteCreationTime;
    private String mFileName;
    private Note mLoadedNote;

    private EditText mEtTitle;
    private EditText mEtContent;

    private FloatingActionMenu mMenu;
    private FloatingActionButton mInserttime;

    private LinearLayout mLayout;

    private Boolean mNightBoolean;
    private Boolean mAlignmentBoolean;

    private String mHashtags ="";

    private Boolean mActivatePIN;
    private String mPINstring;

    private EditText pinSetETdialog;
    private EditText pinSetConfirmETdialog;
    private Button setButtonDialog;
    private TextView pinStatus;
    private Dialog cdialog;

    private int timePickerHour;
    private int timePickerMinute;


    public static String dailyCurrentDateTimeString;
    public static String notifyTimePicker;
    public static Boolean reminderBoolean =false;
    public static String notifyUserContent;

    public static String userContentString;




    private PendingIntent pendingIntent;


    public static final String PREFS_NAME = "MyPrefsFile";

    private BottomSheetBehavior mBottomSheetBehavior2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.background_material_light));



        //overridePendingTransition(0, 0);//canceling layout transition
        getWindow().getAttributes().windowAnimations = R.style.Fade;




        mLayout =(LinearLayout)findViewById(R.id.linearLayout) ;
        mMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        mInserttime = (FloatingActionButton) findViewById(R.id.fab_insertTime);









        mEtTitle = (EditText) findViewById(R.id.note_et_title);
        mEtContent = (EditText) findViewById(R.id.note_et_content);

        mAlignmentBoolean =false;
        mNightBoolean =false;






        final View bottomSheet2 = findViewById(R.id.bottom_sheet2);
        mBottomSheetBehavior2 = BottomSheetBehavior.from(bottomSheet2);
        mBottomSheetBehavior2.setHideable(true);
        mBottomSheetBehavior2.setPeekHeight(300);
        mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);




        mEtContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                SpannableString hashText = new SpannableString(mEtContent.getText().toString());
                Matcher matcher = Pattern.compile("#\\S+").matcher(hashText);
                while (matcher.find()) {
                    hashText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(NoteActivity.this, R.color.colorAccent))
                            , matcher.start(), matcher.end(), 0);

                    mEtContent.setText(hashText);
                }
            }
        });
        mEtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);

            }
        });

        mEtContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);




                if(mBottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);

                }
                else if(mBottomSheetBehavior2.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);

                }


                //mEtTitle.setText(hashtags);

                return true;
            }
        });





        //check if view/edit note bundle is set, otherwise user wants to create new note
        mFileName = getIntent().getStringExtra(Utilities.EXTRAS_NOTE_FILENAME);
        if(mFileName != null && !mFileName.isEmpty() && mFileName.endsWith(Utilities.FILE_EXTENSION)) {
            mLoadedNote = Utilities.getNoteByFileName(getApplicationContext(), mFileName);
            if (mLoadedNote != null) {
                //update the widgets from the loaded note
                mEtTitle.setText(mLoadedNote.getTitle());
                mEtContent.setText(mLoadedNote.getContent());
                mNightBoolean=(mLoadedNote.getNight());
                mAlignmentBoolean=(mLoadedNote.getAlignment());
                mActivatePIN = (mLoadedNote.getPin());
                mPINstring =(mLoadedNote.getPinString());



                mNoteCreationTime = mLoadedNote.getDateTime();
                mIsViewingOrUpdating = true;
            }
        } else { //user wants to create a new note
            mNoteCreationTime = System.currentTimeMillis();
            mIsViewingOrUpdating = false;
            mActivatePIN = (false);


        }





        clickableHashtags();
        }

    private void setHashtags() {

        String txt = mEtContent.getText().toString();
        Pattern pattern = Pattern.compile("#\\S+");
        Matcher matcher = pattern.matcher(txt);
        while (matcher.find())
        {

            mHashtags =mHashtags+matcher.group()+" ";

        }
        //Toast.makeText(NoteActivity.this, mHashtags, Toast.LENGTH_SHORT).show();

    }

    private void clickableHashtags() {
        setHashtags();
        String definition = mHashtags.trim();
        TextView definitionView = (TextView) findViewById(R.id.bottom_sheet_hashtags);
        definitionView.setMovementMethod(LinkMovementMethod.getInstance());
        definitionView.setText(definition, TextView.BufferType.SPANNABLE);
        Spannable spans = (Spannable) definitionView.getText();
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(definition);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
                .next()) {
            String possibleWord = definition.substring(start, end);
            if (Character.isLetterOrDigit(possibleWord.charAt(0))) {
                ClickableSpan clickSpan = getClickableSpan(possibleWord);
                spans.setSpan(clickSpan, start, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private ClickableSpan getClickableSpan(final String word) {
        return new ClickableSpan() {
            final String mWord;
            {
                mWord = word;
            }

            @Override
            public void onClick(View widget) {
                Log.d("tapped on:", mWord);


                String contetnS = mEtContent.getText().toString();
                int start=contetnS.indexOf(mWord);
                int end=start+mWord.length();
                mEtContent.setSelection(start,end);
            }

            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() { //app theme changing
        //PIN dialog
        if(mActivatePIN == true) {
            CheckDialog checkPIN = new CheckDialog();
            checkPIN.showCheckDialog(this);
        }

        //load night mode state
        if (mNightBoolean == true && mLoadedNote != null ){
            activateNight();
        }
        //load alignment state
        if(mAlignmentBoolean == true){
            mEtContent.setGravity(Gravity.CENTER | Gravity.TOP);

        }else{            mEtContent.setGravity(Gravity.LEFT);
        }



       getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + mEtTitle.getText() +" Note" + "</font>"));


        super.onResume();
    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        //load menu based on the state we are in (new, view/update/delete)
        if(mIsViewingOrUpdating) { //user is viewing or updating a note
            getMenuInflater().inflate(R.menu.menu_note_view, menu);
        } else { //user wants to create a new note
            getMenuInflater().inflate(R.menu.menu_note_add, menu);
        }


        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       //define
        EditText etContent = (EditText) findViewById(R.id.note_et_content);

        switch (item.getItemId()) {



            case R.id.action_save_note: //save the note
            case R.id.action_update: //oupdate note
                validateAndSaveNote();
                break;

            case R.id.action_delete:
                actionDelete();
                break;

            case R.id.action_cancel: //cancel the note quit
                actionCancel();
                break;
            case R.id.action_font_size:


                final Dialog fontSize =new Dialog(NoteActivity.this );
                fontSize.requestWindowFeature(Window.FEATURE_NO_TITLE);
                fontSize.setContentView(R.layout.change_font_size_dialog);

                fontSize.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                Button setBtn =(Button) fontSize.findViewById(R.id.set_fontsize);
                Button defaultBtn =(Button) fontSize.findViewById(R.id.rest_fontsize);
                SeekBar seekBar =(SeekBar) fontSize.findViewById(R.id.textSize_seekbar);

                Button color11 =(Button) fontSize.findViewById(R.id.color11);
                Button color21 =(Button) fontSize.findViewById(R.id.color21);
                Button color31 =(Button) fontSize.findViewById(R.id.color31);
                Button color41 =(Button) fontSize.findViewById(R.id.color41);

                Button color12 =(Button) fontSize.findViewById(R.id.color12);
                Button color22 =(Button) fontSize.findViewById(R.id.color22);
                Button color32 =(Button) fontSize.findViewById(R.id.color32);
                Button color42 =(Button) fontSize.findViewById(R.id.color42);


                color11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    mEtContent.setTextColor(ContextCompat.getColor(NoteActivity.this, R.color.holo_orange_dark));



                    }
                });
                color21.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
/*
                        mEtContent.setTextColor(ContextCompat.getColor(NoteActivity.this, R.color.holo_blue_dark));
*/

                    }
                });
                color31.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEtContent.setTextColor(ContextCompat.getColor(NoteActivity.this, R.color.holo_green_dark));

                    }
                });
                color41.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEtContent.setTextColor(ContextCompat.getColor(NoteActivity.this, R.color.holo_red_dark));

                    }
                });

                color12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEtTitle.setTextColor(ContextCompat.getColor(NoteActivity.this, R.color.holo_orange_dark));

                    }
                });
                color22.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEtTitle.setTextColor(ContextCompat.getColor(NoteActivity.this, R.color.holo_blue_dark));

                    }
                });
                color32.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEtTitle.setTextColor(ContextCompat.getColor(NoteActivity.this, R.color.holo_green_dark));

                    }
                });
                color42.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEtTitle.setTextColor(ContextCompat.getColor(NoteActivity.this, R.color.holo_red_dark));

                    }
                });



                setBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fontSize.cancel();
                    }
                });

                defaultBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mEtContent.setTextSize(16);
                        mEtTitle.setTextSize(18);

                        if(mNightBoolean==true){
                            mEtTitle.setTextColor(Color.WHITE);
                            mEtContent.setTextColor(Color.WHITE);

                        }else{

                            mEtTitle.setTextColor(ContextCompat.getColor(NoteActivity.this, R.color.colorPrimaryDark));
                            mEtContent.setTextColor(ContextCompat.getColor(NoteActivity.this, R.color.colorPrimaryDark));
                        }
                    }
                });
                seekBar.setMax(55);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser) {

                        int textSizeContent = progress+16;int textSizeTitle = progress+18;
                            mEtContent.setTextSize(textSizeContent);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }
                });






                fontSize.show();

                break;



        }

        return super.onOptionsItemSelected(item);
    }


    @Override



    public void onBackPressed() {

        actionCancel();
        overridePendingTransition(0, 0);




    }

    /**
     * Handle delete action
     */







        public void activatePasswordDialog(View view) {
            ViewDialog alert = new ViewDialog();
            if(mActivatePIN == false){
            String firsttime = "Set your PIN, you will be asked to enter it every time you open this note.";
            alert.showDialog(this, firsttime);
            }else {
                String firsttime = "Change your PIN.";
                alert.showDialog(this, firsttime);}
    }


    public void setReminder(View view) {

        Reminder reminder = new Reminder();
        reminder.showDialog(this ,this);

        Calendar calendar;
        String specificCurrentDateTimeString;

        SimpleDateFormat daily = new SimpleDateFormat("H:m");
        SimpleDateFormat specific = new SimpleDateFormat("H:m");


        calendar = Calendar.getInstance();

        dailyCurrentDateTimeString = daily.format(calendar.getTime());
        specificCurrentDateTimeString = specific.format(calendar.getTime());

        Toast.makeText(this, "current time is ; "+dailyCurrentDateTimeString + " " +notifyTimePicker, Toast.LENGTH_SHORT).show();


        if(dailyCurrentDateTimeString.equals(notifyTimePicker)){
            Toast.makeText(this, "notifyTime matches current ", Toast.LENGTH_SHORT).show();
        }










    }


    public void activateCenter(View view) {

        if (mAlignmentBoolean == true) {
            mEtContent.setGravity(Gravity.LEFT);

            mAlignmentBoolean =false;
        }else {
            mEtContent.setGravity(Gravity.CENTER | Gravity.TOP);
            mAlignmentBoolean=true;

        }




    }

    public void activateNight(View view) {


        if (mNightBoolean == true) {
            deactivateNight();

            mNightBoolean = false;
        } else {
            mNightBoolean = true;
            activateNight();

        }
    }







    private void setOnFocusChangeListener(TextView textView, String name){
        setOnFocusChangeListener(mEtContent, "name");

        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    Toast.makeText(NoteActivity.this, "name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



        /**Test**/

    public void insertTime(View view) {
        Calendar calendar;
        String currentDateTimeString;
        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy h:mm a");

        calendar = Calendar.getInstance();
        currentDateTimeString = df.format(calendar.getTime());



        mEtContent.setText(mEtContent.getText().toString()+ "\n" +currentDateTimeString, TextView.BufferType.EDITABLE);
    mInserttime.animate().translationX(mInserttime.getHeight());

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mInserttime.setVisibility(View.GONE);

            }
        }, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mInserttime.animate().translationX(0);
                mInserttime.setVisibility(View.VISIBLE);

            }
        }, 60000);
    }




    public void SetAlarm(Context c, long AlarmTime, int ItemID, String Message, Boolean Set) {
        Calendar calendar = Calendar.getInstance();
/**if some thing true**/
       /* calendar.set(Calendar.MONTH, month); //int
        calendar.set(Calendar.YEAR, 2013); //int
        calendar.set(Calendar.DAY_OF_MONTH, 13); //int*/

        calendar.set(Calendar.HOUR_OF_DAY, 20); //int
        calendar.set(Calendar.MINUTE, 48); //int
        calendar.set(Calendar.SECOND, 0); //int

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

    }



    private void actionDelete() {


        //ask user if he really wants to delete the note!
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("  delete -" +  mLoadedNote.getTitle() + "- ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mLoadedNote != null && Utilities.deleteFile(getApplicationContext(), mFileName)) {
                            Toast.makeText(NoteActivity.this, mLoadedNote.getTitle() + " is deleted"
                                    , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NoteActivity.this, "can not delete the note '" + mLoadedNote.getTitle() + "'"
                                    , Toast.LENGTH_SHORT).show();
                        }
                        finish();
                        overridePendingTransition(0, 0);

                    }
                })
                .setNegativeButton("NO", null); //do nothing on clicking NO button :P

        dialogDelete.show();
    }

    /**
     * Handle cancel action
     */
    private void actionCancel() {

        if(!checkNoteAltred()) { //if note is not altered by user (user only viewed the note/or did not write anything)
            finish(); //go back to MainActivity
        } else { // showing a dialog
            AlertDialog.Builder dialogCancel = new AlertDialog.Builder(this)
                    .setTitle("discard changes...")
                    .setMessage("are you sure you do not want to save changes to this note?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish(); //just go back to main activity
                        }
                    })
                    .setNegativeButton("NO", null); //null = stay in the activity!
            dialogCancel.show();
        }
    }

    /**
     * Check to see if a loaded note/new note has been changed by user or not
     * @return true if note is changed, otherwise false
     */
    private boolean checkNoteAltred() {

        if(mIsViewingOrUpdating) { //if in view/update mode
            return mLoadedNote != null && (!mEtTitle.getText().toString().equalsIgnoreCase(mLoadedNote.getTitle())
                    || !mEtContent.getText().toString().equalsIgnoreCase(mLoadedNote.getContent()));
        } else { //if in new note mode
            return !mEtTitle.getText().toString().isEmpty() || !mEtContent.getText().toString().isEmpty();
        }
    }

    /**
     * Validate the title and content and save the note and finally exit the activity and go back to MainActivity
     */


    private void validateAndSaveNote() {

        setHashtags();
        //get the content of widgets to make a note object
        String title = mEtTitle.getText().toString();
        String content = mEtContent.getText().toString();
        Boolean night = mNightBoolean;
        Boolean alignment = mAlignmentBoolean;

        Boolean pinActiveS = mActivatePIN;
        String pinString = mPINstring;

        String hashtags =mHashtags;
        Toast.makeText(this, hashtags, Toast.LENGTH_SHORT).show();



        //see if user has entered anything :D lol
        if(title.isEmpty()) { //title
            Toast.makeText(NoteActivity.this, "please enter a title!"
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        if(content.isEmpty()) { //content
            Toast.makeText(NoteActivity.this, "please enter a content for your note!"
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        //set the creation time, if new note, now, otherwise the loaded note's creation time
        if(mLoadedNote != null) {
            mNoteCreationTime = mLoadedNote.getDateTime();
        } else {
            mNoteCreationTime = System.currentTimeMillis();
        }
        //finally save the note!
        if(Utilities.saveNote(this, new Note(mNoteCreationTime, title, content, night,alignment , pinActiveS ,pinString,hashtags))) { //success!
            //tell user the note was saved!
            Toast.makeText(this, "note has been saved", Toast.LENGTH_SHORT).show();
        } else { //failed to save the note! but this should not really happen :P :D :|
            Toast.makeText(this, "can not save the note. make sure you have enough space " +
                    "on your device", Toast.LENGTH_SHORT).show();
        }

        finish(); //exit the activity, should return to MainActivity
    }
    //NightMode
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)


    private void fadeColor(View view ,int from ,int to){
        ObjectAnimator.ofObject(view, "backgroundColor", new ArgbEvaluator(),from ,to)
                .setDuration(500)
                .start();
    }
    public void activateNight(){



        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        mEtTitle.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mEtContent.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        mEtContent.setHintTextColor(Color.GRAY);
        mEtTitle.setHintTextColor(Color.GRAY);
        int black =Color.BLACK;
        int colorAccent =ContextCompat.getColor(this, R.color.colorAccent);
        int colorPrimary =ContextCompat.getColor(this, R.color.colorPrimary);



    fadeColor(mEtContent, colorPrimary, black);
    fadeColor(mEtTitle, colorPrimary, black);
    fadeColor(myToolbar, colorAccent, black);








        mLayout.setBackgroundColor(Color.BLACK);

        mMenu.setMenuButtonColorNormal(ContextCompat.getColor(this, R.color.colorPrimary));
        mMenu.setMenuButtonColorPressed(ContextCompat.getColor(this, R.color.colorAccent));
        //getWindow().setNavigationBarColor(Color.BLACK);
        //getWindow().setStatusBarColor(Color.BLACK);
        mMenu.animate().alpha(0.5f).setDuration(1000);


    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void deactivateNight(){
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        int black =Color.BLACK;
        int colorAccent =ContextCompat.getColor(this, R.color.colorAccent);
        int colorPrimary =ContextCompat.getColor(this, R.color.colorPrimary);




        fadeColor(mEtContent, black, colorPrimary );
        fadeColor(mEtTitle, black, colorPrimary );
        fadeColor(myToolbar, black, colorAccent );

        mEtContent.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mEtTitle.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mEtTitle.setBackgroundResource(R.drawable.rounded_cornerstop);
        myToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.background_material_light));
        mMenu.setMenuButtonColorNormal(Color.TRANSPARENT);
        mMenu.setMenuButtonColorPressed(Color.TRANSPARENT);
        mMenu.animate().alpha(1f).setDuration(1000);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.background_material_light));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.background_material_light));


    }


    public static void sendNotify(Context context){

        Random rand=new Random();
        int nN = rand.nextInt((5 - 2) + 1) + 1;
        String[] announce ={"Don't forget to ...", "You planned to do...", "Hey, i think you should...", "Hmm...",
                "hey, I'm Your Reminder... "};
        NewNotification.notify(context,announce[nN],notifyUserContent);
    }


    public class Reminder {


        public void showDialog(final Activity activity ,Context context){
            final Dialog Rdialog = new Dialog(activity);
            Rdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Rdialog.setCancelable(false);
            Rdialog.setContentView(R.layout.reminder_dialog);

            setButtonDialog = (Button) Rdialog.findViewById(R.id.set_btn_reminder_dialog);
            final Button backButtonDialog = (Button) Rdialog.findViewById(R.id.back_btn_reminder_dialog);
            final TimePicker timePicker = (TimePicker)Rdialog.findViewById(R.id.timepicker_reminder_dialog);
            timePicker.clearFocus();
            final Button nextViewButtonDialog = (Button) Rdialog.findViewById(R.id.view_nextbtn_dialog);
            final ViewSwitcher viewSwitcher = (ViewSwitcher)Rdialog.findViewById(R.id.viewSwitcher1);

            final LinearLayout myFirstView=(LinearLayout) findViewById(R.id.first_view_Rdialog);
            final LinearLayout mySecondView =(LinearLayout)Rdialog.findViewById(R.id.second_view_Rdialog);

            final EditText etRContent = (EditText) Rdialog.findViewById(R.id.notify_title_content);

            final Boolean[] allIsOk = new Boolean[1];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePickerHour = timePicker.getHour();
                timePickerMinute = timePicker.getMinute();

            }
            //SetAlarm(context, timePickerHour+timePickerMinute, 1, "", true);



            nextViewButtonDialog.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {





                    // TODO Auto-generated method stub
                    if (viewSwitcher.getCurrentView() != myFirstView){

                        viewSwitcher.showPrevious();
                    } else if(viewSwitcher.getCurrentView() != mySecondView){

                        viewSwitcher.showNext();
                    }



                }
            });

            setButtonDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        timePickerHour = timePicker.getHour();
                        timePickerMinute = timePicker.getMinute();

                    }else{
                        timePickerMinute = timePicker.getCurrentMinute();
                        timePickerHour = timePicker.getCurrentHour();

                    }


                    /*userContentString = etRContent.getText().toString();
                    notifyTimePicker = timePickerHour + ":" + timePickerMinute;
*/


                    if( userContentString.isEmpty() ){
                        Toast.makeText(NoteActivity.this, "All fields must be filled.", Toast.LENGTH_SHORT).show();

                    }else {

                       // notifyUserContent = userContentString;
                        reminderBoolean =true;
                        startService(new Intent(NoteActivity.this, AlarmIntentService.class));

                        Rdialog.dismiss();
                    }
                    //et fields










                }
            });
            backButtonDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    Rdialog.dismiss();

                }
            });

            Rdialog.show();

        }


    }

    public class ViewDialog {

         public void showDialog(Activity activity, String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.set_pin_dialog);

            pinSetETdialog = (EditText) dialog.findViewById(R.id.set_pin_dialog);
            pinSetConfirmETdialog = (EditText) dialog.findViewById(R.id.set_pin_dialog_confirm);
            setButtonDialog = (Button) dialog.findViewById(R.id.set_btn_dialog);
             Button cancelButtonDialog = (Button) dialog.findViewById(R.id.cancel_btn_dialog);
             Button clearPin = (Button) dialog.findViewById(R.id.clear_pin);
             if(mActivatePIN == false){

                 clearPin.setVisibility(View.GONE);
                 Toast.makeText(NoteActivity.this, " false ", Toast.LENGTH_SHORT).show();
             }else{clearPin.setVisibility(View.VISIBLE);}



             TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
             pinStatus = (TextView) dialog.findViewById(R.id.pin_status);

             text.setText(msg);

             setButtonDialog.setEnabled(false);


             setButtonDialog.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     mPINstring=pinSetETdialog.getText().toString();
                     mActivatePIN = true;

                     validateAndSaveNote();
                     dialog.dismiss();

                 }
             });
             clearPin.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     mPINstring="";
                     mActivatePIN = false;

                     validateAndSaveNote();
                     dialog.dismiss();

                 }
             });
             cancelButtonDialog.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {


                     dialog.dismiss();

                 }
             });




            dialog.show();
             insideViewDialog();


        }
    }

    public void insideViewDialog(){

        if(pinSetETdialog.getText().toString().isEmpty()|pinSetETdialog.getText().toString().equals("")) {
            pinStatus.setText("These fields cannot be empty.");
            pinStatus.setTextColor(Color.RED);



        }else {

            if (pinSetConfirmETdialog.getText().toString().equals(pinSetETdialog.getText().toString())) {

                setButtonDialog.setEnabled(true);
                pinStatus.setText("PIN fields match.");
                pinStatus.setTextColor(Color.GREEN);


            } else {
                setButtonDialog.setEnabled(false);
                pinStatus.setText("PIN fields doesn't match.");
                pinStatus.setTextColor(Color.RED);

            }
        }
           //call itself again and again...to Check
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                insideViewDialog();
            }
        }, 100);

    }

    //PIN dialog

    public class CheckDialog {
        public void showCheckDialog(Activity activity){
            cdialog = new Dialog(activity);
            cdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            cdialog.setCancelable(false);
            cdialog.setContentView(R.layout.get_pin_dialog);

            EditText getPIN = (EditText) cdialog.findViewById(R.id.the_PIN);
            Button backBtn = (Button) cdialog.findViewById(R.id.back_btn_checkdialog);

            String PIN = getPIN.getText().toString();

            if(mActivatePIN == false){

                cdialog.dismiss();
            }




            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();

                }
            });





            cdialog.show();
            insideCheckDialog();

        }

    }

    public void insideCheckDialog(){
        EditText getPIN = (EditText) cdialog.findViewById(R.id.the_PIN);

        if(getPIN.getText().toString().equals(mPINstring)){

            cdialog.dismiss();
        }
        //call itself again and again...to Check
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                insideCheckDialog();
            }
        }, 100);

    }



private void looberMOU(){

    //sendNotification(notifyTimePicker,notifyUserTitle, notifyUserContent, false);

    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            looberMOU();
        }
    }, 1000);
}
}

