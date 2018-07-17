package com.example.seamon.bangkoktransit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class PickStationActivity extends AppCompatActivity {

    private static final String TAG = "PickStationActivity";

    //vars
    private ArrayList<String> mStationNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_station);
        Log.d(TAG, "onCreate: started.");

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if(getIntent().hasExtra("train_name")){
            Log.d(TAG, "getIncomingIntent: Found intent extras.");

            String trainName = getIntent().getStringExtra("train_name");
            setStations(trainName);
            initRecyclerView();
        }
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



    private void initRecyclerView(){

        RecyclerView recyclerView = findViewById(R.id.StationRecyclerView);
        RecylerViewAdapterStations recylerViewAdapterStations = new RecylerViewAdapterStations(this, mStationNames);
        recyclerView.setAdapter(recylerViewAdapterStations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
