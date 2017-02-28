package com.example.bruno.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    DatePicker alarm_datepicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        //initialisation variables
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);
        update_text = (TextView) findViewById(R.id.update_text);

        //instanciation calendrier
        final Calendar calendar = Calendar.getInstance();


        //initialisation des boutons
        Button alarm_on = (Button) findViewById(R.id.alarm_on);
        Button alarm_off = (Button) findViewById(R.id.alarm_off);

        //creation de l'intent
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);


        //onClick listener pour activer/desactiver les alarmes
        alarm_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //affecter les valeurs au timepicker
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                //recuperer les valeurs du timepicker
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                //convertir les valeurs en int
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                if (minute < 10){
                    minute_string = "0" + String.valueOf(minute);
                }

                set_alarm_text("Alarme activée le  ? à " + hour_string + " : " + minute_string);

                //pending intent
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                //alarm manager
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
            }
        });

        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_alarm_text("Alarme désactivée.");

                alarm_manager.cancel(pending_intent);
            }
        });
    }
    public void set_alarm_text(String output) {
        update_text.setText(output);
    }


}
