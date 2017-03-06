package com.example.bruno.myapplication;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.example.bruno.myapplication.Song;
import com.example.bruno.myapplication.Image;

/**
 * Created by Bruno on 28/02/2017.
 */

public class RingtonePlayingService extends Service{
    //MediaPlayer media_song;
    MediaPlayer mediaPlayer = new MediaPlayer();
    int startId;
    boolean isRunning;

    private final String SD_PATH = new String("/sdcard/");

    private List<Song> songs = new ArrayList<Song>();

    private List<Image> images = new ArrayList<Image>();

    private void updateSongs() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {

            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int path=  cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

            do {
                //long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                String thispath = cursor.getString(path);
                Log.v("var:", thisTitle);
                Log.v("path", thispath);
                Song that = new Song();
                that.addSong(thispath, thisTitle);
                songs.add(that);

            } while (cursor.moveToNext());
        }
    }
    private void updateImages() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {

            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Images.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Images.Media._ID);
            int path=  cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            do {
                //long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                String thispath = cursor.getString(path);
                Log.v("var:", thisTitle);
                Log.v("path", thispath);
                Image that = new Image();
                that.addImage(thispath, thisTitle);
                images.add(that);

            } while (cursor.moveToNext());
        }
    }
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

        Log.e("RPS extra ", state);
        Log.e("RPS jour : ", "" + int_day + "");


        //notifs
        NotificationManager notify_manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        //envoyer un intent dans le main
        Intent intent_main_activity = new Intent(this.getApplicationContext(), Splash.class);

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

        //WindowManager windowManager=(WindowManager)getSystemService(WINDOW_SERVICE);
        //LayoutInflater inflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        //RelativeLayout layout=(RelativeLayout) inflater.inflate(R.layout.splash);
        //WindowManager.LayoutParams params =  new WindowManager.LayoutParams();
        //windowManager.addView(layout,params);

        //interpretation de l'intent
        assert state != null;
        switch (state) {
            case "alarm on":
                Log.e("SWITCH CASE :", "ON");
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                Log.e("SWITCH CASE :", "off");
                break;
            default:
                startId = 0;
                break;
        }

        //if else
        //musique commence
        if (!this.isRunning && startId == 1) {

            Log.e("musique off", "debut please");

            this.isRunning = true;
            this.startId = 0;



            // set up notif
            notify_manager.notify(0, notification_popup);
            //debut musique

            updateSongs();
            Collections.shuffle(songs);

            Song thisSong = songs.get(0);
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(thisSong.pathSong);
            } catch (IOException e) {

                e.printStackTrace();
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();


        }
        //musique arrêt
        else if (this.isRunning && startId == 0) {

            Log.e("musique on", "fin please");
            //arret musique
            mediaPlayer.stop();
            mediaPlayer.reset();

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

            Log.e("y'a rien à voir !", " ");
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
