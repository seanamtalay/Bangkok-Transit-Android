package com.example.seamon.bangkoktransit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seamon.bangkoktransit.R;
import com.example.seamon.bangkoktransit.Adapter.RecyclerViewAdapterStations;

import java.util.ArrayList;
import java.util.Arrays;

public class PickStationActivity extends AppCompatActivity {

    private static final String TAG = "PickStationActivity";

    //vars
    private ArrayList<String> mStationNames = new ArrayList<>();
    private TextView trainNameHeader;
    private ImageView trainLogoHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_station);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Pick Station");

        trainLogoHeader = findViewById(R.id.pick_station_line_head_image);
        trainNameHeader = findViewById(R.id.pick_station_line_head_text);

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
            trainNameHeader.setText(trainName);
            trainLogoHeader.setImageResource(R.drawable.arl_logo);
        }
        else if(trainName.equals(getResources().getString(R.string.BTS_Sukhumvit))){
            mStationNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.stations_BTS_Sukhumvit_array)));
            trainNameHeader.setText(trainName);
            trainLogoHeader.setImageResource(R.drawable.bts_sukhumvit_logo);
        }
        else if(trainName.equals(getResources().getString(R.string.BTS_Silom))){
            mStationNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.stations_BTS_Silom_array)));
            trainNameHeader.setText(trainName);
            trainLogoHeader.setImageResource(R.drawable.bts_silom_logo);
        }
        else if(trainName.equals(getResources().getString(R.string.MRT_blue))){
            mStationNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.stations_MRT_Blue_array)));
            trainNameHeader.setText(trainName);
            trainLogoHeader.setImageResource(R.drawable.mrt_blue_logo);
        }
        else if(trainName.equals(getResources().getString(R.string.MRT_purple))){
            mStationNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.stations_MRT_Purple_array)));
            trainNameHeader.setText(trainName);
            trainLogoHeader.setImageResource(R.drawable.mrt_purple_logo);
        }
    }



    private void initRecyclerView(){

        RecyclerView recyclerView = findViewById(R.id.StationRecyclerView);
        RecyclerViewAdapterStations recyclerViewAdapterStations = new RecyclerViewAdapterStations(this, mStationNames);
        recyclerView.setAdapter(recyclerViewAdapterStations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
