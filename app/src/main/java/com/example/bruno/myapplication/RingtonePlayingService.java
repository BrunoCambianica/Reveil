package com.example.bruno.myapplication;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;


/**
 * Created by Bruno on 28/02/2017.
 */

public class RingtonePlayingService extends Service{
    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        //fetch la valeur de l'info
        String state = intent.getExtras().getString("extra");
        //fetch le jour
        Integer int_day = intent.getExtras().getInt("day");

        //Log.e("RPS extra ", state);
        //Log.e("RPS jour : ", "" + int_day + "");


        //notifs
        NotificationManager notify_manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        //envoyer un intent dans le main
        Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);

        //récupération ID du pending intent
        PendingIntent pending_intent_MA = PendingIntent.getActivity(this, int_day, intent_main_activity, 0);

        //Parametres des notifications
        Notification notification_popup = new Notification.Builder(this)
                .setContentTitle("L'alarme sonne !")
                .setContentText("Clique ici !")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pending_intent_MA)
                .setAutoCancel(true)
                .build();


        //interpretation de l'intent
        assert state != null;
        switch (state) {
            case "alarm on":
                //Log.e("SWITCH CASE :", "ON");
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                //Log.e("SWITCH CASE :", "off");
                break;
            default:
                startId = 0;
                break;
        }

        //if else
        //musique commence
        if (!this.isRunning && startId == 1) {

            //Log.e("musique off", "debut please");

            this.isRunning = true;
            this.startId = 0;

            // set up notif
            notify_manager.notify(0, notification_popup);
            //debut musique
            media_song = MediaPlayer.create(this, R.raw.test);
            media_song.start();
        }
        //musique arrêt
        else if (this.isRunning && startId == 0) {

            //Log.e("musique on", "fin please");
            //arret musique
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;
        }
        // pas de musique mais appui sur desactiver
        else if (!this.isRunning && startId == 0){
            //Log.e("musique off", "fin please");

            this.isRunning = false;
            this.startId = 0;
        }
        // musique mais appui sur activer
        else if (this.isRunning && startId == 1){

            //Log.e("musique on", "debut please");
            this.isRunning = true;
            this.startId = 1;
        }
        //au cas ou!
        else{

            //Log.e("y'a rien à voir !", " ");
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("destroy methode", "BOOM");

        super.onDestroy();
        this.isRunning = false;

    }
}
