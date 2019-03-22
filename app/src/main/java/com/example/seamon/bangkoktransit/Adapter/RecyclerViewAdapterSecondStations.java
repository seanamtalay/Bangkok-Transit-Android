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

import com.example.seamon.bangkoktransit.Activity.TripInfoActivity;
import com.example.seamon.bangkoktransit.R;

import java.util.ArrayList;

public class RecyclerViewAdapterSecondStations extends RecyclerView.Adapter<RecyclerViewAdapterSecondStations.ViewHolder> {
    private static final String TAG = "RecylerViewAdapterStat";

    private ArrayList<String> mSecondStationNames = new ArrayList<>();
    private Context mContext;
    private String mFirstStation;
    private String mSelectedAs;
    private Activity mActivity;

    public RecyclerViewAdapterSecondStations(Context context, ArrayList<String> mStationNames, String firstStation, String selectedAs) {
        this.mSecondStationNames = mStationNames;
        this.mContext = context;
        this.mFirstStation = firstStation;
        this.mSelectedAs = selectedAs;
        this.mActivity = (Activity) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_second_stations, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.secondStationName.setText(mSecondStationNames.get(position));

        holder.parentLayoutSecondStations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mSecondStationNames.get(position));
                Intent intent = new Intent(mContext, TripInfoActivity.class);
                intent.putExtra("first_station", mFirstStation);
                intent.putExtra("second_station",mSecondStationNames.get(position));
                intent.putExtra("selected_as", mSelectedAs);
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
        return mSecondStationNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView secondStationName;
        RelativeLayout parentLayoutSecondStations;

        public ViewHolder(View itemView) {
            super(itemView);
            secondStationName = itemView.findViewById(R.id.second_station_name);
            parentLayoutSecondStations = itemView.findViewById(R.id.parent_layout_second_stations);
        }
    }
}
