package com.example.seamon.bangkoktransit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seamon.bangkoktransit.Activity.StationInfoActivity;
import com.example.seamon.bangkoktransit.R;

import java.util.ArrayList;

public class RecyclerViewAdapterNearByStationsTab2 extends RecyclerView.Adapter<RecyclerViewAdapterNearByStationsTab2.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mNearbyStationNames = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapterNearByStationsTab2(Context context, ArrayList<String> mTrainNames) {
        this.mNearbyStationNames = mTrainNames;
        this.mContext = context;
        
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_nearby_station_tab2, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Log.d(TAG, "onBindViewHolder: called.");
        holder.nearbyStationName.setText(mNearbyStationNames.get(position));

        //changeLogo(holder,mTrainNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mNearbyStationNames.get(position));


                Toast.makeText(mContext , mNearbyStationNames.get(position), Toast.LENGTH_SHORT).show();

                //open the stationInfo window
                Intent intent = new Intent(mContext, StationInfoActivity.class);
                intent.putExtra("station_name", mNearbyStationNames.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNearbyStationNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nearbyStationName;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            nearbyStationName = itemView.findViewById(R.id.nearby_station_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }


    /*
    //change trainLogo according to line name.
    private void changeLogo(@NonNull ViewHolder holder, String line){
        if(line.equals(holder.itemView.getContext().getResources().getString(R.string.ARL))){
            holder.trainLogo.setText("  ARL  ");
            holder.trainLogo.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.ARLRed));
            holder.trainLogo.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }
        else if(line.equals(holder.itemView.getContext().getResources().getString(R.string.BTS_Sukhumvit))){
            holder.trainLogo.setText("  BTS  ");
            holder.trainLogo.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.SukhumvitGreen));
            holder.trainLogo.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
        }
        else if(line.equals(holder.itemView.getContext().getResources().getString(R.string.BTS_Silom))){
            holder.trainLogo.setText("  BTS  ");
            holder.trainLogo.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.SilomGreen));
            holder.trainLogo.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }
        else if(line.equals(holder.itemView.getContext().getResources().getString(R.string.MRT_blue))){
            holder.trainLogo.setText("  MRT  ");
            holder.trainLogo.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.MRTBlue));
            holder.trainLogo.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }
        else if(line.equals(holder.itemView.getContext().getResources().getString(R.string.MRT_purple))){
            holder.trainLogo.setText("  MRT  ");
            holder.trainLogo.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.MRTPurple));
            holder.trainLogo.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }

    }
    */
}
