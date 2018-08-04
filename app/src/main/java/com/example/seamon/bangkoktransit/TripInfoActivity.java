package com.example.seamon.bangkoktransit;

import android.app.Dialog;
import android.location.Address;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TripInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "TripInfoActivity";
    private String mOriginStation;
    private String mDestinationStation;
    private String mSelectedAs;
    private Double mDestinationStationLat;
    private Double mDestinationStationLng;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: first");
        setContentView(R.layout.activity_trip_info);
        Log.d(TAG, "onCreate: second");
        TextView firstStationTextview = findViewById(R.id.first_station_textview);
        TextView secondStationTextview = findViewById(R.id.second_station_textview);
        ImageView arrowStations = findViewById(R.id.arrow_stations);

        arrowStations.setRotation(180);

        //get the names from the intent and change the textViews accordingly
        getIncomingIntent();

        //update mDestinationStationLatLng according to the destination station name
        updateDestinationLatLng();

        firstStationTextview.setText(mOriginStation);
        secondStationTextview.setText(mDestinationStation);
        Log.d(TAG, "onCreate: before if");
        if(googleServicesAvailable()){
            Log.d(TAG, "onCreate: in if");
            initMap();
        }

    }

    //get the names of picked stations from the intent. Then update the globals
    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if(getIntent().hasExtra("first_station") && getIntent().hasExtra("second_station") && getIntent().hasExtra("selected_as")) {
            mSelectedAs = getIntent().getStringExtra("selected_as");

            //assign the station according to the selected option (origin or destination)
            if (mSelectedAs.equals("origin")){
                mOriginStation = getIntent().getStringExtra("first_station");
                mDestinationStation = getIntent().getStringExtra("second_station");

            }
            else if(mSelectedAs.equals("destination")){
                mDestinationStation = getIntent().getStringExtra("first_station");
                mOriginStation = getIntent().getStringExtra("second_station");
            }

            Log.d(TAG, "getIncomingIntent: Found intent extras: " + mOriginStation);
            Log.d(TAG, "getIncomingIntent: Found intent extras: " + mDestinationStation);
        }
    }

    private void updateDestinationLatLng(){
        //get Lat & Lng from the second station name
        //Get the station code
        String[] splitedName = mDestinationStation.split("\\s+");
        String stationCode = splitedName[0];

        //get the station info based on the stationCode
        int resId = this.getResources().getIdentifier(stationCode,"array",this.getPackageName()); // might cause a bug (return 0)
        String[] stringArray = getResources().getStringArray(resId);
        String destinationLatLng = stringArray[4];

        String[] latLng =  destinationLatLng.split(", ");
        mDestinationStationLat = Double.parseDouble(latLng[0]);
        mDestinationStationLng = Double.parseDouble(latLng[1]);
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
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.mapFragmentTripInfo);
        mapFragment.getMapAsync(this);
    }

    // Manipulating maps after it is built
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        LatLng sydney = new LatLng(-33.852, 151.211);
        LatLng stationLatLng = new LatLng(mDestinationStationLat, mDestinationStationLng);

        googleMap.addMarker(new MarkerOptions().position(stationLatLng)
                .title(mDestinationStation));
        float zoomLevel = 18.0f;

        //adjusting camera to the center
        double cameraLat = mDestinationStationLat - 0.0006;
        LatLng cameraLatLng = new LatLng(cameraLat, mDestinationStationLng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng, zoomLevel));


    }

    //Calculate costs
    private int getTotalCost(String originStation, String destinationStation){
        //ArrayList holding station code of the ones that is a transit station
        ArrayList<String> ARLTransitStations = new ArrayList<String>();
        ARLTransitStations.add("A6");
        ARLTransitStations.add("A8");

        ArrayList<String> BTSTransitStations = new ArrayList<String>();
        ARLTransitStations.add("N8");
        ARLTransitStations.add("N2");
        ARLTransitStations.add("CEN");
        ARLTransitStations.add("E4");
        ARLTransitStations.add("S2");

        ArrayList<String> MRTTransitStations = new ArrayList<String>();
        MRTTransitStations.add("BL10");
        MRTTransitStations.add("BL13");
        MRTTransitStations.add("BL21");
        MRTTransitStations.add("BL22");
        MRTTransitStations.add("BL26");

        //Get the station code
        String[] splitedNameDes = destinationStation.split("\\s+");
        String destinationStationCode = splitedNameDes[0];
        String[] splitedNameOri = originStation.split("\\s+");
        String originStationCode = splitedNameOri[0];

        int totalCost = 0;

        // get only line code eg. BL10 -> BL, N2 -> N
        String originLineCode = originStationCode.replaceAll("[^A-Za-z]+", "");
        String destinationLineCode = destinationStationCode.replaceAll("[^A-Za-z]+", "");

        //if origin and destination are on the same line
        if(originLineCode.equals(destinationLineCode)){
            totalCost = assignGetCostFunc(originStationCode, destinationStationCode);
        }

        return totalCost;
    }

    //both station has to be on the same line
    private int assignGetCostFunc(String originStationCode, String destinationStationCode){
        int totalCost = 0;
        // get only line code eg. BL10 -> BL, N2 -> N
        String originLineCode = originStationCode.replaceAll("[^A-Za-z]+", "");
        String destinationLineCode = destinationStationCode.replaceAll("[^A-Za-z]+", "");

        //get only number digits eg. BL10 -> 10, N2 -> 2
        int originStationNumber = Integer.parseInt(originStationCode.replaceAll("\\D+",""));
        int destinationStationNumber = Integer.parseInt(destinationStationCode.replaceAll("\\D+",""));
        //ARL
        if(originLineCode.equals("A")){
            totalCost = getCostARL(originStationCode, destinationStationCode);
        }
        //BTS WRONG
        else if(originLineCode.equals("E") || originLineCode.equals("N") || originLineCode.equals("CEN") || originLineCode.equals("W") || originLineCode.equals("S")){
            totalCost = getCostBTS(originStationCode, destinationStationCode);
        }
        //MRT
        else if(originLineCode.equals("BL") || originLineCode.equals("PP")){
            totalCost = getCostMRT(originStationCode, destinationStationCode);
        }

        return totalCost;
    }

    private int getCostMRT(String originStationCode, String destinationStationCode){
        // pricing based on https://www.bangkokmetro.co.th/web/imgcontent/Image/rate59.jpg
        // and https://www.bangkokmetro.co.th/web/imgcontent/Image/tk17mark3.jpg
        // on August 3rd 2018
        // Not super accurate due to the non linear pricing when combine Blue line and Purple Line together
        int numStation;

        //get only number digits eg. BL10 -> 10, N2 -> 2
        int originStationIndex = Integer.parseInt(originStationCode.replaceAll("\\D+",""));
        int destinationStationIndex = Integer.parseInt(destinationStationCode.replaceAll("\\D+",""));

        // get only line code eg. BL10 -> BL, N2 -> N
        String originLineCode = originStationCode.replaceAll("[^A-Za-z]+", "");
        String destinationLineCode = destinationStationCode.replaceAll("[^A-Za-z]+", "");

        //if origin and destination are not in the same line (one in PP one in BL). numStation is (num from Origin to end of Line 1) + (num from Des to end of Line 2). Tao Poon in this case.
        if(!originLineCode.equals(destinationLineCode)){
            if(originLineCode.equals("PP")) {
                numStation = Math.abs(16 - originStationIndex) + Math.abs(destinationStationIndex - 10); //16 is from PP16, Tao Poon station. 10 is from BL10, Tao Poon.
            }
            else{
                numStation = Math.abs(10 - originStationIndex) + Math.abs(destinationStationIndex - 16);
            }
        }
        else { // if origin and destination are in the same line, just simply subtract each other
             numStation = Math.abs(destinationStationIndex - originStationIndex);
        }

        int totalCost = 0;

        if(numStation <= 1)
            totalCost = 16;
        else if(numStation == 2)
            totalCost = 19;
        else if(numStation == 3)
            totalCost = 21;
        else if(numStation == 4)
            totalCost = 23;
        else if(numStation == 5)
            totalCost = 25;
        else if(numStation == 6)
            totalCost = 28;
        else if(numStation == 7)
            totalCost = 30;
        else if(numStation == 8)
            totalCost = 32;
        else if(numStation == 9)
            totalCost = 35;
        else if(numStation == 10)
            totalCost = 37;
        else if(numStation == 11)
            totalCost = 39;
        else if(numStation >= 12 && numStation <= 18)
            totalCost = 42;
        else if(numStation == 19)
            totalCost = 45;
        else if(numStation == 20)
            totalCost = 48;
        else if(numStation == 21)
            totalCost = 51;
        else if(numStation == 22)
            totalCost = 45;
        else if(numStation == 23)
            totalCost = 48;
        else if(numStation == 24)
            totalCost = 51;
        else if(numStation == 25)
            totalCost = 53;
        else if(numStation == 26)
            totalCost = 56;
        else if(numStation == 27)
            totalCost = 58;
        else if(numStation == 28)
            totalCost = 60;
        else if(numStation == 29)
            totalCost = 63;
        else if(numStation == 30)
            totalCost = 66;
        else if(numStation == 31)
            totalCost = 69;
        else if(numStation >= 32)
            totalCost = 70;

        return totalCost;
    }

    private int getCostBTS(String originStationCode, String destinationStationCode){
        // pricing based on http://www.bts.co.th/customer/th/01-ticketing-type-sing-journey-ticket.aspx
        // and https://img.pptvhd36.com/resize/wTOV8T4Ccm3Z_Aza6xxARgcpfLQ=/0x0/smart/filters:quality(75)/Y29udGVudHMlMkZmaWxlcyUyRnBoZXQlMkYlRTAlQjglQUElRTAlQjglODQ2MCUyRjU5YTRlODZlMGU0OTMuanBn
        // on August 3rd 2018
        // Not super accurate due to the non linear pricing after the non-Bangkok extension stations
        int numStation;
        int originStationIndex;
        int destinationStationIndex;
        String originLineCode;
        String destinationLineCode;

        if(!originStationCode.equals("CEN") && !destinationStationCode.equals("CEN")) {
            //get only number digits eg. BL10 -> 10, N2 -> 2
            originStationIndex = Integer.parseInt(originStationCode.replaceAll("\\D+", ""));
            destinationStationIndex = Integer.parseInt(destinationStationCode.replaceAll("\\D+", ""));

            // get only line code eg. BL10 -> BL, N2 -> N
            originLineCode = originStationCode.replaceAll("[^A-Za-z]+", "");
            destinationLineCode = destinationStationCode.replaceAll("[^A-Za-z]+", "");
        }
        else if(originStationCode.equals("CEN")){
            originStationIndex = 0;
            originLineCode = "CEN";
            destinationStationIndex = Integer.parseInt(destinationStationCode.replaceAll("\\D+", ""));
            destinationLineCode = destinationStationCode.replaceAll("[^A-Za-z]+", "");
        }
        else{
            destinationStationIndex = 0;
            destinationLineCode = "CEN";
            originStationIndex = Integer.parseInt(originStationCode.replaceAll("\\D+", ""));
            originLineCode = originStationCode.replaceAll("[^A-Za-z]+", "");
        }

        //if origin and destination don't have the same code, compare each to CEN station and add them together
        if(!originLineCode.equals(destinationLineCode)){
            numStation = Math.abs(destinationStationIndex - 0) + Math.abs(0 - originStationIndex); // 0 from CEN station
        }
        else{// if origin and destination have same code, simply subtract them together
            numStation = Math.abs(destinationStationIndex - originStationIndex);
        }

        int totalCost = 0;

        if(numStation <= 1)
            totalCost = 16;
        else if(numStation == 2)
            totalCost = 23;
        else if(numStation == 3)
            totalCost = 26;
        else if(numStation == 4)
            totalCost = 30;
        else if(numStation == 5)
            totalCost = 33;
        else if(numStation == 6)
            totalCost = 37;
        else if(numStation == 7)
            totalCost = 40;
        else if(numStation == 8)
            totalCost = 44;
        else if(numStation >= 9)
            totalCost = 59;


        return totalCost;
    }

    private int getCostARL(String originStationCode, String destinationStationCode){
        // pricing based on http://www.srtet.co.th/index.php/th/cityline-calculate/cityline-calculate
        // and http://www.deesudsud.com/wp-content/uploads/2014/10/airport-link-fare-table.jpg
        // on August 3rd 2018
        int originStationIndex = Integer.parseInt(originStationCode.replaceAll("\\D+",""));
        int destinationStationIndex = Integer.parseInt(destinationStationCode.replaceAll("\\D+",""));

        int numStation = Math.abs(destinationStationIndex - originStationIndex);
        int totalCost = 0;

        if(numStation <= 1)
            totalCost = 15;
        else if(numStation == 2)
            totalCost = 20;
        else if(numStation == 3)
            totalCost = 25;
        else if(numStation == 4)
            totalCost = 30;
        else if(numStation == 5)
            totalCost = 35;
        else if(numStation == 6)
            totalCost = 40;
        else if(numStation >= 7)
            totalCost = 45;

        return totalCost;
    }

}
