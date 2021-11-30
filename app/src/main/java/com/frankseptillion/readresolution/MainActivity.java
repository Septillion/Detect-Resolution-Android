package com.frankseptillion.readresolution;

import android.annotation.SuppressLint;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.os.Bundle;
import android.widget.TextView;

import com.frankseptillion.read.DisplayCapabilities;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        DisplayCapabilities dc = new DisplayCapabilities(this);

        setContentView(R.layout.activity_main);

        // Use Dual Screen Layout if is on a Surface Duo.
        if (dc.isAppSpanned()) {
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setContentView(R.layout.activity_main_horizontal_dual_screen);
            }
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                setContentView(R.layout.activity_main_vertical_dual_screen);
            }
        }

        // Initializing View
        LinearLayoutCompat bg = findViewById(R.id.background);
        LinearLayoutCompat horizontalGap = findViewById(R.id.horizontalGap);
        TextView model = findViewById(R.id.idModel);
        TextView resolution = findViewById(R.id.idResolution);
        TextView dimensions = findViewById(R.id.idDemensions);
        TextView density = findViewById(R.id.idDesity);
        TextView isHDR = findViewById(R.id.idIsHDR);
        TextView isWideColor = findViewById(R.id.isWideColor);
        TextView refreshRate = findViewById(R.id.refreshRate);
        TextView memoryStatus = findViewById(R.id.memoryStatus);

        // Set Display Information
        model.setText(dc.getModelNumber());
        resolution.setText(dc.getResolutionsInPixels() + dc.getSupportedResolutions() + dc.getAspectRatio() + dc.getDPI() );
        dimensions.setText(dc.getDimensionsInDp());
        density.setText(dc.getScaleFactor() + "x");
        isHDR.setText(dc.getHdrCapabilities());
        isWideColor.setText(dc.getWideColorSupport());
        refreshRate.setText(dc.getRefreshRate() + dc.getSupportedRefreshRates());
        memoryStatus.setText(dc.getMemoryInfo());

    }
}

