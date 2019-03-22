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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.seamon.bangkoktransit.Activity.StationInfoActivity;
import com.example.seamon.bangkoktransit.R;

import java.util.ArrayList;

public class RecyclerViewAdapterStations extends RecyclerView.Adapter<RecyclerViewAdapterStations.ViewHolder> {
    private static final String TAG = "RecylerViewAdapterStat";

    private ArrayList<String> mStationNames = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;

    public RecyclerViewAdapterStations(Context context, ArrayList<String> mStationNames) {
        this.mStationNames = mStationNames;
        this.mContext = context;
        this.mActivity = (Activity) context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_stations, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.stationName.setText(mStationNames.get(position));
        holder.parentLayoutStations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mStationNames.get(position));
                Intent intent = new Intent(mContext, StationInfoActivity.class);
                intent.putExtra("station_name", mStationNames.get(position));
                mContext.startActivity(intent);

                //call finish function so that when user press back on the stationInfo Activity, it will skip select station activity and jump back to select line activity
                mActivity.finish();

                //transition animation
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mStationNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView stationName;
        RelativeLayout parentLayoutStations;

        public ViewHolder(View itemView) {
            super(itemView);
            stationName = itemView.findViewById(R.id.station_name);
            parentLayoutStations = itemView.findViewById(R.id.parent_layout_stations);
        }
    }
}
