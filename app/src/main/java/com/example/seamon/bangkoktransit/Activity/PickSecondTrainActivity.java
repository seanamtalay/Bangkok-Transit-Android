package com.example.seamon.bangkoktransit.Activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.example.seamon.bangkoktransit.Adapter.RecyclerViewAdapterSecondTrain;
import com.example.seamon.bangkoktransit.R;

import java.util.ArrayList;
import java.util.Arrays;

public class PickSecondTrainActivity extends AppCompatActivity {
    private static final String TAG = "PickSecondTrainActivi";

    //vars
    private ArrayList<String> mTrainNames = new ArrayList<>();
    private String mFirstStation;
    private String mSelectedAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_second_train);
        getIncomingIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Snack bar asking user to pick the second station
        String originOrDestinationText = "";
        if(mSelectedAs.equals("origin")){
            originOrDestinationText = getResources().getString(R.string.select_destination_station);
            getSupportActionBar().setTitle("Pick Destination");
        }
        else if(mSelectedAs.equals("destination")){
            originOrDestinationText = getResources().getString(R.string.select_origin_station);
            getSupportActionBar().setTitle("Pick Origin");
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
            getSupportActionBar().setTitle("Pick Destination");
        }
        else if(mSelectedAs.equals("destination")){
            originOrDestinationText = getResources().getString(R.string.select_origin_station);
            getSupportActionBar().setTitle("Pick Origin");
        }
        Snackbar selectStationSnackbar = Snackbar.make(findViewById(android.R.id.content), originOrDestinationText, 7000);
        selectStationSnackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        selectStationSnackbar.show();
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if(getIntent().hasExtra("station_first")&& getIntent().hasExtra("selected_as")){
            Log.d(TAG, "getIncomingIntent: Found intent extras.");

            mFirstStation = getIntent().getStringExtra("station_first");
            mSelectedAs = getIntent().getStringExtra("selected_as");
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
        RecyclerViewAdapterSecondTrain recyclerViewAdapterSecondTrain = new RecyclerViewAdapterSecondTrain(this, mTrainNames, mFirstStation, mSelectedAs);
        recyclerView.setAdapter(recyclerViewAdapterSecondTrain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
