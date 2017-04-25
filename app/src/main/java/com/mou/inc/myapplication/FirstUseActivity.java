package com.mou.inc.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class FirstUseActivity extends AppCompatActivity {
    private ViewFlipper vf;
    private float lastX;
    private float lastY;
    int screenHight;
    int screenWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_use);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;



        final Button button = (Button) findViewById(R.id.startBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(FirstUseActivity.this, MainActivity.class));
            }
        });





        vf = (ViewFlipper) findViewById(R.id.view_flipper);


        final float[] dX = new float[1];
        final float[] dY = new float[1];
        /*button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN: {
                        lastX = event.getX();
                        lastY = event.getY();
                        dX[0] = v.getX() - event.getRawX();
                        dY[0] = v.getY() - event.getRawY();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE:

                        final Button button = (Button) findViewById(R.id.goBtn);

                        button.setX(event.getRawX() + dX[0]);
                            button.setY(event.getRawY() + dY[0]);

                        button.setText((lastX-event.getX())+"");

                        if(button.getX()<=0)button.setX(0);
                        if(button.getX()>=screenWidth-button.getWidth())button.setX(screenWidth- button.getWidth());
                        if(button.getY()<=0)button.setY(0);
                        if(button.getY()>=screenHight-button.getHeight())button.setY(screenHight-button.getHeight());
                        button.setText((button.getX()+""));

                        if(button.getX()>screenWidth/2)button.setX(screenWidth-button.getWidth());



                }

                return true;
            }
        });*/



        String title="\uD83D\uDE04Hi!";
        String content="You can insert date and time to your note like this \"@Apr_24_2017_11.55_PM\" " +
                "through the float button or you can do this with " +
                "the runtime command detection when you simply type [.cDAT] (without brackets) after a space," +
                " also all commands can be customized from Settings.";


        Utilities.saveNote(this, new Note(System.currentTimeMillis(), title, content, false, false, false, "", "your note app"));

    }


public void currentViewDots(){



}


    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {

            case MotionEvent.ACTION_DOWN: {
                lastX = touchevent.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final View currentView = vf.getCurrentView();

                if (vf.getDisplayedChild()==0 &&lastX-touchevent.getX()>0){
                    currentView.layout((int)(touchevent.getX() - lastX)*2,
                        currentView.getTop(), currentView.getRight(),
                        currentView.getBottom());}


                break;

           case MotionEvent.ACTION_UP: {
                float currentX = touchevent.getX();
                if (lastX < currentX) {
                    if (vf.getDisplayedChild() == 0)
                        break;
                    vf.setInAnimation(this, R.anim.in_from_left);
                    vf.setOutAnimation(this, R.anim.out_to_right);
                    vf.showNext();
                }
                if (lastX > currentX) {
                    if (vf.getDisplayedChild() == 1)
                        break;
                    vf.setInAnimation(this, R.anim.in_from_right);
                    vf.setOutAnimation(this, R.anim.out_to_left);
                    vf.showPrevious();
                }
               ImageView dot1=(ImageView)findViewById(R.id.dot_1);
               ImageView dot2=(ImageView)findViewById(R.id.dot_2);

               if(vf.getDisplayedChild()==0){
                   dot1.animate().scaleX(2); dot2.animate().scaleX(1);
                   dot1.animate().scaleY(2); dot2.animate().scaleY(1);
               }
               if(vf.getDisplayedChild()==1){
                   dot2.animate().scaleX(2); dot1.animate().scaleX(1);
                   dot2.animate().scaleY(2); dot1.animate().scaleY(1);
                   Button button1 = (Button) findViewById(R.id.startBtn);
                   button1.setAlpha(0);
                   button1.animate().alpha(1).setDuration(500);
                   NoteActivity.buttonEffect(button1, FirstUseActivity.this);

               }
                break;
            }
        }
        return false;
    }



}
