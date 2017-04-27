package com.mou.inc.myapplication;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class MouMethods {



    public static void dragableView(View v, Activity activity) {
        final float[] lastX = new float[1];
        final float[] lastY = new float[1];
        final int[] screenHight = new int[1];
        final int[] screenWidth = new int[1];
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final float[] dX = new float[1];
        final float[] dY = new float[1];
        screenHight[0] = displayMetrics.heightPixels;
        screenWidth[0] = displayMetrics.widthPixels;

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {


                    case MotionEvent.ACTION_DOWN: {
                        lastX[0] = event.getX();
                        lastY[0] = event.getY();
                        dX[0] = v.getX() - event.getRawX();
                        dY[0] = v.getY() - event.getRawY();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE:


                        v.setX(event.getRawX() + dX[0]);
                        v.setY(event.getRawY() + dY[0]);


                        if (v.getX() <= 0) v.setX(0);
                        if (v.getX() >= screenWidth[0] - v.getWidth())
                            v.setX(screenWidth[0] - v.getWidth());
                        if (v.getY() <= 0) v.setY(0);
                        if (v.getY() >= screenHight[0] - v.getHeight())
                            v.setY(screenHight[0] - v.getHeight());

                        if (v.getX() > screenWidth[0] / 2) v.setX(screenWidth[0] - v.getWidth());


                }

                return true;
            }
        });
    }

    public static void dragableDialog(View v, Activity activity) {
        final float[] lastX = new float[1];
        final float[] lastY = new float[1];
        final int[] screenHight = new int[1];
        final int[] screenWidth = new int[1];
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final float[] dX = new float[1];
        final float[] dY = new float[1];
        screenHight[0] = displayMetrics.heightPixels;
        screenWidth[0] = displayMetrics.widthPixels;

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {


                    case MotionEvent.ACTION_DOWN: {
                        lastX[0] = event.getX();
                        lastY[0] = event.getY();
                        dX[0] = v.getX() - event.getRawX();
                        dY[0] = v.getY() - event.getRawY();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE:


                        v.setX(event.getRawX() + dX[0]);
                        v.setY(event.getRawY() + dY[0]);


                        if (v.getX() <= 0) v.setX(0);
                        if (v.getX() >= screenWidth[0] - v.getWidth())
                            v.setX(screenWidth[0] - v.getWidth());
                        if (v.getY() <= 0) v.setY(0);
                        if (v.getY() >= screenHight[0] - v.getHeight())
                            v.setY(screenHight[0] - v.getHeight());

                        if (v.getX() > screenWidth[0] / 2) v.setX(screenWidth[0] - v.getWidth());


                }

                return true;
            }
        });
    }

}
