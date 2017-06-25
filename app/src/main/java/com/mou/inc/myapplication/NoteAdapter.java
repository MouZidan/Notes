package com.mou.inc.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class NoteAdapter extends ArrayAdapter<Note> {
    Animation scaleUp;
    public static  TextView title = null;
    public static final int WRAP_CONTENT_LENGTH = 50;
    public NoteAdapter(Context context, int resource, List<Note> objects) {
        super(context, resource, objects);
        scaleUp = AnimationUtils.loadAnimation(context, R.anim.translate_left_side);


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Note note = getItem(position);


        if(convertView == null && note.getType()==note.TEXT) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.view_note_item_text, null);
        }else if (convertView == null && note.getType()==note.AUDIO) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.view_note_item_audio, null);
        }



        if(note != null && note.getType()==note.TEXT) {
            title = (TextView) convertView.findViewById(R.id.list_note_title);
            TextView date = (TextView) convertView.findViewById(R.id.list_note_date);
            TextView content = (TextView) convertView.findViewById(R.id.list_note_content_preview);
            ImageView lock = (ImageView)convertView.findViewById(R.id.lock_icon);
            TextView hashtags =(TextView)convertView.findViewById(R.id.list_note_hashtags);


            try {

                if (note.getPin() == false) {
                    lock.setVisibility(View.GONE);
                    content.setText(note.getContent());


                } else {
                    lock.setVisibility(View.VISIBLE);
                    content.setText("PRIVATE");
                    content.setTextColor(Color.RED);


                }
            }catch(NullPointerException e){

            }


            hashtags.setText(note.getHashtags());
            date.setTypeface(null, Typeface.ITALIC);

             title.setText(note.getTitle());
            date.setText(note.getDateTimeFormatted(getContext()));


        }

        if(note != null && note.getType()==note.AUDIO) {
            title = (TextView) convertView.findViewById(R.id.list_note_title);
            TextView date = (TextView) convertView.findViewById(R.id.list_note_date);
            //TextView content = (TextView) convertView.findViewById(R.id.list_note_content_preview);
            ImageView lock = (ImageView)convertView.findViewById(R.id.lock_icon);
            TextView hashtags =(TextView)convertView.findViewById(R.id.list_note_hashtags);


            try {

                if (note.getPin() == false) {
                    lock.setVisibility(View.GONE);


                } else {
                    lock.setVisibility(View.VISIBLE);


                }
            }catch(NullPointerException e){

            }


            hashtags.setText(note.getHashtags());
            date.setTypeface(null, Typeface.ITALIC);

            title.setText(note.getTitle());
            date.setText(note.getDateTimeFormatted(getContext()));


        }

        return convertView;
    }

}