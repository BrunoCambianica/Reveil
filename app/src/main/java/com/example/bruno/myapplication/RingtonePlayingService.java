package com.example.bruno.myapplication;

import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Bruno on 28/02/2017.
 */

public class RingtonePlayingService extends Service{
    MediaPlayer media_song;
    private int startId;
    private boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        //fetch la valeur de l'info
        String state = intent.getExtras().getString("extra");

        Log.e("Ringtone state extra is", state);
        assert state != null;
        switch (state) {
            case "alarm_on":
                startId = 1;
                break;
            case "alarm_off":
                startId = 0;
                Log.e("START ID IS", state);
                break;
            default:
                startId = 0;
                break;
        }

        //if else
        //musique commence
        if (!this.isRunning && startId == 1){
            //debut musique
            media_song = MediaPlayer.create(this, R.raw.test);
            media_song.start();

            Log.e("musique off", "debut please");

            this.isRunning = true;
            this.startId = 0;
        }
        //musique arrêt
        else if (this.isRunning && startId == 0){

            Log.e("musique on", "fin please");
            //arret musique
            media_song.stop();
            media_song.reset();

            this.isRunning = false;
            this.startId = 0;
        }
        // pas de musique mais appui sur desactiver
        else if (!this.isRunning && startId == 0){

            Log.e("musique off", "fin please");

            this.isRunning = false;
            this.startId = 0;
        }
        // musique mais appui sur activer
        else if (this.isRunning && startId == 1){

            Log.e("musique on", "debut please");
            this.isRunning = true;
            this.startId = 1;
        }
        //au cas ou!
        else{

        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Toast.makeText(this, "methode destroy", Toast.LENGTH_SHORT).show();
    }
}
