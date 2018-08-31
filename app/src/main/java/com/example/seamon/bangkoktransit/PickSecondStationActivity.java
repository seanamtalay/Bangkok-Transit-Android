package com.example.seamon.bangkoktransit;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class PickSecondStationActivity extends AppCompatActivity {

    private static final String TAG = "PickSecondStationAct";

    //vars
    private ArrayList<String> mStationNames = new ArrayList<>();
    private String mFirstStation;
    private String mSelectedAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_second_station);
        Log.d(TAG, "onCreate: started.");

        getIncomingIntent();

        //Snack bar asking user to pick the second station
        String originOrDestinationText = "";
        if(mSelectedAs.equals("origin")){
            originOrDestinationText = getResources().getString(R.string.select_destination_station);
        }
        else if(mSelectedAs.equals("destination")){
            originOrDestinationText = getResources().getString(R.string.select_origin_station);
        }
        Snackbar selectStationSnackbar = Snackbar.make(findViewById(android.R.id.content), originOrDestinationText, 7000);
        selectStationSnackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        selectStationSnackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Snack bar asking user to pick the second station
        String originOrDestinationText = "";
        if(mSelectedAs.equals("origin")){
            originOrDestinationText = getResources().getString(R.string.select_destination_station);
        }
        else if(mSelectedAs.equals("destination")){
            originOrDestinationText = getResources().getString(R.string.select_origin_station);
        }
        Snackbar selectStationSnackbar = Snackbar.make(findViewById(android.R.id.content), originOrDestinationText, 7000);
        selectStationSnackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        selectStationSnackbar.show();
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if(getIntent().hasExtra("second_station_name") && getIntent().hasExtra("first_station_name") && getIntent().hasExtra("selected_as")){
            String trainName = getIntent().getStringExtra("second_station_name");
            Log.d(TAG, "getIncomingIntent: Found intent extras: " + trainName);
            setStations(trainName);

            mFirstStation = getIntent().getStringExtra("first_station_name");
            Log.d(TAG, "getIncomingIntent: Found intent extras: " + mFirstStation);

            mSelectedAs = getIntent().getStringExtra("selected_as");

        }


        initRecyclerView(mFirstStation);
    }

    private void setStations(String trainName){
        Log.d(TAG, "setStations: setting the station according to the train type");
        // pull the station based on train here

        //match station array with the extra from previous activity's intent
        if(trainName.equals(getResources().getString(R.string.ARL))){
            mStationNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.stations_ARL_array)));
        }
        else if(trainName.equals(getResources().getString(R.string.BTS_Sukhumvit))){
            mStationNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.stations_BTS_Sukhumvit_array)));
        }
        else if(trainName.equals(getResources().getString(R.string.BTS_Silom))){
            mStationNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.stations_BTS_Silom_array)));
        }
        else if(trainName.equals(getResources().getString(R.string.MRT_blue))){
            mStationNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.stations_MRT_Blue_array)));
        }
        else if(trainName.equals(getResources().getString(R.string.MRT_purple))){
            mStationNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.stations_MRT_Purple_array)));
        }
    }



    private void initRecyclerView(String first_station_name){

        RecyclerView recyclerView = findViewById(R.id.SecondStationRecyclerView);
        RecyclerViewAdapterSecondStations recyclerViewAdapterSecondStations = new RecyclerViewAdapterSecondStations(this, mStationNames, first_station_name, mSelectedAs);
        recyclerView.setAdapter(recyclerViewAdapterSecondStations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void finish() {
        super.finish();
        //exit transition animation
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
