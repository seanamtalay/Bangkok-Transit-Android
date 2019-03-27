package com.example.seamon.bangkoktransit.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.seamon.bangkoktransit.Activity.ZoomTransitMapActivity;
import com.example.seamon.bangkoktransit.Adapter.EtaStationRecyclerAdapter;
import com.example.seamon.bangkoktransit.Adapter.RecyclerViewAdapterStations;
import com.example.seamon.bangkoktransit.R;

import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;

public class Tab3Fragment extends Fragment {
    private static final String TAG = "Tab3Fragment";

    private ArrayList<String> mNearByStations = new ArrayList<>();
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private String[] mAllStationsArray;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3fragment, container, false);
        ImageView transitMap = rootView.findViewById(R.id.transit_map);
        Button ARLTimeButton = rootView.findViewById(R.id.ARL_time_button);
        Button BTSTimeButton = rootView.findViewById(R.id.BTS_time_button);
        Button MRTTimeButton = rootView.findViewById(R.id.MRT_time_button);

        mAllStationsArray = getResources().getStringArray(R.array.all_stations_name_array);
        mContext = getContext();


        transitMap.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ZoomTransitMapActivity.class);
                getActivity().startActivity(intent);


            }
        });

        //pop up message showing time schedules
        ARLTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewPopup = LayoutInflater.from(getActivity()).inflate(R.layout.layout_time_schedules_info_popup, null);

                initTable(viewPopup, "ARLSchedules");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Time Schedules")
                        .setView(viewPopup);
                AlertDialog timeScheduleAlert = builder.create();
                timeScheduleAlert.show();

            }
        });

        BTSTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View viewPopup = LayoutInflater.from(getActivity()).inflate(R.layout.layout_time_schedules_info_popup, null);

                initTable(viewPopup, "BTSSchedules");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Time Schedules")
                        .setView(viewPopup);
                AlertDialog timeScheduleAlert = builder.create();
                timeScheduleAlert.show();

            }
        });

        MRTTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewPopup = LayoutInflater.from(getActivity()).inflate(R.layout.layout_time_schedules_info_popup, null);

                initTable(viewPopup, "MRTSchedules");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Time Schedules")
                        .setView(viewPopup);
                AlertDialog timeScheduleAlert = builder.create();
                timeScheduleAlert.show();

            }
        });

        getNearByStations(rootView);

        return rootView;
    }



    public void initTable(View rootView, String trainArrayName) {
        TableLayout stk = (TableLayout) rootView.findViewById(R.id.table_main);
        TableRow tbRow0 = new TableRow(getContext());
        TextView hourLabel = new TextView(getContext());
        hourLabel.setText("Hours");
        hourLabel.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
        hourLabel.setTextColor(Color.argb(200, 0, 0, 0));
        hourLabel.setGravity(Gravity.CENTER_HORIZONTAL);
        hourLabel.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tbRow0.addView(hourLabel);
        TextView frequencyLabel = new TextView(getContext());
        frequencyLabel.setText("Frequency");
        frequencyLabel.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
        frequencyLabel.setTextColor(Color.argb(200, 0, 0, 0));
        frequencyLabel.setGravity(Gravity.CENTER_HORIZONTAL);
        frequencyLabel.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tbRow0.addView(frequencyLabel);

        TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams();
        tableRowParams.setMargins(0,20,0,20);
        tbRow0.setLayoutParams(tableRowParams);

        stk.addView(tbRow0);

        //get array from resource file
        int resId = this.getResources().getIdentifier(trainArrayName,"array",getContext().getPackageName()); // might cause a bug (return 0)
        String[] infoArray = getResources().getStringArray(resId);

        for (int i = 0; i < infoArray.length; i+=2) {
            TableRow tbRow = new TableRow(getContext());
            TextView t1v = new TextView(getContext());
            t1v.setText(infoArray[i]);
            t1v.setTextColor(Color.argb(161, 0, 0, 0));
            t1v.setGravity(Gravity.CENTER_HORIZONTAL);
            t1v.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tbRow.addView(t1v);
            TextView t2v = new TextView(getContext());
            t2v.setText(infoArray[i+1]);
            t2v.setTextColor(Color.argb(161, 0, 0, 0));
            t2v.setGravity(Gravity.CENTER);
            t2v.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tbRow.addView(t2v);

            TableLayout.LayoutParams tableRowParamsSub = new TableLayout.LayoutParams();
            tableRowParamsSub.setMargins(0,12,0,12);
            tbRow.setLayoutParams(tableRowParamsSub);

            stk.addView(tbRow);
        }

    }

    private void initRecyclerView(View rootView, ArrayList<String> mStationNames){

        RecyclerView recyclerView = rootView.findViewById(R.id.nearby_eta_recyclerView);
        EtaStationRecyclerAdapter recyclerViewAdapterStations = new EtaStationRecyclerAdapter(getActivity(), mStationNames);
        recyclerView.setAdapter(recyclerViewAdapterStations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getNearByStations(final View view) {
        Log.d(TAG, "getNearByStations: starts");
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        final TextView noNearbyText = view.findViewById(R.id.nearby_eta_no_station_text);
        final ImageView noNearbyImage = view.findViewById(R.id.nearby_eta_no_station_image);
        final RecyclerView nearbyRecyclerView = view.findViewById(R.id.nearby_eta_recyclerView);
        final TextView note = view.findViewById(R.id.nearby_eta_note_text);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //clear the arrayList first
                mNearByStations.clear();
                //Log.d(TAG, "onLocationChanged: location: " + location.toString());
                //compare nearby locations with current lat long
                Double currentLat = location.getLatitude();
                Double currentLng = location.getLongitude();

                // near by radius =  +- 0.1
                String[] eachStation;
                String eachStationLatLng;
                String[] eachLatLng;
                Double compareLat;
                Double compareLng;

                //for all the lat lng{
                for(String current_station : mAllStationsArray) {
                    //extract station code from current_station name
                    String[] splitedName = current_station.split("\\s+");
                    String stationCode = splitedName[0];

                    //get the station info based on the stationCode
                    int resId = mContext.getResources().getIdentifier(stationCode,"array",mContext.getPackageName()); // might cause a bug (return 0)
                    eachStation = mContext.getResources().getStringArray(resId);
                    eachStationLatLng = eachStation[4];
                    eachLatLng =  eachStationLatLng.split(", ");
                    compareLat = Double.parseDouble(eachLatLng[0]);
                    compareLng = Double.parseDouble(eachLatLng[1]);

                    if (compareLat <= currentLat + 0.01 && compareLat >= currentLat - 0.01) {
                        if (compareLng <= currentLng + 0.01 && compareLng >= currentLng - 0.01) {
                            //Log.d(TAG, "onLocationChanged: detected station nearby! :"+eachStation[0]);
                            //add that station to the mNearByStations arrayList
                            mNearByStations.add(current_station); // full name with code format
                        }
                    }
                }

                //set the text to be invisible if there are nearby stations
                if(mNearByStations.size() != 0){
                    noNearbyText.setVisibility(View.GONE);
                    noNearbyImage.setVisibility(View.GONE);
                    nearbyRecyclerView.setVisibility(View.VISIBLE);
                    note.setVisibility(View.VISIBLE);
                }
                else{
                    noNearbyText.setVisibility(View.VISIBLE);
                    noNearbyImage.setVisibility(View.VISIBLE);
                    nearbyRecyclerView.setVisibility(View.GONE);
                    note.setVisibility(View.GONE);
                }

                initRecyclerView(view, mNearByStations);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if(Build.VERSION.SDK_INT > 23) {

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }

        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);

        Log.d(TAG, "getNearByStations: before calling initRecyclerView");
        initRecyclerView(view, mNearByStations);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            }
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: is called");
        super.onPause();
        mLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: is called");
        super.onStop();
        mLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: is called");
        super.onResume();
        View rootView = getView();
        getNearByStations(rootView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLocationManager.removeUpdates(mLocationListener);
    }
}