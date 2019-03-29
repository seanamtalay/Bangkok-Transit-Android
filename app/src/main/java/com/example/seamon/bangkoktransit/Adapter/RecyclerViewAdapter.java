package com.example.seamon.bangkoktransit.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seamon.bangkoktransit.Activity.PickStationActivity;
import com.example.seamon.bangkoktransit.Activity.StationInfoActivity;
import com.example.seamon.bangkoktransit.R;

import java.util.ArrayList;
import java.util.Arrays;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mTrainNames = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;
    private ArrayList<String> mAllTrainsList;

    public RecyclerViewAdapter(Context context, ArrayList<String> mTrainNames) {
        this.mTrainNames = mTrainNames;
        this.mContext = context;
        this.mActivity = (Activity) context;
        this.mAllTrainsList = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.train_names_array)));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Log.d(TAG, "onBindViewHolder: called.");
        holder.trainName.setText(mTrainNames.get(position));

        changeLogo(holder,mTrainNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mTrainNames.get(position));

                //if the list is the train list then bring user to the pick station activity
                if(mTrainNames.equals(mAllTrainsList)){
                    Toast.makeText(mContext, mTrainNames.get(position), Toast.LENGTH_SHORT).show();

                    //open the PickStationActivity window
                    Intent intent = new Intent(mContext, PickStationActivity.class);
                    intent.putExtra("train_name", mTrainNames.get(position));
                    mContext.startActivity(intent);
                    mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                else{
                    //else the list contain stations (after search with keywords) then bring the user to the station info activity
                    Intent intent = new Intent(mContext, StationInfoActivity.class);
                    intent.putExtra("station_name", mTrainNames.get(position));
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrainNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView trainName;
        ImageView trainLogo;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            trainName = itemView.findViewById(R.id.train_name);
            trainLogo = itemView.findViewById(R.id.all_station_line_image);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }


    //change trainLogo according to line name.
    private void changeLogo(@NonNull ViewHolder holder, String lineOrStation){
        //if it's the list contain strains
        if(mTrainNames.equals(mAllTrainsList)) {
            if (lineOrStation.equals(holder.itemView.getContext().getResources().getString(R.string.ARL))) {
                holder.trainLogo.setImageResource(R.drawable.arl_logo);
            } else if (lineOrStation.equals(holder.itemView.getContext().getResources().getString(R.string.BTS_Sukhumvit))) {
                holder.trainLogo.setImageResource(R.drawable.bts_sukhumvit_logo);
            } else if (lineOrStation.equals(holder.itemView.getContext().getResources().getString(R.string.BTS_Silom))) {
                holder.trainLogo.setImageResource(R.drawable.bts_silom_logo);
            } else if (lineOrStation.equals(holder.itemView.getContext().getResources().getString(R.string.MRT_blue))) {
                holder.trainLogo.setImageResource(R.drawable.mrt_blue_logo);
            } else if (lineOrStation.equals(holder.itemView.getContext().getResources().getString(R.string.MRT_purple))) {
                holder.trainLogo.setImageResource(R.drawable.mrt_purple_logo);
            }
        }
        else { //else then the list contains stations
            ArrayList<String> btsSukList = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.stations_BTS_Sukhumvit_array)));
            ArrayList<String> btsSilList = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.stations_BTS_Silom_array)));
            ArrayList<String> arlList = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.stations_ARL_array)));
            ArrayList<String> mrtBlueList = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.stations_MRT_Blue_array)));
            ArrayList<String> mrtPurpleList = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.stations_MRT_Purple_array)));

            if (arlList.contains(lineOrStation)) {
                holder.trainLogo.setImageResource(R.drawable.arl_logo);
            } else if (btsSukList.contains(lineOrStation)) {
                holder.trainLogo.setImageResource(R.drawable.bts_sukhumvit_logo);
            } else if (btsSilList.contains(lineOrStation)) {
                holder.trainLogo.setImageResource(R.drawable.bts_silom_logo);
            } else if (mrtBlueList.contains(lineOrStation)) {
                holder.trainLogo.setImageResource(R.drawable.mrt_blue_logo);
            } else if (mrtPurpleList.contains(lineOrStation)) {
                holder.trainLogo.setImageResource(R.drawable.mrt_purple_logo);
            }
        }

    }
}
