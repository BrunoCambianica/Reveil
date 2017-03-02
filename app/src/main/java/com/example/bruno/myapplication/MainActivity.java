package com.example.bruno.myapplication;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    String reference;
    String days = "";

    Boolean lundi;
    Boolean mardi;
    Boolean mercredi;
    Boolean jeudi;
    Boolean vendredi;
    Boolean samedi;
    Boolean dimanche;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        // initialisation des jours demandés
        final CheckBox monday = (CheckBox) findViewById(R.id.monday);
        final CheckBox tuesday = (CheckBox) findViewById(R.id.tuesday);
        final CheckBox wednesday = (CheckBox) findViewById(R.id.wednesday);
        final CheckBox thursday = (CheckBox) findViewById(R.id.thursday);
        final CheckBox friday = (CheckBox) findViewById(R.id.friday);
        final CheckBox saturday = (CheckBox) findViewById(R.id.saturday);
        final CheckBox sunday = (CheckBox) findViewById(R.id.sunday);

        //initialisation variables
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);
        update_text = (TextView) findViewById(R.id.update_text);

        //instanciation calendrier
        final java.util.Calendar calendar = java.util.Calendar.getInstance();

        //creation de l'intention
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        //initialisation des boutons
        Button alarm_on = (Button) findViewById(R.id.alarm_on);

        //onClick listener pour activer/desactiver les alarmes
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

            //    int days = Calendar.SUNDAY + (7 - calendar.get(Calendar.DAY_OF_WEEK)); REPETE CHAQUE SEMAINE?
                //affecter les valeurs au timepicker
                calendar.set(Calendar.HOUR, 24);
                calendar.set(java.util.Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(java.util.Calendar.MINUTE, alarm_timepicker.getMinute());


                //VERIFICATION JOUR COCHES
                if (monday.isChecked()){
                    reference += "2";
                    if(!days.contains("lundi")){
                        days += "lundi";
                    }
                }
                if (tuesday.isChecked()){
                    reference += "3";
                    if(!days.contains(" mardi")) {
                        days += " mardi";
                    }
                }
                if (wednesday.isChecked()){
                    reference += "4";
                    if(!days.contains(" mercredi")) {
                        days += " mercredi";
                    }
                }
                if (thursday.isChecked()){
                    reference += "5";
                    if(!days.contains(" jeudi")) {
                        days += " jeudi";
                    }
                }
                if (friday.isChecked()){
                    reference += "6";
                    if(!days.contains(" vendredi")) {
                        days += " vendredi";
                    }
                }
                if (saturday.isChecked()){
                    reference += "7";
                    if(!days.contains(" samedi")) {
                        days += " samedi";
                    }
                }
                if (sunday.isChecked()){
                    reference += "1";
                    if(!days.contains(" dimanche")) {
                        days += " dimanche";
                    }
                }

                //recuperer les valeurs du timepicker
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                //convertir les valeurs en int
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                //afficher un 0devant les minutes inerieures à 10
                if (minute < 10){
                    minute_string = "0" + String.valueOf(minute);
                }

                if (days != "") {
                    set_alarm_text("Alarme activée à " + hour_string + " : " + minute_string + " le " + days);
                } else{
                    set_alarm_text("Alarme activée à " + hour_string + " : " + minute_string);
                }
                days = "";

                my_intent.putExtra("extra", "alarm_on");

                //pending intent
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0,
                        my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                //alarm manager
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pending_intent);

            }
        });

        Button alarm_off = (Button) findViewById(R.id.alarm_off);

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_alarm_text("Alarme désactivée.");

                //text actualisé
                alarm_manager.cancel(pending_intent);

                //avertir de l'action bouton off
                my_intent.putExtra("extra", "alarm_off");

                // arreter la sonnerie
                sendBroadcast(my_intent);
            }
        });
    }
    private void set_alarm_text(String output) {
        update_text.setText(output);
    }


}
