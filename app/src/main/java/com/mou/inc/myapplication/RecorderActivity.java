package com.mou.inc.myapplication;

import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecorderActivity extends AppCompatActivity {

    private static final String TAG ="pp" ;
    Boolean recordingState=false;

    MediaRecorder recorder = new MediaRecorder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Chronometer chronometer=(Chronometer)findViewById(R.id.recorder_chronometer);
        chronometer.setFormat("%s");


        ImageButton recordBtn=(ImageButton)findViewById(R.id.recrod_button);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recordingState==false){
                    recordingState=true;
                    chronometer.setTextColor(ContextCompat.getColor(RecorderActivity.this, R.color.holo_orange_dark));
                    chronometer.start();

                    Toast.makeText(RecorderActivity.this, "State: "+Environment.getExternalStorageState(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(RecorderActivity.this, "ExternalStorageDic...: "+Environment.getExternalStorageDirectory(), Toast.LENGTH_SHORT).show();


                    try {
                        startRecording();
                    } catch (IOException e) {
                        e.printStackTrace();
                     }

                }else if(recordingState==true){
                    recordingState=false;
                    chronometer.setTextColor(ContextCompat.getColor(RecorderActivity.this, R.color.colorPrimaryDark));
                    chronometer.start();


                    recorder.stop();
                    recorder.release();






                }






            }
        });









    }
    public void startRecording() throws IOException{
        long currentTimeMilles=System.currentTimeMillis();




        String path;
        String fileFormat=".3gpp";
        File audioFile = new File(Environment.getExternalStorageDirectory()+ File.separator +currentTimeMilles+ fileFormat);
        audioFile.createNewFile();
        String status = Environment.getExternalStorageState();
        if(status.equals("mounted")){
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        }else path=null; //......need to be fixed later.......

        FileOutputStream fos = new FileOutputStream(audioFile);


        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(path+ File.separator+ System.currentTimeMillis()+ fileFormat);
        recorder.prepare();
        recorder.start();
        fos.close();




    }
    public String getExternalStoragePath() {

        //returns null !!!!!!!!!!!!

        String internalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String[] paths = internalPath.split("/");
        String parentPath = "/";
        for (String s : paths) {
            if (s.trim().length() > 0) {
                parentPath = parentPath.concat(s);
                break;
            }
        }
        File parent = new File(parentPath);
        if (parent.exists()) {
            File[] files = parent.listFiles();
            for (File file : files) {
                String filePath = file.getAbsolutePath();
                Log.d(TAG, filePath);
                if (filePath.equals(internalPath)) {
                    continue;
                } else if (filePath.toLowerCase().contains("sdcard")) {
                    return filePath;
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        if (Environment.isExternalStorageRemovable(file)) {
                            return filePath;
                        }
                    } catch (RuntimeException e) {
                        Log.e(TAG, "RuntimeException: " + e);
                    }
                }
            }

        }
        return null;
    }


}
