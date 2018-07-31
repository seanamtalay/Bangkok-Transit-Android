package com.example.seamon.bangkoktransit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class TripInfoActivity extends AppCompatActivity {
    private static final String TAG = "TripInfoActivity";
    private String mOriginStation;
    private String mDestinationStation;
    private String mSelectedAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);

        TextView firstStationTextview = findViewById(R.id.first_station_textview);
        TextView secondStationTextview = findViewById(R.id.second_station_textview);
        ImageView arrowStations = findViewById(R.id.arrow_stations);

        arrowStations.setRotation(180);

        //get the names from the intent and change the textViews accordingly
        getIncomingIntent();

        firstStationTextview.setText(mOriginStation);
        secondStationTextview.setText(mDestinationStation);


    }

    //get the names of picked stations from the intent. Then update the globals
    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if(getIntent().hasExtra("first_station") && getIntent().hasExtra("second_station") && getIntent().hasExtra("selected_as")) {
            mSelectedAs = getIntent().getStringExtra("selected_as");

            //assign the station according to the selected option (origin or destination)
            if (mSelectedAs.equals("origin")){
                mOriginStation = getIntent().getStringExtra("first_station");
                mDestinationStation = getIntent().getStringExtra("second_station");

            }
            else if(mSelectedAs.equals("destination")){
                mDestinationStation = getIntent().getStringExtra("first_station");
                mOriginStation = getIntent().getStringExtra("second_station");
            }

            Log.d(TAG, "getIncomingIntent: Found intent extras: " + mOriginStation);
            Log.d(TAG, "getIncomingIntent: Found intent extras: " + mDestinationStation);
        }
    }
}
