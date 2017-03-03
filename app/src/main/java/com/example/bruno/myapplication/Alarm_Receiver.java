package com.example.bruno.myapplication;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Bruno on 28/02/2017.
 */

public class Alarm_Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

/*
        //Variable contenant jour de la semaine

        Date d = new Date();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String today = null;
        if (day == 2) {
            today = "Monday";
        } else if (day == 3) {
            today = "Tuesday";
        } else if (day == 4) {
            today = "Wednesday";
        } else if (day == 5) {
            today = "Thursday";
        } else if (day == 6) {
            today = "Friday";
        } else if (day == 7) {
            today = "Saturday";
        } else if (day == 1) {
            today = "Sunday";
        }
*/
        //fetch infoirmation
        String get_your_string = intent.getExtras().getString("extra");

        Log.e("extra alarm receiver? ", get_your_string);

        //intent service musique
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        //passe l'info du main à ringtone
        service_intent.putExtra("extra", get_your_string);

        //lancer la musique
        context.startService(service_intent);



    }
}
