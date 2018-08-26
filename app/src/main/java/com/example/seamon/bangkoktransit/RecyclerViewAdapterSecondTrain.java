package com.example.seamon.bangkoktransit;

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

import java.util.ArrayList;

public class RecyclerViewAdapterSecondTrain extends RecyclerView.Adapter<RecyclerViewAdapterSecondTrain.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapterSeco";


    private ArrayList<String> mSecondTrainNames = new ArrayList<>();
    private Context mContext;
    private String mFirstStation;
    private String mSelectedAs;
    private Activity mActivity;

    public RecyclerViewAdapterSecondTrain(Context context, ArrayList<String> mTrainNames, String firstStation, String selectedAs) {
        this.mSecondTrainNames = mTrainNames;
        this.mContext = context;
        this.mFirstStation = firstStation;
        this.mSelectedAs = selectedAs;
        this.mActivity = (Activity) context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_second_train, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.secondTrainName.setText(mSecondTrainNames.get(position));

        changeLogo(holder,mSecondTrainNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mSecondTrainNames.get(position));
                Intent intent = new Intent(mContext, PickSecondStationActivity.class);
                intent.putExtra("second_station_name", mSecondTrainNames.get(position));
                intent.putExtra("first_station_name", mFirstStation);
                intent.putExtra("selected_as", mSelectedAs);
                mContext.startActivity(intent);

                //transition animation
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSecondTrainNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView secondTrainName;
        TextView secondTrainLogo;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            secondTrainName = itemView.findViewById(R.id.second_train_name);
            parentLayout = itemView.findViewById(R.id.parent_layout_second_train);
            secondTrainLogo = itemView.findViewById(R.id.second_train_logo);
        }
    }

    //change trainLogo according to line name.
    private void changeLogo(@NonNull ViewHolder holder, String line){
        if(line.equals(holder.itemView.getContext().getResources().getString(R.string.ARL))){
            holder.secondTrainLogo.setText("  ARL  ");
            holder.secondTrainLogo.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.ARLRed));
            holder.secondTrainLogo.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }
        else if(line.equals(holder.itemView.getContext().getResources().getString(R.string.BTS_Sukhumvit))){
            holder.secondTrainLogo.setText("  BTS  ");
            holder.secondTrainLogo.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.SukhumvitGreen));
            holder.secondTrainLogo.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
        }
        else if(line.equals(holder.itemView.getContext().getResources().getString(R.string.BTS_Silom))){
            holder.secondTrainLogo.setText("  BTS  ");
            holder.secondTrainLogo.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.SilomGreen));
            holder.secondTrainLogo.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }
        else if(line.equals(holder.itemView.getContext().getResources().getString(R.string.MRT_blue))){
            holder.secondTrainLogo.setText("  MRT  ");
            holder.secondTrainLogo.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.MRTBlue));
            holder.secondTrainLogo.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }
        else if(line.equals(holder.itemView.getContext().getResources().getString(R.string.MRT_purple))){
            holder.secondTrainLogo.setText("  MRT  ");
            holder.secondTrainLogo.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.MRTPurple));
            holder.secondTrainLogo.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
        }

    }

}
