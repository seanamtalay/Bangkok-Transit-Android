package com.example.seamon.bangkoktransit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class StationInfoActivity extends AppCompatActivity {
    private static final String TAG = "StationInfoActivity";
    private String current_station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView stationInfoName = findViewById(R.id.station_info_name);
        TextView stationInfoLine = findViewById(R.id.station_info_line);
        TextView stationInfoPlatform = findViewById(R.id.station_info_platform);
        TextView stationInfoExit = findViewById(R.id.station_info_exit);

        current_station = getIncomingIntent();

        //Get the station code
        String[] splitedName = current_station.split("\\s+");
        String stationCode = splitedName[0];

        //get the station info based on the stationCode
        int resId = this.getResources().getIdentifier(stationCode,"array",this.getPackageName()); // might cause a bug (return 0)
        String[] stringArray = getResources().getStringArray(resId);
        String receivedName = stringArray[0];
        String receivedLine = stringArray[1];
        String receivedPlatform = stringArray[2];
        String receivedExit = stringArray[3];

        //change them accordingly
        stationInfoName.setText(receivedName);
        stationInfoLine.setText(receivedLine);
        stationInfoPlatform.setText(receivedPlatform);
        stationInfoExit.setText(receivedExit);

        toolbar.setTitle(current_station);


        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private String getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if(getIntent().hasExtra("station_name")){
            Log.d(TAG, "getIncomingIntent: Found intent extras.");

            String trainName = getIntent().getStringExtra("station_name");
            return trainName;

        }
        else{
            return "no_intent_received";
        }
    }
}
