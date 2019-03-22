package com.example.seamon.bangkoktransit.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.seamon.bangkoktransit.Activity.ZoomTransitMapActivity;
import com.example.seamon.bangkoktransit.R;

public class Tab3Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3fragment, container, false);
        ImageView transitMap = rootView.findViewById(R.id.transit_map);
        Button ARLTimeButton = rootView.findViewById(R.id.ARL_time_button);
        Button BTSTimeButton = rootView.findViewById(R.id.BTS_time_button);
        Button MRTTimeButton = rootView.findViewById(R.id.MRT_time_button);

        transitMap.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ZoomTransitMapActivity.class);
                getActivity().startActivity(intent);


            }
        });

        //pop up message showing time schedules
        ARLTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewPopup = LayoutInflater.from(getActivity()).inflate(R.layout.layout_time_schedules_info_popup, null);

                initTable(viewPopup, "ARLSchedules");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Time Schedules")
                        .setView(viewPopup);
                AlertDialog timeScheduleAlert = builder.create();
                timeScheduleAlert.show();

            }
        });

        BTSTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View viewPopup = LayoutInflater.from(getActivity()).inflate(R.layout.layout_time_schedules_info_popup, null);

                initTable(viewPopup, "BTSSchedules");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Time Schedules")
                        .setView(viewPopup);
                AlertDialog timeScheduleAlert = builder.create();
                timeScheduleAlert.show();

            }
        });

        MRTTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewPopup = LayoutInflater.from(getActivity()).inflate(R.layout.layout_time_schedules_info_popup, null);

                initTable(viewPopup, "MRTSchedules");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Time Schedules")
                        .setView(viewPopup);
                AlertDialog timeScheduleAlert = builder.create();
                timeScheduleAlert.show();

            }
        });
        return rootView;
    }



    public void initTable(View rootView, String trainArrayName) {
        TableLayout stk = (TableLayout) rootView.findViewById(R.id.table_main);
        TableRow tbRow0 = new TableRow(getContext());
        TextView hourLabel = new TextView(getContext());
        hourLabel.setText("Hours");
        hourLabel.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
        hourLabel.setTextColor(Color.argb(200, 0, 0, 0));
        hourLabel.setGravity(Gravity.CENTER_HORIZONTAL);
        hourLabel.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tbRow0.addView(hourLabel);
        TextView frequencyLabel = new TextView(getContext());
        frequencyLabel.setText("Frequency");
        frequencyLabel.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
        frequencyLabel.setTextColor(Color.argb(200, 0, 0, 0));
        frequencyLabel.setGravity(Gravity.CENTER_HORIZONTAL);
        frequencyLabel.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tbRow0.addView(frequencyLabel);

        TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams();
        tableRowParams.setMargins(0,20,0,20);
        tbRow0.setLayoutParams(tableRowParams);

        stk.addView(tbRow0);

        //get array from resource file
        int resId = this.getResources().getIdentifier(trainArrayName,"array",getContext().getPackageName()); // might cause a bug (return 0)
        String[] infoArray = getResources().getStringArray(resId);

        for (int i = 0; i < infoArray.length; i+=2) {
            TableRow tbRow = new TableRow(getContext());
            TextView t1v = new TextView(getContext());
            t1v.setText(infoArray[i]);
            t1v.setTextColor(Color.argb(161, 0, 0, 0));
            t1v.setGravity(Gravity.CENTER_HORIZONTAL);
            t1v.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tbRow.addView(t1v);
            TextView t2v = new TextView(getContext());
            t2v.setText(infoArray[i+1]);
            t2v.setTextColor(Color.argb(161, 0, 0, 0));
            t2v.setGravity(Gravity.CENTER);
            t2v.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tbRow.addView(t2v);

            TableLayout.LayoutParams tableRowParamsSub = new TableLayout.LayoutParams();
            tableRowParamsSub.setMargins(0,12,0,12);
            tbRow.setLayoutParams(tableRowParamsSub);

            stk.addView(tbRow);
        }

    }
}