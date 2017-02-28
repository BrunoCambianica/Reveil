package com.example.bruno.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Bruno on 28/02/2017.
 */

public class Alarm_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("THE RECEIVER IS HEREEE", "Whew!");

        //intent service musique
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        //lancer la musique
        context.startService(service_intent);


    }
}
