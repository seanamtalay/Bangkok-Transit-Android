package com.example.seamon.bangkoktransit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.seamon.bangkoktransit.R;
import com.github.chrisbanes.photoview.PhotoView;

public class ZoomTransitMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_transit_map);
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view_transit_map);
        photoView.setImageResource(R.drawable.bkk_transit_map_2017_small);

    }
}
