package com.mou.inc.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.view_note_item, null);
        }


        Note note = getItem(position);

        if(note != null) {
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

            //correctly show preview of the content (not more than 50 char or more than one line!)

        }

        return convertView;
    }

/**END here >> remember to delete this*/
}