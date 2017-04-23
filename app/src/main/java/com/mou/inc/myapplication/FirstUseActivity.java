package com.mou.inc.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FirstUseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_use);


        final Button button = (Button) findViewById(R.id.goBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(FirstUseActivity.this, MainActivity.class));
            }
        });












        String title="\uD83D\uDE04Hi!";
        String content="You can insert date and time to your note like this ' .cDAT' through the float button" +
                " or you can do this with the realtime command detection when you simply type " +
                "[.cDAT] (without brackets) after a space.  .";


        Utilities.saveNote(this, new Note(System.currentTimeMillis(), title, content, false,true , false ,"","your note app"));

    }


}
