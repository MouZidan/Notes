package com.mou.inc.myapplication;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.mou.inc.myapplication.RecorderActivity.mMediaPlayer;
import static com.mou.inc.myapplication.RecorderActivity.seekBarProgress;

/**
 * Created by mr-mou on 27/06/17.
 */

public class PlayAudioActivity extends AppCompatActivity {
    private static final String TAG ="PlayAudioActivity" ;
    public Activity activity;
    public Button play;
    private Handler mHandler = new Handler();
    private SeekBar seekBar;
    private String mAudioFilePath;
    private String mAudioFileName;
    private String mAudioNoteTitleString;
    private String mAudioNoteDescriptionString;
    private TextView mTvAudioNoteTitle;
    private TextView mTvAudioNoteDescription;
    private long noteDate;
    private LinearLayout mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.play_audio_dialog);
        activity=this;



        final Object note=MainActivity.clickedNote;
        mAudioNoteTitleString =((Note) note).getTitle();
        mAudioNoteDescriptionString=((Note) note).getContent();
        mAudioFilePath=((Note) note).getAudioPath();
        mAudioFileName=((Note) note).getAudioFileName();
        noteDate=((Note) note).getDateTime();





        mLayout=(LinearLayout)findViewById(R.id.main_play_dialog_layout);


        mTvAudioNoteTitle=(TextView)findViewById(R.id.title_audio_dialog);
        mTvAudioNoteDescription=(TextView)findViewById(R.id.description_audio_dialog);


        mTvAudioNoteTitle.setText(mAudioNoteTitleString);

        if(!mAudioNoteDescriptionString.isEmpty()){

            mTvAudioNoteDescription.setText(mAudioNoteDescriptionString);
        }else{
            String htmlString="<u>click to add content...</u>";
            mTvAudioNoteDescription.setText(Html.fromHtml(htmlString));

            Animator scaleUp = ObjectAnimator.ofPropertyValuesHolder((Object)null
                    , PropertyValuesHolder.ofFloat("scaleX", 0, 1)
                    , PropertyValuesHolder.ofFloat("scaleY", 0, 1));

            Animator scaleDown = ObjectAnimator.ofPropertyValuesHolder((Object)null
                    , PropertyValuesHolder.ofFloat("scaleX", 1, 0)
                    , PropertyValuesHolder.ofFloat("scaleY", 1, 0));

            scaleDown.setDuration(300);
            scaleDown.setStartDelay(300);
            scaleDown.setInterpolator(new OvershootInterpolator());
            scaleUp.setDuration(300);
            scaleUp.setStartDelay(200);
            scaleUp.setInterpolator(new OvershootInterpolator());


            final LayoutTransition itemLayoutTransition = new LayoutTransition();
            itemLayoutTransition.setAnimator(LayoutTransition.APPEARING, scaleUp);
            itemLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, scaleDown);


            mTvAudioNoteDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    mLayout.setLayoutTransition(itemLayoutTransition);



                    mLayout.removeView(mTvAudioNoteDescription);
                    final EditText et = new EditText(activity);
                    et.setHint("Enter a description...");
                    et.setMinLines(4);
                    et.setTextSize(16);
                    et.setMaxLines(3);
                    mLayout.addView(et);


                    final Button saveBtn = new Button(activity);
                    saveBtn.setText("SAVE");
                    saveBtn.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);


                    TypedValue typedValue = new TypedValue();
                    activity.getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, typedValue, true);
                    saveBtn.setBackgroundResource(typedValue.resourceId);




                    mLayout
                            .addView(saveBtn);

                    saveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String newDesc =et.getText().toString();

                            if(!newDesc.isEmpty()){


                                if(Utilities.saveNote(activity, new Note( noteDate, mAudioNoteTitleString, newDesc
                                        , false, false, false ,"","", false, Note.AUDIO, mAudioFilePath, mAudioFileName))) { //success!

                                    mLayout.removeView(et);
                                    mLayout.removeView(saveBtn);
                                    mLayout.addView(mTvAudioNoteDescription);
                                    mTvAudioNoteDescription.setText(newDesc);


                                    Toast.makeText(activity, "All changes has been saved!", Toast.LENGTH_SHORT).show();
                                }

                            }else Toast.makeText(activity, "Please enter a description!", Toast.LENGTH_SHORT).show();




                        }
                    });


                    //removing click listners
                    mTvAudioNoteDescription.setOnClickListener(null);
                }
            });




        }



        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = activity.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);


        mMediaPlayer = new MediaPlayer();
        seekBar =(SeekBar)findViewById(R.id.audio_seekbar_dialog);
        play = (Button) findViewById(R.id.play_button_dialog);











        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RecorderActivity.mMediaPlayer.isPlaying()) {
                    play.setBackgroundResource(R.drawable.ic_play_audio);

                    Animation buttonAnimation= AnimationUtils.loadAnimation(activity,R.anim.rotate_360);
                    v.startAnimation(buttonAnimation);
                    long animationDuration=v.getAnimation().getDuration();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            play.setBackgroundResource(R.drawable.ic_pause_audio);



                        }
                    }, animationDuration/2);








                    RecorderActivity.audioPlayer(activity, mAudioFilePath,mAudioFileName,RecorderActivity.FULL_AUDIO_DURATION,seekBar);


                }else if(RecorderActivity.mMediaPlayer.isPlaying()){
                    play.setBackgroundResource(R.drawable.ic_pause_audio);


                    RecorderActivity.mMediaPlayer.pause();
                    RecorderActivity.pstn=mMediaPlayer.getCurrentPosition();
                    RecorderActivity.seekBarProgress=seekBar.getProgress();

                    Log.d(TAG, "onClick: pstn="+RecorderActivity.pstn+" seekBarProgress="+seekBarProgress+ " seekBarViewProgress="+seekBar.getProgress());

                    Animation buttonAnimation= AnimationUtils.loadAnimation(activity,R.anim.rotate_360);
                    v.startAnimation(buttonAnimation);


                    long animationDuration=v.getAnimation().getDuration();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            play.setBackgroundResource(R.drawable.ic_play_audio);



                        }
                    }, animationDuration/2);

                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            if(mMediaPlayer.isPlaying()) play.setBackgroundResource(R.drawable.ic_pause_audio);
                            else play.setBackgroundResource(R.drawable.ic_play_audio);

                            mHandler.postDelayed(this, 40);
                        }
                    });

                }

            }
        });




        play.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                circularRevealActivity(mLayout,play);




            }
        });

    }
    private void circularRevealActivity(ViewGroup linearLayout,View view) {


        int cx = (int) linearLayout.getX()+(view.getWidth()/2);
        int cy = (int) linearLayout.getY()+(view.getHeight()/2);
        linearLayout.setVisibility(View.GONE);


        float finalRadius = Math.max(linearLayout.getWidth(), linearLayout.getHeight());

        Log.i(TAG, "circularRevealActivity: finalReadius="+finalRadius);

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(linearLayout, cx, cy, 0, finalRadius);
        circularReveal.setDuration(1000);

        // make the view visible and start the animation
        linearLayout.setVisibility(View.VISIBLE);
        circularReveal.start();
    }






}