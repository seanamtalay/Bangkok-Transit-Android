package com.example.seamon.bangkoktransit.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.seamon.bangkoktransit.Activity.StationInfoActivity;
import com.example.seamon.bangkoktransit.R;

import java.util.ArrayList;

import static com.example.seamon.bangkoktransit.Utils.UtilsKt.getEta;
import static com.example.seamon.bangkoktransit.Utils.UtilsKt.getTrainType;


public class EtaStationRecyclerAdapter extends RecyclerView.Adapter<EtaStationRecyclerAdapter.ViewHolder> {
    private static final String TAG = EtaStationRecyclerAdapter.class.getSimpleName();

    private ArrayList<String> mStationNames = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;

    public EtaStationRecyclerAdapter(Context context, ArrayList<String> mStationNames) {
        this.mStationNames = mStationNames;
        this.mContext = context;
        this.mActivity = (Activity) context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_nearby_eta_stations, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.stationName.setText(mStationNames.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
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


        if(mContext.getResources().getString(R.string.BTS_Silom).equals(mStationNames.get(position))){
            holder.trainLogo.setImageResource(R.drawable.bts_silom_logo);
        }
        else if(mContext.getResources().getString(R.string.BTS_Sukhumvit).equals(mStationNames.get(position))){
            holder.trainLogo.setImageResource(R.drawable.bts_sukhumvit_logo);
        }
        else if(mContext.getResources().getString(R.string.ARL).equals(mStationNames.get(position))){
            holder.trainLogo.setImageResource(R.drawable.arl_logo);
        }
        else if(mContext.getResources().getString(R.string.MRT_blue).equals(mStationNames.get(position))){
            holder.trainLogo.setImageResource(R.drawable.mrt_blue_logo);
        }
        else if(mContext.getResources().getString(R.string.MRT_purple).equals(mStationNames.get(position))){
            holder.trainLogo.setImageResource(R.drawable.mrt_purple_logo);
        }

        holder.etaTimeText.setText(getEta(mContext, mStationNames.get(position)));

    }

    @Override
    public int getItemCount() {
        return mStationNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView trainLogo;
        TextView stationName;
        ConstraintLayout layout;
        TextView etaTimeText;

        public ViewHolder(View itemView) {
            super(itemView);
            trainLogo = itemView.findViewById(R.id.nearby_eta_line_image);
            stationName = itemView.findViewById(R.id.nearby_station_name);
            layout = itemView.findViewById(R.id.nearby_eta_layout);
            etaTimeText = itemView.findViewById(R.id.nearby_eta_time_text);
        }
    }
}

