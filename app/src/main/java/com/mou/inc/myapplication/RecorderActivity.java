package com.mou.inc.myapplication;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
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
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class RecorderActivity extends AppCompatActivity {

    private static final String TAG ="p!!!!!!!!!!!!!!!!!p" ;
    private static final String START_REC ="start recording" ;
    private static final String STOP_REC ="stop recording" ;
    private static final int HIGH_QUALITY=320*1024;
    private static final int MID_QUALITY=192*1024;
    private static final int LOW_QUALITY=32*1024;
    private static final String FILE_FORMAT = ".mpeg4";
    public static final int FULL_AUDIO_DURATION = 0;
    public static final int PREVIEW_AUDIO_DURATION = 3;
    private static String FILE_PATH =Environment.getExternalStorageDirectory().getAbsolutePath() ;
    private static  SeekBar audioSeekBar;
    public static int  pstn;
    public static int seekBarProgress;
    String mFileName;

    MediaRecorder mRecorder = new MediaRecorder();
    private Handler mHandler = new Handler();

    public static  MediaPlayer mMediaPlayer;
    private boolean isRecording;
    private boolean audioSavingSucced;




    private EditText mEtAudioContent;
    private EditText mEtAudioTitle;
    private LinearLayout mUserChoose;
    private ImageButton recordBtn;
    private Button playBtn;
    private Chronometer chronometer;
    private ViewGroup mAudioRecorderContainer;
    private ViewGroup mAudioPlayerContainer;
    private ViewGroup mAudioInfoContainer;
    private LinearLayout mAudioRecorderMainLayout;
    private boolean visulaize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recorder);











        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = this.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);


        mAudioRecorderContainer =(ViewGroup) findViewById(R.id.audio_note_recorder_layout);

        mAudioPlayerContainer=(ViewGroup)findViewById(R.id.audio_note_player_layout);

        mAudioInfoContainer=(ViewGroup)findViewById(R.id.audio_note_info_layout);


        chronometer=(Chronometer)findViewById(R.id.recorder_chronometer);
        recordBtn=(ImageButton)findViewById(R.id.record_button);
        playBtn=(Button)findViewById(R.id.play_button);
        audioSeekBar=(SeekBar)findViewById(R.id.audio_seek_bar);

        mMediaPlayer = new MediaPlayer();


        mFileName =System.currentTimeMillis()+"";


        chronometer.setFormat("%s");

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!RecorderActivity.mMediaPlayer.isPlaying()) {
                    playBtn.setBackgroundResource(R.drawable.ic_play_audio);

                    Animation buttonAnimation = AnimationUtils.loadAnimation(RecorderActivity.this, R.anim.rotate_360);
                    v.startAnimation(buttonAnimation);
                    long animationDuration = v.getAnimation().getDuration();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playBtn.setBackgroundResource(R.drawable.ic_pause_audio);


                        }
                    }, animationDuration / 2);


                    audioPlayer(RecorderActivity.this, FILE_PATH + File.separator + ".audionotes", mFileName + FILE_FORMAT, FULL_AUDIO_DURATION, audioSeekBar);
                }else if(RecorderActivity.mMediaPlayer.isPlaying()){
                    playBtn.setBackgroundResource(R.drawable.ic_pause_audio);


                    RecorderActivity.mMediaPlayer.pause();
                    RecorderActivity.pstn=mMediaPlayer.getCurrentPosition();
                    RecorderActivity.seekBarProgress=audioSeekBar.getProgress();

                    Log.d(TAG, "onClick: pstn="+RecorderActivity.pstn+" seekBarProgress="+seekBarProgress+ " seekBarViewProgress="+audioSeekBar.getProgress());

                    Animation buttonAnimation= AnimationUtils.loadAnimation(RecorderActivity.this,R.anim.rotate_360);
                    v.startAnimation(buttonAnimation);


                    long animationDuration=v.getAnimation().getDuration();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playBtn.setBackgroundResource(R.drawable.ic_play_audio);



                        }
                    }, animationDuration/2);

                    RecorderActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            if(mMediaPlayer.isPlaying()) playBtn.setBackgroundResource(R.drawable.ic_pause_audio);
                            else playBtn.setBackgroundResource(R.drawable.ic_play_audio);

                            mHandler.postDelayed(this, 40);
                        }
                    });

                }



            }
        });








        final int l =200;
        recordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        visulaize =true;







                        try {
                            recordAudio(START_REC);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        chronometer.setTextColor(ContextCompat.getColor(RecorderActivity.this, R.color.colorPrimaryDark));

                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();

                        Animation scaleUp=AnimationUtils.loadAnimation(RecorderActivity.this,R.anim.scale_up);

                        recordBtn.startAnimation(scaleUp);






                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        visulaize =false;





                        chronometer.setTextColor(ContextCompat.getColor(RecorderActivity.this, R.color.holo_red_dark));

                        chronometer.stop();
                        Animation scaleDwon=AnimationUtils.loadAnimation(RecorderActivity.this,R.anim.scale_down);
                        recordBtn.setAnimation(scaleDwon);
                        scaleDwon.start();

                        //audioPlayer(FILE_PATH+File.separator+".audionotes", mFileName+FILE_FORMAT, PREVIEW_AUDIO_DURATION);



                        //user stop recording
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isRecording) {
                                    int elpasedMilles = (int) (SystemClock.elapsedRealtime() - chronometer.getBase());
                                    Log.i(TAG, "onTouch: chronometer="+elpasedMilles);

                                    if (elpasedMilles<1000) {
                                        Toast.makeText(RecorderActivity.this, "Hold to Record, Release when you finish.", Toast.LENGTH_LONG).show();



                                    }else{

                                        try {
                                            recordAudio(STOP_REC);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }


                                    }



                                }

                            }
                        },scaleDwon.getDuration());






                    }
                }
                return true;
            }
        });












        mUserChoose =(LinearLayout)findViewById(R.id.user_choose_audio_note);
        mEtAudioTitle =(EditText)findViewById(R.id.et_audio_note_title);
        mEtAudioContent =(EditText)findViewById(R.id.et_audio_note_description);








        //mAudioRecorderMainLayout.setLayoutParams(params);
        final Runnable removeViwes = new Runnable() {
            public void run() {
                mAudioPlayerContainer.removeAllViews();
                mAudioInfoContainer.removeAllViews();
            }
        };
        Runnable getLayoutSize = new Runnable() {
            public void run() {







                mAudioRecorderMainLayout =(LinearLayout)findViewById(R.id.activity_recorder_main_layout);






                final ViewTreeObserver observer = mAudioRecorderMainLayout.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {


                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        // TODO Auto-generated method stub
                        int targetedHight=mAudioPlayerContainer.getHeight()+mAudioInfoContainer.getHeight();

                        Log.i(TAG, "onCreate: targetedHight="+targetedHight);
                        if (targetedHight>0) {

                            final ViewGroup.LayoutParams params = mAudioRecorderMainLayout.getLayoutParams();
                            params.height=targetedHight;
                            params.width= ViewGroup.LayoutParams.MATCH_PARENT;

                            mAudioRecorderMainLayout.setLayoutParams(params);


                            //recorder layout
                            final ViewGroup.LayoutParams paramsl = mAudioRecorderContainer.getLayoutParams();
                            paramsl.height=targetedHight;
                            paramsl.width= ViewGroup.LayoutParams.MATCH_PARENT;

                            mAudioRecorderContainer.setLayoutParams(paramsl);

                            removeViwes.run();


                            mAudioRecorderMainLayout.setVisibility(View.GONE);
                            circularRevealActivity(mAudioRecorderMainLayout);







                            mAudioRecorderMainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        /*mAudioRecorderMainLayout .getViewTreeObserver().removeGlobalOnLayoutListener(
                                this);*/
                    }
                });

            }
        };
        getLayoutSize.run();



    }

    private void circularRevealActivity(ViewGroup linearLayout) {


        int cx = (int) recordBtn.getX()+(recordBtn.getWidth()/2);
        int cy = (int) recordBtn.getY()+(recordBtn.getHeight()/2);

        float finalRadius = Math.max(linearLayout.getWidth(), linearLayout.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(linearLayout, cx, cy, 0, finalRadius);
        circularReveal.setDuration(1000);

        // make the view visible and start the animation
        linearLayout.setVisibility(View.VISIBLE);
        circularReveal.start();
    }


    public void recordAudio(String CONST) throws IOException {





        String status = Environment.getExternalStorageState();

        if (!status.equals("mounted")) {
            Toast.makeText(this, "Please insert your sd card!", Toast.LENGTH_SHORT).show();
        }


        File mAudioFile = (new File(FILE_PATH+File.separator+".audionotes"+File.separator+ mFileName +FILE_FORMAT));

        if (CONST.equals(START_REC) && mAudioFile.length()==0) {
            isRecording =true;


            File audionotes = (new File(FILE_PATH+File.separator+".audionotes"));
            if(!audionotes.isDirectory()) {
                audionotes.mkdirs();

            }





            mRecorder.reset();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setAudioEncodingBitRate(HIGH_QUALITY);
            mRecorder.setAudioSamplingRate(44100);
            mRecorder.setOutputFile(FILE_PATH + File.separator +".audionotes"+File.separator+ mFileName + FILE_FORMAT);
            mRecorder.prepare();
            mRecorder.start();
        } else if (CONST.equals(STOP_REC)) {
            isRecording =false;

            mRecorder.stop();
            mRecorder.release();



            if(mAudioFile.isFile()&&mAudioFile.length() !=0) {

                Toast.makeText(this, "Recording succeed! :: size= "+mAudioFile.length()/1024+" KB's", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(this, "Recording fail! something went wrong.", Toast.LENGTH_SHORT).show();
            validateAndSave();




        }


    }


    public static void audioPlayer(Activity activity, String path, String fileName, int duration, final SeekBar seekBar){
        if(mMediaPlayer==null){mMediaPlayer = new MediaPlayer();}








        //set up MediaPlayer

        if(duration== FULL_AUDIO_DURATION)duration= mMediaPlayer.getDuration();

        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(path + File.separator + fileName);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {


                    Log.d(TAG, "onPrepared: progress="+seekBar.getProgress()+"   Max="+seekBar.getMax());
                    if(pstn!=0 && seekBar.getProgress()!=seekBar.getMax()){


                        mMediaPlayer.start();
                        seekBar.setProgress(pstn);
                        mMediaPlayer.seekTo(pstn);
                        Log.d(TAG, "onPrepared: pstn="+pstn);

                    }else mMediaPlayer.start();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }






            seekBar.setMax(duration);
            Log.d(TAG, "audioSeekerMax: "+duration);



        final Handler mHandler = new Handler();
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                    int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                   //Log.d(TAG, "mCurrentPosition: "+mCurrentPosition + " mSeekBarMax="+seekBar.getMax()+" mSeekBarPosition="+seekBar.getProgress() );

                    seekBar.setProgress(mCurrentPosition);

                mHandler.postDelayed(this, 40);
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mMediaPlayer != null && fromUser){
                    mMediaPlayer.seekTo(progress);
                    pstn=progress;
                    Log.d(TAG, "onProgressChanged: progress="+progress + "pstn="+pstn);
                }
            }
        });

    }





    public void validateAndSave(){

        //animations
        Animator scaleUp = ObjectAnimator.ofPropertyValuesHolder((Object)null
                , PropertyValuesHolder.ofFloat("scaleX", 0, 1)
                , PropertyValuesHolder.ofFloat("scaleY", 0, 1));

        Animator scaleDown = ObjectAnimator.ofPropertyValuesHolder((Object)null
                , PropertyValuesHolder.ofFloat("scaleX", 1, 0)
                , PropertyValuesHolder.ofFloat("scaleY", 1, 0));

        scaleDown.setDuration(300);
        scaleDown.setInterpolator(new OvershootInterpolator());
        scaleUp.setDuration(300);
        scaleUp.setStartDelay(400);
        scaleUp.setInterpolator(new OvershootInterpolator());


        LayoutTransition itemLayoutTransition = new LayoutTransition();
        itemLayoutTransition.setAnimator(LayoutTransition.APPEARING, scaleUp);
        itemLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, scaleDown);






        mAudioRecorderContainer.setLayoutTransition(itemLayoutTransition);
        mAudioPlayerContainer.setLayoutTransition(itemLayoutTransition);
        mAudioInfoContainer.setLayoutTransition(itemLayoutTransition);



        final String path=FILE_PATH+File.separator+".audionotes";
        final String filaName= mFileName +FILE_FORMAT;
        final File f = new File(path + File.separator + filaName);

        if(f.isFile()&&f.length()>0) {

            audioSavingSucced=true;





            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {



                    mAudioRecorderContainer.removeAllViews();

                    final ViewGroup.LayoutParams linearLparams = mAudioRecorderContainer.getLayoutParams();
                    linearLparams.height=ViewGroup.LayoutParams.WRAP_CONTENT;
                    linearLparams.width= ViewGroup.LayoutParams.MATCH_PARENT;

                    mAudioRecorderContainer.setLayoutParams(linearLparams);

                    mAudioPlayerContainer.addView(playBtn,0);






                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            mAudioPlayerContainer.addView(audioSeekBar,1);



                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    RecorderActivity.this.setFinishOnTouchOutside(false);

                                    mAudioInfoContainer.addView(mEtAudioTitle,0);
                                    mAudioInfoContainer.addView(mEtAudioContent,1);
                                    mAudioInfoContainer.addView(mUserChoose,2);




                                    Button saveBtn=(Button)findViewById(R.id.save_audio_note_button);
                                    final Button discardBtn=(Button)findViewById(R.id.discard_audio_note_button);
                                    final EditText audioNoteTitle=(EditText)findViewById(R.id.et_audio_note_title);
                                    final EditText audioNoteDescription=(EditText)findViewById(R.id.et_audio_note_description);



                                    saveBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            String title=audioNoteTitle.getText().toString();
                                            String description=audioNoteDescription.getText().toString();


                                            if (!title.isEmpty()||!title.equals("")||title.length()!=0) {
                                                if(Utilities.saveNote(RecorderActivity.this, new Note(System.currentTimeMillis(), title, description, false, false, false ,"","", false, Note.AUDIO, path, filaName))) { //success!

                                                    //note saved
                                                    Toast.makeText(RecorderActivity.this, "note has been saved"+title, Toast.LENGTH_SHORT).show();
                                                    finish();

                                                } else {
                                                    Toast.makeText(RecorderActivity.this, "can not save the note. make sure you have enough space " +
                                                            "on your device", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(RecorderActivity.this, "Please enter a title to save the note!", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });
                                    discardBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            deleteRecordedAudioFile();
                                            finish();

                                        }
                                    });


                                }
                            },500);


                        }
                    },500);

                }
            },300);
            //Utilities.saveNote(this, new Note(System.currentTimeMillis(), title, mFileName, false, false, false ,"","", false, Note.AUDIO, path, filaName));



        }else{
            audioSavingSucced=false;
            Toast.makeText(RecorderActivity.this, "fail! something went wrong.\n FILE_PATH="+path+"/"+ mFileName, Toast.LENGTH_LONG).show();
        }


    }
    public void deleteRecordedAudioFile(){
        String path=FILE_PATH+File.separator+".audionotes";
        String filaName= mFileName +FILE_FORMAT;
        File f = new File(path + File.separator + filaName);
        mMediaPlayer.stop();

        boolean deleted = f.delete();

        if(!f.exists()) {

            Log.i(TAG, "deleteRecordedAudioFile: file deleted succesfully! path-filename="+path+"-"+filaName);



        }else Log.e(TAG, "deleteRecordedAudioFile: cannot delete file! path-filename="+path+"-"+filaName);



    }



}
