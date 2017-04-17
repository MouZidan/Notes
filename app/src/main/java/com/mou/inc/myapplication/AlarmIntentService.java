package com.mou.inc.myapplication;

/**
 * Created by mr-mou on 15/04/17.
 */
import android.app.IntentService;

import android.content.Intent;


public class AlarmIntentService extends IntentService {

    public AlarmIntentService() {
        this(AlarmIntentService.class.getName());
    }


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AlarmIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AlarmService i =new AlarmService();

            i.showToast("Starting IntentService",getApplicationContext());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            i.showToast("Finishing IntentService",getApplicationContext());

    }
}