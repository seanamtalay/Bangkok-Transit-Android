package com.example.seamon.bangkoktransit.Fragment;

import android.animation.LayoutTransition;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.seamon.bangkoktransit.Adapter.RecyclerViewAdapter;
import com.example.seamon.bangkoktransit.Adapter.RecyclerViewAdapterStations;
import com.example.seamon.bangkoktransit.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class AllStationTabFragment extends Fragment{
    private static final String TAG = "AllStationTabFragment";
    //vars
    private ArrayList<String> mTrainNames = new ArrayList<>();
    private ArrayList<String> allStationList;
    private EditText mSearchEditText;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_station_tab, container, false);

        allStationList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.all_stations_name_array)));

        if(mTrainNames.size() == 0) {
            // convert array to arrayList
            mTrainNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.train_names_array)));
        }

        recyclerView = rootView.findViewById(R.id.TabRecyclerView);
        updateRecyclerView(mTrainNames);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Search for station
        mSearchEditText = view.findViewById(R.id.all_station_tab_search_editText);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mTrainNames.clear();
                if(count == 0){
                    //if there is nothing in the editText, show the train names
                    mTrainNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.train_names_array)));
                    updateRecyclerView(mTrainNames);
                }
                else{
                    //else show the station that contain the keyword
                    for(String stationName: allStationList){
                        if(Pattern.compile(Pattern.quote(charSequence.toString()), Pattern.CASE_INSENSITIVE).matcher(stationName).find()){
                            mTrainNames.add(stationName);
                        }
                    }
                    //if there is no result found then show default trains
                    if(mTrainNames.size() == 0){
                        mTrainNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.train_names_array)));
                    }
                    updateRecyclerView(mTrainNames);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ViewGroup constraintLayout = (ViewGroup) view.findViewById(R.id.all_station_constraintLayout);
            LayoutTransition constraintLayoutTransition = constraintLayout.getLayoutTransition();
            constraintLayoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }
    }

    private void updateRecyclerView(ArrayList<String> trainNamesList){
        Log.d(TAG, "initRecyclerView: updating");
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), trainNamesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
