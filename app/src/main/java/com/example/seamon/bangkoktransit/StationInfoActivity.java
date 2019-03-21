package com.example.seamon.bangkoktransit;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class StationInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "StationInfoActivity";
    private String current_station;
    private Boolean has_ori_des_buttons = true;
    private Double mStationLat;
    private Double mStationLng;
    private String mStationNameForMaps;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView stationInfoName = findViewById(R.id.station_info_name);
        TextView stationInfoLine = findViewById(R.id.station_info_line);
        TextView stationInfoPlatform = findViewById(R.id.station_info_platform);
        TextView stationInfoExit = findViewById(R.id.station_info_exit);
        Button setOriginButton = findViewById(R.id.set_origin_button);
        Button setDestinationButton = findViewById(R.id.set_destination_button);

        current_station = getIncomingIntent();

        //Get the station code
        String[] splitedName = current_station.split("\\s+");
        String stationCode = splitedName[0];

        //get the station info based on the stationCode
        int resId = this.getResources().getIdentifier(stationCode,"array",this.getPackageName()); // might cause a bug (return 0)
        String[] stringArray = getResources().getStringArray(resId);
        String receivedName = stringArray[0];
        mStationNameForMaps = stringArray[0];
        String receivedLine = stringArray[1];
        String receivedPlatform = stringArray[2];
        String receivedExit = stringArray[3];
        String stationLatLng = stringArray[4];

        String[] latLng =  stationLatLng.split(", ");
        mStationLat = Double.parseDouble(latLng[0]);
        mStationLng = Double.parseDouble(latLng[1]);

        //change them accordingly
        stationInfoName.setText(receivedName);
        stationInfoLine.setText(receivedLine);
        stationInfoPlatform.setText(receivedPlatform);
        stationInfoExit.setText(receivedExit);

        toolbar.setTitle(current_station);
        changeLogo(receivedLine);

        setSupportActionBar(toolbar);

        //circle button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cite: https://developers.google.com/maps/documentation/urls/android-intents

                //Uri gmmIntentUri = Uri.parse("geo:"+mStationLat+","+mStationLng+"?q=" + stationNameForMaps + " station");
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+mStationLat+","+mStationLng+"("+mStationNameForMaps+ " station"+")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                mapIntent.setPackage("com.google.android.apps.maps");

                startActivity(mapIntent);

            }
        });

        //check if the buttons are needed or not
        if(!has_ori_des_buttons){
            setOriginButton.setVisibility(View.GONE);
            setDestinationButton.setVisibility(View.GONE);
        }

        //As Origin button
        setOriginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StationInfoActivity.this , PickSecondTrainActivity.class);
                intent.putExtra("station_first", current_station);
                intent.putExtra("selected_as", "origin");
                StationInfoActivity.this.startActivity(intent);
            }
        });

        //As Destination button
        setDestinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StationInfoActivity.this , PickSecondTrainActivity.class);
                intent.putExtra("station_first", current_station);
                intent.putExtra("selected_as", "destination");
                StationInfoActivity.this.startActivity(intent);
            }
        });


        //Maps
        if(googleServicesAvailable()){
            Log.d(TAG, "onCreate: in if");
            initMap();
        }


    }

    private void changeLogo(String line){
        TextView logo = findViewById(R.id.line_logo);
        if(line.equals("City Line")){
            logo.setText("  ARL  ");
            logo.setBackgroundColor(getResources().getColor(R.color.ARLRed));
            logo.setTextColor(getResources().getColor(R.color.white));
        }
        else if(line.equals("Sukhumvit Line")){
            logo.setText("  BTS  ");
            logo.setBackgroundColor(getResources().getColor(R.color.SukhumvitGreen));
            logo.setTextColor(getResources().getColor(R.color.black));
        }
        else if(line.equals("Silom Line")){
            logo.setText("  BTS  ");
            logo.setBackgroundColor(getResources().getColor(R.color.SilomGreen));
            logo.setTextColor(getResources().getColor(R.color.white));
        }
        else if(line.equals("Blue Line")){
            logo.setText("  MRT  ");
            logo.setBackgroundColor(getResources().getColor(R.color.MRTBlue));
            logo.setTextColor(getResources().getColor(R.color.white));
        }
        else if(line.equals("Purple Line")){
            logo.setText("  MRT  ");
            logo.setBackgroundColor(getResources().getColor(R.color.MRTPurple));
            logo.setTextColor(getResources().getColor(R.color.white));
        }
        else if(line.equals("Sukhumvit Line, Silom Line")){
            //use spanner to change multiple styles of one textView
            String linesText = "  BTS      BTS  ";
            SpannableString sString = new SpannableString(linesText);

            ForegroundColorSpan fcsWhite = new ForegroundColorSpan(getResources().getColor(R.color.white));
            ForegroundColorSpan fcsBlack = new ForegroundColorSpan(getResources().getColor(R.color.black));
            BackgroundColorSpan bgsSilom = new BackgroundColorSpan(getResources().getColor(R.color.SilomGreen));
            BackgroundColorSpan bgsSukhumvit = new BackgroundColorSpan(getResources().getColor(R.color.SukhumvitGreen));

            sString.setSpan(fcsBlack, 2,5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE  );
            sString.setSpan(fcsWhite, 11,14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE  );
            sString.setSpan(bgsSukhumvit,0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
            sString.setSpan(bgsSilom,9, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

            logo.setText(sString);
        }
        else if(line.equals("Blue Line, Purple Line")){
            //use spanner to change multiple styles of one textView
            String linesText = "  MRT      MRT  ";
            SpannableString sString = new SpannableString(linesText);

            ForegroundColorSpan fcsWhite = new ForegroundColorSpan(getResources().getColor(R.color.white));
            ForegroundColorSpan fcsBlack = new ForegroundColorSpan(getResources().getColor(R.color.white));
            BackgroundColorSpan bgsSilom = new BackgroundColorSpan(getResources().getColor(R.color.MRTBlue));
            BackgroundColorSpan bgsSukhumvit = new BackgroundColorSpan(getResources().getColor(R.color.MRTPurple));

            sString.setSpan(fcsBlack, 2,5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE  );
            sString.setSpan(fcsWhite, 11,14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE  );
            sString.setSpan(bgsSukhumvit,0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
            sString.setSpan(bgsSilom,9, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

            logo.setText(sString);
        }
    }

    private String getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if(getIntent().hasExtra("has_ori_des_buttons")){
            has_ori_des_buttons =  getIntent().getBooleanExtra("has_ori_des_buttons", true);
        }

        if(getIntent().hasExtra("station_name")){
            Log.d(TAG, "getIncomingIntent: Found intent extras.");

            String trainName = getIntent().getStringExtra("station_name");
            return trainName;

        }
        else{
            return "no_intent_received";
        }


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    //check for Play Services. It is required for GoogleMap API to work
    public boolean googleServicesAvailable(){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS){
            return true;
        }
        else if(api.isUserResolvableError(isAvailable)){
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        }
        else{
            Toast.makeText(this, "Can't connect to Play Services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    //initialize the GoogleMap fragment
    private void initMap(){
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.mapFragmentStationInfo);
        mapFragment.getMapAsync(this);
    }

    // Manipulating maps after it is built
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        LatLng sydney = new LatLng(-33.852, 151.211);
        LatLng stationLatLng = new LatLng(mStationLat, mStationLng);

        googleMap.addMarker(new MarkerOptions().position(stationLatLng)
                .title(mStationNameForMaps));
        float zoomLevel = 17.5f;

        //adjusting camera to the center
        double cameraLat = mStationLat /*- 0.0006*/;
        LatLng cameraLatLng = new LatLng(cameraLat, mStationLng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng, zoomLevel));
        //googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);


    }



}
