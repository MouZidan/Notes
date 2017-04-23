package com.mou.inc.myapplication;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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

    private Boolean checLastL;

    private String internalHashs="";
    private String internalHashs0=null;

    private String mHashtags ="";
    private String mAt ="";
    private String internalAt="";
    private String internalAt0=null;



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

private int counter0=-1;


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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        mLayout =(LinearLayout)findViewById(R.id.linearLayout) ;
        mMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        mInserttime = (FloatingActionButton) findViewById(R.id.fab_insertTime);



        TextView extraView = (TextView) findViewById(R.id.bottom_sheet_at_extra);
        extraView.setText("@; ");





        mEtTitle = (EditText) findViewById(R.id.note_et_title);
        mEtContent = (EditText) findViewById(R.id.note_et_content);

        mAlignmentBoolean =false;
        mNightBoolean =false;




        final View bottomSheet2 = findViewById(R.id.bottom_sheet2);
        mBottomSheetBehavior2 = BottomSheetBehavior.from(bottomSheet2);
        mBottomSheetBehavior2.setPeekHeight(100);
        mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);





        mMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if(mMenu.isOpened() && (!mHashtags.equals("")|!mAt.equals("")) ){

                    mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);

                }else mBottomSheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);

            }
        });

                mEtContent.addTextChangedListener(new TextWatcher() {
                    CountDownTimer timer = null;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        final TextWatcher textWatcher=this;

                        if (timer != null) {
                            timer.cancel();
                        }


                        timer = new CountDownTimer(100, 1000) {

                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {

                                final int selectionStartHash = mEtContent.getSelectionStart() - mEtContent.getSelectionEnd();
                                final int selectionStartAt = mEtContent.getSelectionStart() - mEtContent.getSelectionEnd();

                                //do what you wish

                                //coloredSpanThread(selectionStartAt ,textWatcher);




                                coloredSpanThread(selectionStartAt,textWatcher);




                            }

                        }.start();




                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

//set
        mEtContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


            }
        });
        mEtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenu.close(true);



//                Toast.makeText(NoteActivity.this, Utilities.getFileNandD(NoteActivity.this,String.valueOf(mLoadedNote.getDateTime())+".bin"), Toast.LENGTH_SHORT).show();

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




bottomSheetClick();
        }


    private void clickableHashtags() {
        String txt = mEtContent.getText().toString();
        Pattern pattern = Pattern.compile("#\\S+");
        Matcher matcher = pattern.matcher(txt);

        String internalHashs="";

        int myMatches = 0;

        TextView definitionView = (TextView) findViewById(R.id.bottom_sheet_hashtags);
        TextView extraView = (TextView) findViewById(R.id.bottom_sheet_hashtags_extra);
        if(myMatches<=120) {

            while (matcher.find()) {
                internalHashs = internalHashs + matcher.group() + " ";
                internalHashs0=matcher.group();

                myMatches++;

            }

            if (myMatches == 0) {
                mHashtags = "";
                internalHashs0 = "";
                definitionView.setVisibility(View.GONE);
                extraView.setVisibility(View.GONE);


            } else {
                if (!matcher.find()) {
                    definitionView.setVisibility(View.VISIBLE);
                    extraView.setVisibility(View.VISIBLE);

                    mHashtags = internalHashs;
                    internalHashs0 = internalHashs;
                }
            }


            String definition = mHashtags.trim() + " ";

            if (!mHashtags.equals("")) {
                definitionView.setVisibility(View.VISIBLE);
            } else {
                definitionView.setVisibility(View.GONE);
            }

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
                    ClickableSpan clickSpan = getClickableSpanHash(possibleWord);
                    spans.setSpan(clickSpan, start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

    }
    private void coloredSpanThread(final int selectionStartAt , final TextWatcher textWatcher){


        mEtContent.removeTextChangedListener(textWatcher);

                final SpannableString atText = new SpannableString(mEtContent.getText().toString());
                final Matcher matcherAt = Pattern.compile("@\\S+").matcher(atText);
        String command;
//        String commandfromPref = PreferenceManager.getDefaultSharedPreferences(NoteActivity.this).getString("cDAT", "");

        if(!SettingsActivity.commandfromPref.equals("")){command=" "+SettingsActivity.commandfromPref;
        }else {command=" .cDAT";}


        if (selectionStartAt == 0 && mEtContent.getText().toString().contains(command)) {
            final int oldCursorPosition = mEtContent.getSelectionStart();

            mEtContent.setText(mEtContent.getText().toString().replace(command, "@" + currentDateTimeString()));
            try {
                coloredSpanThread(selectionStartAt,textWatcher);
                mEtContent.setSelection(oldCursorPosition);
            } catch (IndexOutOfBoundsException e) {}

        }

        String s = mEtContent.getText().toString();
        int counter = 0;
        for( int i=0; i<s.length(); i++ ) {
            if( s.charAt(i) == '@' ) {
                counter++;
            }
        }

                        while (matcherAt.find() && counter!=counter0 && counter<400 )  {

                            if (selectionStartAt == 0) {

                                final int oldCursorPosition = mEtContent.getSelectionStart();

                                atText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(NoteActivity.this, R.color.atColor))
                                        , matcherAt.start(), matcherAt.end(), 0);

                                mEtContent.setText(atText);
                                try {
                                    mEtContent.setSelection(oldCursorPosition);
                                } catch (IndexOutOfBoundsException e) {
                                }


                                //mEtTitle.setText(beforeEdits);

                            }

                            clickableHashtags();
                            clickableAt();

                        }
                        if (counter>= 400)
                        {
                            Toast.makeText(this, "you reached the maximum amount of '@' at this note (400 @)\n Warning:if you add more the app probably will crash", Toast.LENGTH_LONG).show();
                        }else if(counter==0){counter0=-1;}else {counter0=counter;}

        mEtContent.addTextChangedListener(textWatcher);








    }

    private ClickableSpan getClickableSpanHash(final String word) {
        return new ClickableSpan() {
            final String mWord;
            {
                mWord = word;
            }

            @Override
            public void onClick(View widget) {
                Log.d("tapped on:", mWord);

                String contetnS = mEtContent.getText().toString();
                int start=contetnS.indexOf(mWord)-1;
                int end=start+mWord.length()+1;
                mEtContent.setSelection(start,end);
            }


            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
            }
        };
    }

    private void clickableAt() {
        String txt = mEtContent.getText().toString();
        Pattern pattern = Pattern.compile("@\\S+");
        Matcher matcher = pattern.matcher(txt);

        String internalAt="";

        int myMatches = 0;
        TextView definitionView = (TextView) findViewById(R.id.bottom_sheet_at);
        TextView extraView = (TextView) findViewById(R.id.bottom_sheet_at_extra);


            while (matcher.find()) {
                internalAt = internalAt + matcher.group() + " ";

                internalAt0=matcher.group();

                myMatches++;

            }
            if (myMatches == 0) {
                mAt = "";

                definitionView.setVisibility(View.GONE);
                extraView.setVisibility(View.GONE);

            } else {
                if (!matcher.find()) {
                    definitionView.setVisibility(View.VISIBLE);
                    extraView.setVisibility(View.VISIBLE);
                    mAt = internalAt;
                    internalAt0 = internalAt;
                }
            }


            String definition = mAt.trim() + " ";


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
                    ClickableSpan clickSpan = getClickableSpanAt(possibleWord);
                    spans.setSpan(clickSpan, start, end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            definitionView.setTextColor(ContextCompat.getColor(this, R.color.atColor));

    }

    private ClickableSpan getClickableSpanAt(final String word) {
        return new ClickableSpan() {
            final String mWord;
            {
                mWord = word;
            }

            @Override
            public void onClick(View widget) {
                Log.d("tapped on:", mWord);

                String contetnS = mEtContent.getText().toString();
                int start=contetnS.indexOf(mWord)-1;
                int end=start+mWord.length()+1;
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
                int conSize=Math.round(mEtContent.getTextSize());
                seekBar.setProgress(conSize);
                Toast.makeText(this, seekBar.getProgress()+"  "+conSize, Toast.LENGTH_SHORT).show();

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
                seekBar.setMax(30);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                                                  boolean fromUser) {
                        int textSizeContent = progress+16;
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

    public void bottomSheetClick(){
        ImageButton selectAll =(ImageButton)findViewById(R.id.select_all_bs);
        ImageButton copy =(ImageButton)findViewById(R.id.copy_bs);
        ImageButton paste =(ImageButton)findViewById(R.id.paste_bs);
        ImageButton share =(ImageButton)findViewById(R.id.share_bs);



        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEtContent.isFocused()){mEtContent.setSelection(0,mEtContent.getText().toString().length());
                }else if(mEtTitle.isFocused()){mEtTitle.setSelection(0,mEtTitle.getText().toString().length());}
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEtContent.isFocused()) {
                    String copiedString = mEtContent.getText().toString();
                    int startIndex = mEtContent.getSelectionStart();
                    int endIndex = mEtContent.getSelectionEnd();
                    try {
                        copiedString = copiedString.substring(endIndex, startIndex);
                    } catch (StringIndexOutOfBoundsException e) {
                        copiedString = copiedString.substring(startIndex, endIndex);

                    }
                    if(!copiedString.isEmpty()) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(copiedString);
                        } else {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Copied Text", copiedString);
                            clipboard.setPrimaryClip(clip);
                        }
                        Toast.makeText(NoteActivity.this, "Copied to Clipboard!", Toast.LENGTH_SHORT).show();
                    }else{            Toast.makeText(NoteActivity.this, "Select some text to copy!", Toast.LENGTH_SHORT).show();
                    }
                } else if(mEtTitle.isFocused()){
                    String copiedString = mEtTitle.getText().toString();
                    int startIndex = mEtTitle.getSelectionStart();
                    int endIndex = mEtTitle.getSelectionEnd();
                    copiedString = copiedString.substring(startIndex, endIndex);
                    if(!copiedString.isEmpty()) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(copiedString);
                        } else {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Copied Text", copiedString);
                            clipboard.setPrimaryClip(clip);
                        }
                        Toast.makeText(NoteActivity.this, "Copied to Clipboard!", Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });

        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startIndexT = mEtTitle.getSelectionStart();
                int endIndexT = mEtTitle.getSelectionEnd();

                int startIndexC = mEtContent.getSelectionStart();
                int endIndexC = mEtContent.getSelectionEnd();


                if(mEtContent.isFocused()) {

                               try {

                                   mEtContent.getText().replace(startIndexC, endIndexC, readFromClipboard());

                               } catch (IndexOutOfBoundsException e) {
                                   mEtContent.getText().replace(endIndexC, startIndexC, readFromClipboard());

                               }
                           }else if(mEtTitle.isFocused()){

                               try {

                                   mEtTitle.getText().replace(startIndexT, endIndexT, readFromClipboard());

                               } catch (IndexOutOfBoundsException e) {
                                   mEtTitle.getText().replace(endIndexT, startIndexT, readFromClipboard());

                               }

                           }

            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int contetnSelection=mEtContent.getSelectionEnd()-mEtContent.getSelectionStart();
                int titleSelection=mEtTitle.getSelectionEnd()-mEtTitle.getSelectionStart();


                if(contetnSelection!=0 | titleSelection!=0){
                    getUserActionDialog();




                }else  if(mLoadedNote!=null) {
                    String theWholeNote="";

                    theWholeNote =
                            mEtTitle.getText().toString()
                                    + "\n -------------- \n"
                                    + mEtContent.getText().toString()
                                    +"\n \n I wrote this note at:"+mLoadedNote.getDateTimeFormatted(NoteActivity.this);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, theWholeNote);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);

                }else if(mLoadedNote==null){
                    String theWholeNote="";


                    theWholeNote =
                            mEtTitle.getText().toString()
                                    + "\n -------------- \n"
                                    + mEtContent.getText().toString();

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, theWholeNote);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }






            }
        });
    }

    public void getUserActionDialog(){

        Dialog getUserAction =new Dialog(NoteActivity.this,R.style.CustomDialogVertical );
        getUserAction.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getUserAction.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getUserAction.setCancelable(true);
        getUserAction.setContentView(R.layout.share_action_dialog);

        final String theWholeNote =
                mEtTitle.getText().toString()
                        + "\n -------------- \n"
                        + mEtContent.getText().toString();

        final Button selectedOnly=(Button)getUserAction.findViewById(R.id.share_action_dialog_selectedTextAction);
        final Button wholeNote=(Button)getUserAction.findViewById(R.id.share_action_dialog_noteAction);

        selectedOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(selectedOnly, NoteActivity.this);
                if(mEtTitle.isFocused()){
                    int startIndex = mEtTitle.getSelectionStart();
                    int endIndex = mEtTitle.getSelectionEnd();
                    String selectedText=mEtTitle.getText().toString().substring(startIndex,endIndex);



                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, selectedText);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);

                } else if(mEtContent.isFocused()){

                    int startIndex = mEtContent.getSelectionStart();
                    int endIndex = mEtContent.getSelectionEnd();
                    String selectedText=mEtContent.getText().toString().substring(startIndex,endIndex);



                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, selectedText);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }

            }
        });

        wholeNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonEffect(wholeNote, NoteActivity.this);

                String theWholeNote="";
                if(mLoadedNote!=null) {
                    theWholeNote =
                            mEtTitle.getText().toString()
                                    + "\n -------------- \n"
                                    + mEtContent.getText().toString()
                                    +"\n \n I wrote this note at:"+mLoadedNote.getDateTime();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, theWholeNote);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);

                }else if(mLoadedNote==null){

                    theWholeNote =
                            mEtTitle.getText().toString()
                                    + "\n -------------- \n"
                                    + mEtContent.getText().toString();

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, theWholeNote);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            }
        });



        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getUserAction.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        getUserAction.show();
    }



    public void buttonEffect(View button , final Context context){

        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimaryOff));
                        v.animate().alpha(.7F).setDuration(500);
                        //v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        //v.getBackground().clearColorFilter();
                        v.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
                        v.animate().alpha(1F).setDuration(500);

                        //v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }


    public String readFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            android.content.ClipDescription description = clipboard.getPrimaryClipDescription();
            android.content.ClipData data = clipboard.getPrimaryClip();
            if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                return String.valueOf(data.getItemAt(0).getText());
        }
        return null;
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
        SimpleDateFormat df = new SimpleDateFormat("MMM_dd_yyyy_h.mm_a");

        calendar = Calendar.getInstance();
        currentDateTimeString = df.format(calendar.getTime());


        mEtContent.setText(mEtContent.getText().toString()+ "\n" +"@"+currentDateTimeString, TextView.BufferType.EDITABLE);
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
public String currentDateTimeString(){
    Calendar calendar;
    String currentDateTimeString;
    SimpleDateFormat df = new SimpleDateFormat("MMM_dd_yyyy_h.mm_a");

    calendar = Calendar.getInstance();
    currentDateTimeString = df.format(calendar.getTime());
return currentDateTimeString;
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

        //get the content of widgets to make a note object
        String title = mEtTitle.getText().toString();
        String content = mEtContent.getText().toString();
        Boolean night = mNightBoolean;
        Boolean alignment = mAlignmentBoolean;

        Boolean pinActiveS = mActivatePIN;
        String pinString = mPINstring;

        String hashtags =mHashtags;



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

                    userContentString=etRContent.getText().toString();

                    if( userContentString.equals("")){
                        reminderBoolean =true;
                        startService(new Intent(NoteActivity.this, AlarmIntentService.class));

                        Rdialog.dismiss();
                    }else {

                       // notifyUserContent = userContentString;
                        Toast.makeText(NoteActivity.this, "All fields must be filled.", Toast.LENGTH_SHORT).show();

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

