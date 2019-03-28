package com.example.seamon.bangkoktransit.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.seamon.bangkoktransit.Adapter.RecyclerViewAdapter;
import com.example.seamon.bangkoktransit.R;

import java.util.ArrayList;
import java.util.Arrays;

public class AllStationTabFragment extends Fragment{
    private static final String TAG = "AllStationTabFragment";
    //vars
    private ArrayList<String> mTrainNames = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_station_tab, container, false);

        initTrainNames(rootView);

        return rootView;
    }

    private void initTrainNames(View view){
        //Add train types to the list only when it is empty
        if(mTrainNames.size() == 0) {
            // convert array to arrayList
            mTrainNames = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.train_names_array)));
        }

        initRecyclerView(view);
    }

    private void initRecyclerView(View view){
        Log.d(TAG, "initRecyclerView: initing");
        RecyclerView recyclerView = view.findViewById(R.id.TabRecyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), mTrainNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
