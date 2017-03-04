package com.example.bruno.myapplication;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;
    Integer minute;
    Integer hour;
    String days = "";
    Calendar current = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        //instanciation calendrier
        final java.util.Calendar calendar = java.util.Calendar.getInstance();

        //creation de l'intent
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        //initialisation variables
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);
        update_text = (TextView) findViewById(R.id.update_text);

        // initialisation des jours demandés
        final CheckBox monday = (CheckBox) findViewById(R.id.monday);
        final CheckBox tuesday = (CheckBox) findViewById(R.id.tuesday);
        final CheckBox wednesday = (CheckBox) findViewById(R.id.wednesday);
        final CheckBox thursday = (CheckBox) findViewById(R.id.thursday);
        final CheckBox friday = (CheckBox) findViewById(R.id.friday);
        final CheckBox saturday = (CheckBox) findViewById(R.id.saturday);
        final CheckBox sunday = (CheckBox) findViewById(R.id.sunday);

        Button alarm_on = (Button) findViewById(R.id.alarm_on);

        //onClick listener pour activer/desactiver les alarmes
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                //affecter les valeurs au timepicker
                calendar.set(Calendar.HOUR, 24);
                calendar.set(java.util.Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(java.util.Calendar.MINUTE, alarm_timepicker.getMinute());

                //recuperer les valeurs du timepicker
                hour = alarm_timepicker.getHour();
                minute = alarm_timepicker.getMinute();

                // METHODES POUR LANCER LES ALARMES PAR JOUR
                if (monday.isChecked()) {
                    if(!days.contains("lundi")) {
                        days += " lundi";
                    }
                    alarmToPerform(2);
                }
                if (tuesday.isChecked()) {
                    if(!days.contains(" mardi")) {
                        days += " mardi";
                    }
                    alarmToPerform(3);
                }
                if (wednesday.isChecked()) {
                    if(!days.contains(" mercredi")) {
                        days += " mercredi";
                    }
                    alarmToPerform(4);
                }
                if (thursday.isChecked()) {
                    if(!days.contains(" jeudi")) {
                        days += " jeudi";
                    }
                    alarmToPerform(5);
                }
                if (friday.isChecked()) {
                    if(!days.contains(" vendredi")) {
                        days += " vendredi";
                    }
                    alarmToPerform(6);
                }
                if (saturday.isChecked()) {
                    if(!days.contains(" samedi")) {
                        days += " samedi";
                    }
                    alarmToPerform(7);
                }
                if (sunday.isChecked()) {
                    if(!days.contains(" dimanche")) {
                        days += " dimanche";
                    }
                    alarmToPerform(1);
                }
                if (days == "") {
                    Log.e("alarme non répétée :", "day 0");
                    // info extra
                    my_intent.putExtra("extra", "alarm on");
                    my_intent.putExtra("repeat", "off");
                    my_intent.putExtra("day", 0);

                    // pending intent
                    pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // alarm manager
                    alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
                }

                //convertir les valeurs en int
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                //afficher un 0devant les minutes inerieures à 10
                if (minute < 10){
                    minute_string = "0" + String.valueOf(minute);
                }

                if (days != "") {
                    set_alarm_text("Alarme activée à " + hour_string + " : " + minute_string + " le" + days);
                } else{
                    set_alarm_text("Alarme activée à " + hour_string + " : " + minute_string);
                }
                days = "";
            }
        });

        // ALARME OFF / ARRETER -------------------------------------------------------------------------------------

        Button alarm_off = (Button) findViewById(R.id.alarm_off);


        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //text actualisé
                set_alarm_text("Alarme désactivée.");

                //suppression de l'alarme
                //alarm_manager.cancel(pending_intent);

                //avertir de l'action bouton off
                my_intent.putExtra("extra", "alarm off");

                // arreter la sonnerie
                sendBroadcast(my_intent);

/*
                String state = my_intent.getExtras().getString("repeat");
                if (state != null) {
                    switch (state) {
                        case "on":
                            Log.e("STATUT DE L'ALARME ", "REPEATED");
                            //suppression de l'alarme
                            set_alarm_text("Alarme reportée.");

                            //on ne veut pas supprimer l'alarme
                            //alarm_manager.cancel(pending_intent);

                            //demander la réactivation de la sonnerie
                            my_intent.putExtra("extra", "alarm on");
                            //reactiver la sonnerie
                            sendBroadcast(my_intent);
                            break;
                        case "off":
                            Log.e("STATUT DE L'ALARME ", "NORMAL");
                            //text actualisé
                            set_alarm_text("Alarme désactivée.");

                            //suppression de l'alarme
                            alarm_manager.cancel(pending_intent);

                            //avertir de l'action bouton off
                            my_intent.putExtra("extra", "alarm off");

                            // arreter la sonnerie
                            sendBroadcast(my_intent);
                            break;
                        default:
                            Log.e("DEFAULT", "ok");
                            break;
                    }
                }



*/

            }
        });

        Button delete = (Button) findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer compteur = 0;
                Boolean checked = false;

                Log.e("SUPPRESSION :", "Crash incomming");

                // pire boucle au monde
                while (compteur <= 7){
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, compteur, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Log.e("SUPPRESSION WHILE :", "" + pendingIntent + "");
                    alarmManager.cancel(pendingIntent);

                    compteur += 1;
                }
            }
        });
    }
    private void set_alarm_text(String output) {
        update_text.setText(output);
    }

    public void alarmToPerform (int day){

        //instanciation calendrier
        final java.util.Calendar calendar = java.util.Calendar.getInstance();

        //creation de l'intent
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        my_intent.putExtra("extra", "alarm on");
        my_intent.putExtra("repeat", "on");
        my_intent.putExtra("day", day);
        Log.e("jour : ", "" + day + "");
        String test = my_intent.getExtras().getString("extra");
        Log.e("extra creation", test);

        //pending intent
        pending_intent = PendingIntent.getBroadcast(MainActivity.this, day, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Long test1 = calendar.getTimeInMillis();
        Long hhh = current.getTimeInMillis() - calendar.getTimeInMillis();
        Log.e("Mon temps :", " "+ current.DAY_OF_WEEK);
        Log.e("L'autre temps :", " "+ calendar.DAY_OF_WEEK);
        Log.e("difference :", " "+ hhh);

        if( hhh > 0 ){
            Log.e("là on est dans le passé", ":(");

        }


        //alarm manager
        alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  /*24 * 7 * 60 * 60 * 1000 */ (100 * 1000) , pending_intent);
    }


}
