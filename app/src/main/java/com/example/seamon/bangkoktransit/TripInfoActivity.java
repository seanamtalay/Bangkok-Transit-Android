package com.example.seamon.bangkoktransit;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.w3c.dom.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TripInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "TripInfoActivity";
    private String mOriginStation;
    private String mDestinationStation;
    private String mSelectedAs;
    private Double mDestinationStationLat;
    private Double mDestinationStationLng;
    private GoogleMap mGoogleMap;
    private String mDestinationNameForMaps;


    public class CostAndTransit {
        private final int cost;
        private final int transit;

        public CostAndTransit(int first, int second) {
            this.cost = first;
            this.transit = second;
        }

        public int getCost() {
            return cost;
        }

        public int getNumTransit() {
            return transit;
        }
    }

    public class Graph {
        private Set<Node> nodes = new HashSet<>();

        public void addNode(Node nodeA) {
            nodes.add(nodeA);
        }

        // getters and setters
        public Set<Node> getNodes() {
            return nodes;
        }

        public void setNodes(Set<Node> nodes) {
            this.nodes = nodes;
        }
    }

    public class Node {
        private String name;
        private List<Node> shortestPath = new LinkedList<>();
        private Integer distance = Integer.MAX_VALUE;
        Map<Node, Integer> adjacentNodes = new HashMap<>();

        public void addDestination(Node destination, int distance) {
            adjacentNodes.put(destination, distance);
        }

        public Node(String name) {
            this.name = name;
        }

        public String getName(){
            return this.name;
        }

        // getters and setters
        public void setDistance(Integer distance){
            this.distance = distance;
        }
        public Integer getDistance(){
            return this.distance;
        }

        public Map<Node, Integer> getAdjacentNodes(){
            return this.adjacentNodes;
        }

        public void setShortestPath(LinkedList<Node> shortestPath){
            this.shortestPath = shortestPath;
        }

        public List<Node> getShortestPath(){
            return this.shortestPath;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: first");
        setContentView(R.layout.activity_trip_info);
        Log.d(TAG, "onCreate: second");
        TextView firstStationTextView = findViewById(R.id.first_station_textview);
        TextView secondStationTextView = findViewById(R.id.second_station_textview);
        TextView estimatedCostTextView = findViewById(R.id.estimated_cost_value);
        TextView numTransitsTextView = findViewById(R.id.num_transits_value);
        ImageView arrowStations = findViewById(R.id.arrow_stations);
        ImageView firstStationInfoIcon = findViewById(R.id.station1_info_icon);
        ImageView secondStationInfoIcon = findViewById(R.id.station2_info_icon);
        FloatingActionButton fabDirectionTrip = (FloatingActionButton) findViewById(R.id.fab_direction_trip);

        arrowStations.setRotation(180);

        //get the names from the intent and change the textViews accordingly
        getIncomingIntent();

        //update mDestinationStationLatLng according to the destination station name
        updateDestinationLatLng();

        firstStationTextView.setText(mOriginStation.replace("(", " \n("));
        secondStationTextView.setText(mDestinationStation.replace("(", " \n("));
        Log.d(TAG, "onCreate: before if");
        if(googleServicesAvailable()){
            Log.d(TAG, "onCreate: in if");
            initMap();
        }

        //set cost and numTransit value textView
        CostAndTransit costAndTransit = getTotalCost(mOriginStation, mDestinationStation);
        estimatedCostTextView.setText(Integer.toString(costAndTransit.getCost())+ " Baht");
        numTransitsTextView.setText(Integer.toString(costAndTransit.getNumTransit()));

        //on click info icons
        firstStationInfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripInfoActivity.this, StationInfoActivity.class);
                intent.putExtra("station_name", mOriginStation);
                intent.putExtra("has_ori_des_buttons", false);
                TripInfoActivity.this.startActivity(intent);
            }
        });

        secondStationInfoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripInfoActivity.this, StationInfoActivity.class);
                intent.putExtra("station_name", mDestinationStation);
                intent.putExtra("has_ori_des_buttons", false);
                TripInfoActivity.this.startActivity(intent);
            }
        });

        //fab circle button
        fabDirectionTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cite: https://developers.google.com/maps/documentation/urls/android-intents
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //Uri gmmIntentUri = Uri.parse("geo:"+mStationLat+","+mStationLng+"?q=" + stationNameForMaps + " station");
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+mDestinationStationLat+","+mDestinationStationLng+"("+mDestinationNameForMaps+ " station"+")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                mapIntent.setPackage("com.google.android.apps.maps");

                startActivity(mapIntent);

                // Display a label at the location of Google's Sydney office
                ;


                startActivity(mapIntent);
            }
        });
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
        mDestinationNameForMaps = stringArray[0];
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

    public List<String> getShortestDistant(String originStationCode, String destinationStationCode){
        //initialize all the nodes first, then link them together

        //containers, all+1 so that indexing will be easier, Station A1 = ARLNodesArray_Ax[1] instead of ARLNodesArray_Ax[0]
        Node ARLNodesArray_Ax[] = new Node[8+1];  //ARL
        Node BTSNodesArray_Nx[] = new Node[8+1];  //BTS that starts with N (mostly Sukhumvit line)
        Node BTSNodesArray_Ex[] = new Node[15+1]; //BTS that starts with E (mostly Sukhumvit line)
        Node BTSNodesArray_CEN[] = new Node[1]; //BTS CEN
        Node BTSNodesArray_Wx[] = new Node[1+1];  //BTS that starts with W (mostly Silom line)
        Node BTSNodesArray_Sx[] = new Node[12+1]; //BTS that starts with S (mostly Silom line)
        Node MRTNodesArray_BLx[] = new Node[28+1];//MRT Blue ,only have 19 member in it but set as 28 because indexing
        Node MRTNodesArray_PPx[] = new Node[16+1];//MRT Purple

        //initialize nodes
        for(int i = 1; i <= 8; i++){
            String stationCode = "A" + (i);
            ARLNodesArray_Ax[i] = new Node(stationCode);
        }
        for(int i = 1; i <= 8; i++){
            String stationCode = "N" + (i);
            BTSNodesArray_Nx[i] = new Node(stationCode);
        }
        for(int i = 1; i <= 15; i++){
            String stationCode = "E" + (i);
            BTSNodesArray_Ex[i] = new Node(stationCode);
        }
        for(int i = 0; i < 1; i++){
            String stationCode = "CEN"; //+1 because array starts from zero
            BTSNodesArray_CEN[i] = new Node(stationCode);
        }
        for(int i = 1; i <= 1; i++){
            String stationCode = "W" + (i);
            BTSNodesArray_Wx[i] = new Node(stationCode);
        }
        for(int i = 1; i <= 12; i++){
            String stationCode = "S" + (i); //+1 because array starts from zero
            BTSNodesArray_Sx[i] = new Node(stationCode);
        }
        for(int i = 10; i <= 28; i++){
            String stationCode = "BL" + (i);
            MRTNodesArray_BLx[i] = new Node(stationCode);
        }
        for(int i = 1; i <= 16; i++){
            String stationCode = "PP" + (i);
            MRTNodesArray_PPx[i] = new Node(stationCode);
        }

        //add adjacent nodes to each node
        //ARL
        for(int i = 1; i <= 8; i++){
            if(ARLNodesArray_Ax[i].getName().equals("A1")){
                //terminal station1
                ARLNodesArray_Ax[i].addDestination(ARLNodesArray_Ax[i+1], 1);
            }
            else if(ARLNodesArray_Ax[i].getName().equals("A8")){
                //terminal station2
                ARLNodesArray_Ax[i].addDestination(ARLNodesArray_Ax[i-1], 1);
            }
            else {
                ARLNodesArray_Ax[i].addDestination(ARLNodesArray_Ax[i+1], 1);
                ARLNodesArray_Ax[i].addDestination(ARLNodesArray_Ax[i-1], 1);
            }
            //ARL w/ transits
            if(ARLNodesArray_Ax[i].getName().equals("A6")){ // Linked with BL21
                ARLNodesArray_Ax[i].addDestination(MRTNodesArray_BLx[21],1);
            }
            if(ARLNodesArray_Ax[i].getName().equals("A8")){ //Linked with N2
                ARLNodesArray_Ax[i].addDestination(BTSNodesArray_Nx[2],1);
            }
        }

        //BTS N
        for(int i = 1; i <= 8; i++){
            if(BTSNodesArray_Nx[i].getName().equals("N1")){
                //terminal station1
                BTSNodesArray_Nx[i].addDestination(BTSNodesArray_Nx[i+1], 1);
                BTSNodesArray_Nx[i].addDestination(BTSNodesArray_CEN[0], 1); //CEN
            }
            else if(BTSNodesArray_Nx[i].getName().equals("N8")){
                //terminal station2
                BTSNodesArray_Nx[i].addDestination(BTSNodesArray_Nx[i-1], 1);
            }
            else {
                BTSNodesArray_Nx[i].addDestination(BTSNodesArray_Nx[i+1], 1);
                BTSNodesArray_Nx[i].addDestination(BTSNodesArray_Nx[i-1], 1);
            }
            //BTS N w/ transits
            if(BTSNodesArray_Nx[i].getName().equals("N8")){ // Linked with BL13
                BTSNodesArray_Nx[i].addDestination(MRTNodesArray_BLx[13],1);
            }
            if(BTSNodesArray_Nx[i].getName().equals("N2")){ //Linked with A8
                BTSNodesArray_Nx[i].addDestination(ARLNodesArray_Ax[8],1);
            }
        }

        //BTS E
        for(int i = 1; i <= 15; i++){
            if(BTSNodesArray_Ex[i].getName().equals("E1")){
                //terminal station1
                BTSNodesArray_Ex[i].addDestination(BTSNodesArray_Ex[i+1], 1);
                BTSNodesArray_Ex[i].addDestination(BTSNodesArray_CEN[0], 1); //CEN
            }
            else if(BTSNodesArray_Ex[i].getName().equals("E15")){
                //terminal station2
                BTSNodesArray_Ex[i].addDestination(BTSNodesArray_Ex[i-1], 1);
            }
            else {
                BTSNodesArray_Ex[i].addDestination(BTSNodesArray_Ex[i+1], 1);
                BTSNodesArray_Ex[i].addDestination(BTSNodesArray_Ex[i-1], 1);
            }
            //BTS E w/ transits
            if(BTSNodesArray_Ex[i].getName().equals("E4")){ // Linked with BL22
                BTSNodesArray_Ex[i].addDestination(MRTNodesArray_BLx[22],1);
            }
        }

        //BTS W
        for(int i = 1; i <= 1; i++){
            if(BTSNodesArray_Wx[i].getName().equals("W1")){
                //terminal station1
                BTSNodesArray_Wx[i].addDestination(BTSNodesArray_CEN[0], 1); //CEN
            }

        }

        //BTS S
        for(int i = 1; i <= 12; i++){
            if(BTSNodesArray_Sx[i].getName().equals("S1")){
                //terminal station1
                BTSNodesArray_Sx[i].addDestination(BTSNodesArray_Sx[i+1], 1);
                BTSNodesArray_Sx[i].addDestination(BTSNodesArray_CEN[0], 1); //CEN
            }
            else if(BTSNodesArray_Sx[i].getName().equals("S12")){
                //terminal station2
                BTSNodesArray_Sx[i].addDestination(BTSNodesArray_Sx[i-1], 1);
            }
            else {
                BTSNodesArray_Sx[i].addDestination(BTSNodesArray_Sx[i+1], 1);
                BTSNodesArray_Sx[i].addDestination(BTSNodesArray_Sx[i-1], 1);
            }
            //BTS S w/ transits
            if(BTSNodesArray_Sx[i].getName().equals("S2")){ // Linked with BL26
                BTSNodesArray_Sx[i].addDestination(MRTNodesArray_BLx[26],1);
            }
        }

        //BTS CEN
        BTSNodesArray_CEN[0].addDestination(BTSNodesArray_Sx[1],1);
        BTSNodesArray_CEN[0].addDestination(BTSNodesArray_Ex[1],1);
        BTSNodesArray_CEN[0].addDestination(BTSNodesArray_Nx[1],1);
        BTSNodesArray_CEN[0].addDestination(BTSNodesArray_Wx[1],1);

        //MRT BL
        for(int i = 10; i <= 28; i++){
            if(MRTNodesArray_BLx[i].getName().equals("BL10")){
                //terminal station1
                MRTNodesArray_BLx[i].addDestination(MRTNodesArray_BLx[i+1], 1);
            }
            else if(MRTNodesArray_BLx[i].getName().equals("BL28")){
                //terminal station2
                MRTNodesArray_BLx[i].addDestination(MRTNodesArray_BLx[i-1], 1);
            }
            else {
                MRTNodesArray_BLx[i].addDestination(MRTNodesArray_BLx[i+1], 1);
                MRTNodesArray_BLx[i].addDestination(MRTNodesArray_BLx[i-1], 1);
            }
            //MRT BL w/ transits
            if(MRTNodesArray_BLx[i].getName().equals("BL10")){ // Linked with PP16
                MRTNodesArray_BLx[i].addDestination(MRTNodesArray_PPx[16],1);
            }
            if(MRTNodesArray_BLx[i].getName().equals("BL13")){ // Linked with N8
                MRTNodesArray_BLx[i].addDestination(BTSNodesArray_Nx[8],1);
            }
            if(MRTNodesArray_BLx[i].getName().equals("BL21")){ // Linked with A6
                MRTNodesArray_BLx[i].addDestination(ARLNodesArray_Ax[6],1);
            }
            if(MRTNodesArray_BLx[i].getName().equals("BL22")){ // Linked with E4
                MRTNodesArray_BLx[i].addDestination(BTSNodesArray_Ex[4],1);
            }
            if(MRTNodesArray_BLx[i].getName().equals("BL26")){ // Linked with S2
                MRTNodesArray_BLx[i].addDestination(BTSNodesArray_Sx[2],1);
            }
        }

        //MRT PP
        for(int i = 1; i <= 16; i++){
            if(MRTNodesArray_PPx[i].getName().equals("PP1")){
                //terminal station1
                MRTNodesArray_PPx[i].addDestination(MRTNodesArray_PPx[i+1], 1);
            }
            else if(MRTNodesArray_PPx[i].getName().equals("PP16")){
                //terminal station2
                MRTNodesArray_PPx[i].addDestination(MRTNodesArray_PPx[i-1], 1);
            }
            else {
                MRTNodesArray_PPx[i].addDestination(MRTNodesArray_PPx[i+1], 1);
                MRTNodesArray_PPx[i].addDestination(MRTNodesArray_PPx[i-1], 1);
            }
            //MRT PP w/ transits
            if(MRTNodesArray_PPx[i].getName().equals("PP16")){ // Linked with BL10
                MRTNodesArray_PPx[i].addDestination(MRTNodesArray_BLx[10],1);
            }
        }

        //create graph and add everything in
        Graph graph = new Graph();
        for(Node node : ARLNodesArray_Ax)
            graph.addNode(node);
        for(Node node : BTSNodesArray_CEN)
            graph.addNode(node);
        for(Node node : BTSNodesArray_Nx)
            graph.addNode(node);
        for(Node node : BTSNodesArray_Ex)
            graph.addNode(node);
        for(Node node : BTSNodesArray_Wx)
            graph.addNode(node);
        for(Node node : BTSNodesArray_Sx)
            graph.addNode(node);
        for(Node node : MRTNodesArray_BLx)
            graph.addNode(node);
        for(Node node : MRTNodesArray_PPx)
            graph.addNode(node);

        //parse input
        // get only line code eg. BL10 -> BL, N2 -> N
        String originLineCode = originStationCode.replaceAll("[^A-Za-z]+", "");
        String destinationLineCode = destinationStationCode.replaceAll("[^A-Za-z]+", "");


        int originStationNumber;
        int destinationStationNumber;
        //get only number digits eg. BL10 -> 10, N2 -> 2 (for CEN, return 0)
        if(!originLineCode.equals("CEN"))
            originStationNumber = Integer.parseInt(originStationCode.replaceAll("\\D+", ""));
        else
            originStationNumber = 0;
        if(!destinationLineCode.equals("CEN"))
            destinationStationNumber = Integer.parseInt(destinationStationCode.replaceAll("\\D+", ""));
        else
            destinationStationNumber = 0;

        //perform Dijkstra on graph
        if(originLineCode.equals("A"))
            graph = calculateShortestPathFromSource(graph, ARLNodesArray_Ax[originStationNumber]);
        else if(originLineCode.equals("N"))
            graph = calculateShortestPathFromSource(graph, BTSNodesArray_Nx[originStationNumber]);
        else if(originLineCode.equals("E"))
            graph = calculateShortestPathFromSource(graph, BTSNodesArray_Ex[originStationNumber]);
        else if(originLineCode.equals("W"))
            graph = calculateShortestPathFromSource(graph, BTSNodesArray_Wx[originStationNumber]);
        else if(originLineCode.equals("S"))
            graph = calculateShortestPathFromSource(graph, BTSNodesArray_Sx[originStationNumber]);
        else if(originLineCode.equals("CEN"))
            graph = calculateShortestPathFromSource(graph, BTSNodesArray_CEN[originStationNumber]);
        else if(originLineCode.equals("BL"))
            graph = calculateShortestPathFromSource(graph, MRTNodesArray_BLx[originStationNumber]);
        else if(originLineCode.equals("PP"))
            graph = calculateShortestPathFromSource(graph, MRTNodesArray_PPx[originStationNumber]);

        List<String> shortestStations = new ArrayList<String>();

        //get the shortest path from origin to destination as a list of String
        List<Node> shortestPathOriToDes = BTSNodesArray_CEN[0].getShortestPath();

        Log.d(TAG, "getShortestDistant: destinationStationNumber: " + destinationStationNumber );
        if(destinationLineCode.equals("A"))
            shortestPathOriToDes = ARLNodesArray_Ax[destinationStationNumber].getShortestPath();
        else if(destinationLineCode.equals("N"))
            shortestPathOriToDes = BTSNodesArray_Nx[destinationStationNumber].getShortestPath();
        else if(destinationLineCode.equals("E"))
            shortestPathOriToDes = BTSNodesArray_Ex[destinationStationNumber].getShortestPath();
        else if(destinationLineCode.equals("W"))
            shortestPathOriToDes = BTSNodesArray_Wx[destinationStationNumber].getShortestPath();
        else if(destinationLineCode.equals("S"))
            shortestPathOriToDes = BTSNodesArray_Sx[destinationStationNumber].getShortestPath();
        else if(destinationLineCode.equals("CEN"))
            shortestPathOriToDes = BTSNodesArray_CEN[destinationStationNumber].getShortestPath();
        else if(destinationLineCode.equals("BL"))
            shortestPathOriToDes = MRTNodesArray_BLx[destinationStationNumber].getShortestPath();
        else if(destinationLineCode.equals("PP"))
            shortestPathOriToDes = MRTNodesArray_PPx[destinationStationNumber].getShortestPath();

        for(Node node2 : shortestPathOriToDes){
            shortestStations.add(node2.getName());
            Log.d(TAG, "getShortestDistant: Shortest path ori - des: " + node2.getName() );
        }
        shortestStations.add(destinationStationCode);
        Log.d(TAG, "getShortestDistant: Shortest path ori - des: " + destinationStationCode );

        return shortestStations;


    }

    // source: http://www.baeldung.com/java-dijkstra
    public static Graph calculateShortestPathFromSource(Graph graph, Node source) {
        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry< Node, Integer> adjacencyPair:
                    currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }



    private static Node getLowestDistanceNode(Set < Node > unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void calculateMinimumDistance(Node evaluationNode,
                                                 Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    //Calculate costs from station full names
    //return as a CostAndTransit object type, which contains the cost and number of transit as int
    private CostAndTransit getTotalCost(String originStationRaw, String destinationStationRaw){
        //Get the station code
        String[] splittedNameDes = destinationStationRaw.split("\\s+");
        String destinationStation = splittedNameDes[0];
        String[] splittedNameOri = originStationRaw.split("\\s+");
        String originStation = splittedNameOri[0];

        int numTransits = 0;
        int totalCost = 0;

        //if origin and destination are on the same line (BTS, ARL, MRT)
        if(checkIfSameLine(originStation, destinationStation)){
            totalCost = totalCost + assignGetCostFunc(originStation, destinationStation);
            if(!originStation.equals("CEN") && !destinationStation.equals("CEN")) {
                if (!getLineCode(originStation).equals(getLineCode(destinationStation))) {
                    numTransits++;
                }
            }
        }
        // if they are not the same line
        else if(!checkIfSameLine(originStation, destinationStation)){
            List<String> shortestStationPath = getShortestDistant(originStation, destinationStation);

            String previousTempStation = originStation;
            String checkPointStation = originStation;
            String prePreviousTempStation = originStation;
            for(String currentStation : shortestStationPath){
                Log.d(TAG, "getTotalCost: output Stations: "+ currentStation);
                if(!checkIfSameLine(previousTempStation, currentStation)){
                    totalCost = totalCost + assignGetCostFunc(checkPointStation, previousTempStation);
                    Log.d(TAG, "getTotalCost: Total cost1 = "+ totalCost);
                    checkPointStation = currentStation;
                    //numTransits++;
                }
                else if(currentStation.equals(destinationStation)){
                    totalCost = totalCost + assignGetCostFunc(checkPointStation, currentStation);
                    Log.d(TAG, "getTotalCost: Total cost2 = "+ totalCost);
                }

                //check if transit
                if(previousTempStation.equals("CEN") || currentStation.equals("CEN")){
                    if((getLineCode(prePreviousTempStation).equals("N") || getLineCode(prePreviousTempStation).equals("E")) &&
                            (getLineCode(currentStation).equals("W") || getLineCode(currentStation).equals("S"))){
                        numTransits++;
                    }
                    else if((getLineCode(prePreviousTempStation).equals("W") || getLineCode(prePreviousTempStation).equals("S")) &&
                            (getLineCode(currentStation).equals("N") || getLineCode(currentStation).equals("E"))){
                        numTransits++;
                    }
                }
                else if(!getLineCode(previousTempStation).equals(getLineCode(currentStation))){
                    numTransits++;
                }

                prePreviousTempStation = previousTempStation;
                previousTempStation = currentStation;
            }
        }

        Log.d(TAG, "getTotalCost: Final Cost & Transit are: "+ totalCost +" And "+numTransits);
        return new CostAndTransit(totalCost, numTransits);
    }

    //return only line code eg. BL12 -> Bl, A2 -> A.
    private String getLineCode(String station){
        return station.replaceAll("[^A-Za-z]+", "");
    }

    //compare two stations code as a string and return true if they are in the same line (BTS is NEWSCEN, MRT is PPBL, ARL is A)
    private boolean checkIfSameLine(String station1, String station2){
        String[] ARLCode = {"A"};
        String[] BTSCode = {"CEN", "N", "E", "W", "S"};
        String[] MRTCode = {"PP", "BL"};
        String station1LineCode = station1.replaceAll("[^A-Za-z]+", "");
        String station2LineCode = station2.replaceAll("[^A-Za-z]+", "");

        if((Arrays.asList(ARLCode).contains(station1LineCode) && Arrays.asList(ARLCode).contains(station2LineCode))||
                (Arrays.asList(BTSCode).contains(station1LineCode) && Arrays.asList(BTSCode).contains(station2LineCode))||
                (Arrays.asList(MRTCode).contains(station1LineCode) && Arrays.asList(MRTCode).contains(station2LineCode))) {
            return true;
        }
        else
            return false;
    }

    //both station has to be on the same line
    private int assignGetCostFunc(String originStationCode, String destinationStationCode){
        int totalCost = 0;
        // get only line code eg. BL10 -> BL, N2 -> N
        String originLineCode = originStationCode.replaceAll("[^A-Za-z]+", "");
        String destinationLineCode = destinationStationCode.replaceAll("[^A-Za-z]+", "");

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


    @Override
    public void finish() {
        super.finish();
        //exit transition animation
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
