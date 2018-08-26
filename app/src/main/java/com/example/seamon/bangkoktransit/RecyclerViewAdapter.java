package com.example.seamon.bangkoktransit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mTrainNames = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;

    public RecyclerViewAdapter(Context context, ArrayList<String> mTrainNames) {
        this.mTrainNames = mTrainNames;
        this.mContext = context;
        this.mActivity = (Activity) context;
        
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
        Log.d(TAG, "onBindViewHolder: called.");
        holder.trainName.setText(mTrainNames.get(position));

        changeLogo(holder,mTrainNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mTrainNames.get(position));


                Toast.makeText(mContext , mTrainNames.get(position), Toast.LENGTH_SHORT).show();

                //open the PickStationActivity window
                Intent intent = new Intent(mContext, PickStationActivity.class);
                intent.putExtra("train_name", mTrainNames.get(position));
                mContext.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrainNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView trainName;
        TextView trainLogo;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            trainName = itemView.findViewById(R.id.train_name);
            trainLogo = itemView.findViewById(R.id.train_logo);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }


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
}
