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

        //fetch infoirmation
        String get_your_string = intent.getExtras().getString("extra");

        Log.e("THE KEY? ", get_your_string);

        //intent service musique
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        //passe l'info du main à ringtone
        service_intent.putExtra("extra", get_your_string);

        //lancer la musique
        context.startService(service_intent);



    }
}
