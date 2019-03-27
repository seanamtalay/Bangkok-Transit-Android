package com.example.seamon.bangkoktransit.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seamon.bangkoktransit.Adapter.RecyclerViewAdapterNearByStationsTab2;
import com.example.seamon.bangkoktransit.R;

import java.util.ArrayList;


import static android.content.Context.LOCATION_SERVICE;

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    private ArrayList<String> mNearByStations = new ArrayList<>();
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2fragment, container, false);

        getNearByStations(rootView);
        return rootView;
    }
    
    private void getNearByStations(final View view) {
        Log.d(TAG, "getNearByStations: starts");
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        final TextView noNearbyText = view.findViewById(R.id.noNearbyText);
        final ImageView noNearbyImage = view.findViewById(R.id.noNearbyImage);

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

                String[] mAllStationsArray = getResources().getStringArray(R.array.all_stations_name_array);

                //for all the lat lng{
                for(String current_station : mAllStationsArray) {
                    //extract station code from current_station name
                    String[] splitedName = current_station.split("\\s+");
                    String stationCode = splitedName[0];

                    //get the station info based on the stationCode
                    int resId = getContext().getResources().getIdentifier(stationCode,"array",getContext().getPackageName()); // might cause a bug (return 0)
                    eachStation = getResources().getStringArray(resId);
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
                    noNearbyText.setVisibility(View.INVISIBLE);
                    noNearbyImage.setVisibility(View.INVISIBLE);
                }
                else{
                    noNearbyText.setVisibility(View.VISIBLE);
                    noNearbyImage.setVisibility(View.VISIBLE);
                }

                initRecyclerView(view);
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

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        initRecyclerView(view);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
            }
        }
    }

    private void initRecyclerView(View view){
        //Log.d(TAG, "initRecyclerView: initing");
        RecyclerView recyclerView = view.findViewById(R.id.Tab2RecyclerView);
        RecyclerViewAdapterNearByStationsTab2 adapter = new RecyclerViewAdapterNearByStationsTab2(getActivity(), mNearByStations);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
