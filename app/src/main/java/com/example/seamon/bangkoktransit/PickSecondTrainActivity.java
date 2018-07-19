package com.example.seamon.bangkoktransit;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class PickSecondTrainActivity extends AppCompatActivity {
    private static final String TAG = "PickSecondTrainActivi";

    //vars
    private ArrayList<String> mTrainNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_second_train);
        getIncomingIntent();
        //Snack bar asking user to pick the second station
        Snackbar selectStationSnackbar = Snackbar.make(findViewById(android.R.id.content), R.string.select_origin_station, 7000);
        selectStationSnackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        selectStationSnackbar.show();
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if(getIntent().hasExtra("station_first")){
            Log.d(TAG, "getIncomingIntent: Found intent extras.");

            String station_first = getIntent().getStringExtra("station_first");

            initTrainNames();
        }
    }

    private void initTrainNames(){
        //Add train types to the list only when it is empty
        if(mTrainNames.size() == 0) {
            // convert array to arrayList
            mTrainNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.train_names_array)));
        }

        initRecyclerView();
    }

    private void initRecyclerView(){

        RecyclerView recyclerView = findViewById(R.id.SecondTrainRecyclerView);
        RecyclerViewAdapterSecondTrain recyclerViewAdapterSecondTrain = new RecyclerViewAdapterSecondTrain(this, mTrainNames);
        recyclerView.setAdapter(recyclerViewAdapterSecondTrain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
