package com.example.seamon.bangkoktransit;

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

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mTrainNames = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<String> mTrainNames) {
        this.mTrainNames = mTrainNames;
        this.mContext = context;
        
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

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mTrainNames.get(position));


                Toast.makeText(mContext , mTrainNames.get(position), Toast.LENGTH_SHORT).show();

                //open the PickStationActivity window
                Intent intent = new Intent(mContext, PickStationActivity.class);
                intent.putExtra("train_name", mTrainNames.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrainNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView trainName;
        RelativeLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            trainName = itemView.findViewById(R.id.train_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
