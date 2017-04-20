package com.mou.inc.myapplication;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class represents a Note object
 * Created by saeid on 5/8/2016.
 */
public class Note implements Serializable {

    private long mDateTime; //creation time of the note
    public String mTitle; //title of the note
    private String mContent;

    private Boolean mNight;

    private Boolean mAlignment;

    public Boolean mPin;
    public static Boolean mPinR;
    private String mPinString;

    private String mHashtags;


    public Note(long dateInMillis, String title, String content, Boolean night,Boolean alignment, Boolean pinActiveS, String pinString,String hashtags) {
        mDateTime = dateInMillis;
        mTitle = title;
        mContent = content;
        mNight = night;
        mAlignment = alignment;
        mPin = pinActiveS;
        mPinString = pinString;

        mHashtags = hashtags;
        mPinR =mPin;

    }

    public void setDateTime(long dateTime) {
        mDateTime = dateTime;
    }

    public void setTitleR(String title) {
        mTitle = title;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public void setPinBoolean(Boolean pin) {
        mPin = pin;
    }
    public void setPinString(String pinString) {
        mPinString = pinString;
    }
    public void setNight(Boolean night) {
        mNight = night;
    }
    public void setAlignment(Boolean alignment) {
        mAlignment = alignment;
    }

    public  void setHashtags(String hashtags){
        mHashtags=hashtags;
    }


    public long getDateTime() {
        return mDateTime;
    }

    /**
     * Get date time as a formatted string
     * @param context The context is used to convert the string to user set locale
     * @return String containing the date and time of the creation of the note
     */
    public String getDateTimeFormatted(Context context) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm"
                , context.getResources().getConfiguration().locale);
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(new Date(mDateTime));
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }
    public Boolean getNight() {
        return mNight;
    }

    public Boolean getAlignment() {
        return mAlignment;
    }
    public  Boolean getPin() {

        return mPin;
    }
    public static  Boolean getPinR() {


        return mPinR;
    }
    public String getPinString() {
        return mPinString;
    }


    public String getHashtags() {
        return mHashtags;
    }



}
