package com.frankseptillion.readresolution;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.microsoft.device.display.DisplayMask;

import java.text.DecimalFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    private TextView model, resolution, dimensions, density, isHDR, isWideColor, refreshRate;
    private String modelNumber2, StringIsWideColor;

    private boolean isDualScreenDevice() {
        String feature = "com.microsoft.device.display.displaymask";
        PackageManager pm = this.getPackageManager();

        if (pm.hasSystemFeature(feature)) {
            Log.i(TAG, "System has feature: " + feature);
            return true;
        } else {
            Log.w(TAG, "System missing feature: " + feature);
            return false;
        }
    }

    private boolean isAppSpanned() {
        DisplayMask displayMask = DisplayMask.fromResourcesRectApproximation(this);
        Region region = displayMask.getBounds();
        List<Rect> boundings = displayMask.getBoundingRects();
        if (!boundings.isEmpty()) {
            Rect first = boundings.get(0);
            View rootView = this.getWindow().getDecorView().getRootView();
            Rect drawingRect = new Rect();
            rootView.getDrawingRect(drawingRect);
            if (first.intersect(drawingRect)) {
                Log.d(TAG, "Dual screen - intersect: " + drawingRect);
                return true;
            } else {
                Log.d(TAG, "Single screen - not intersect: " + drawingRect);
                return false;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);

        // Initializing View
        model = findViewById(R.id.idModel);
        resolution = findViewById(R.id.idResolution);
        dimensions = findViewById(R.id.idDemensions);
        density = findViewById(R.id.idDesity);
        isHDR = findViewById(R.id.idIsHDR);
        isWideColor = findViewById(R.id.isWideColor);
        refreshRate = findViewById(R.id.refreshRate);

        // Get Display Information
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        StringBuilder isHdr = new StringBuilder("");

        // Get Device Name
        try {
            if (BluetoothAdapter.getDefaultAdapter().getName() != null) {
                modelNumber2 = BluetoothAdapter.getDefaultAdapter().getName();
            } else {
                modelNumber2 = Build.MODEL;
            }
        } catch (Exception e) {
            modelNumber2 = Build.MODEL;
        }

        if (isDualScreenDevice()) {
            modelNumber2 = "Microsoft Surface Duo";
        }

        // Get All Supported Modes
        String allSupportedResolutions = "";
        String allSupportedRefreshRates = "";
        Set<Integer> supportedResolutions = new LinkedHashSet<Integer>();
        Set<Float> supportedRefreshRates = new LinkedHashSet<Float>();
        DecimalFormat df = new DecimalFormat("###.###");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Display.Mode[] modes = display.getSupportedModes();
            for (Display.Mode mode : modes) {
                supportedResolutions.add(mode.getPhysicalWidth());
                supportedRefreshRates.add(mode.getRefreshRate());
            }
            if (supportedResolutions.size() > 1) {
                for (int i : supportedResolutions) {
                    if (!allSupportedResolutions.equals("")) {
                        allSupportedResolutions += ", ";
                    }
                    allSupportedResolutions = allSupportedResolutions + i + "p";
                }
            }
            if (supportedRefreshRates.size() > 1) {
                for (float i : supportedRefreshRates) {
                    if (!allSupportedRefreshRates.equals("")) {
                        allSupportedRefreshRates += ", ";
                    }
                    allSupportedRefreshRates = allSupportedRefreshRates + df.format(i);
                }
            }
            if (!allSupportedResolutions.equals("")) {
                allSupportedResolutions = " (" + allSupportedResolutions + ")";
            }
            if (!allSupportedRefreshRates.equals("")) {
                allSupportedRefreshRates = " (" + allSupportedRefreshRates + ")";
            }
        }

        // Get HDR Capabilities
        int[] HDRcapabilities = new int[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            HDRcapabilities = display.getHdrCapabilities().getSupportedHdrTypes();
        }
        if (HDRcapabilities.length > 0) {
            StringBuilder HDRenum = new StringBuilder();
            int HDR10flag = 0;
            for (int i : HDRcapabilities) {
                switch (i) {
                    case Display.HdrCapabilities.HDR_TYPE_HLG:
                        HDRenum.append("HLG, ");
                        break;
                    case Display.HdrCapabilities.HDR_TYPE_HDR10:
                        HDR10flag = 1;
                        break;
                    case Display.HdrCapabilities.HDR_TYPE_HDR10_PLUS:
                        HDR10flag = 2;
                        break;
                    case Display.HdrCapabilities.HDR_TYPE_DOLBY_VISION:
                        HDR10flag = 3;
                        break;
                    default:
                        HDRenum = new StringBuilder(getString(R.string.notSupported));
                }
            }
            // Only show highest HDR standard.
            switch (HDR10flag) {
                case 1:
                    HDRenum.append("HDR10");
                    break;
                case 2:
                    HDRenum.append("HDR10+");
                    break;
                case 3:
                    HDRenum.append("Dolby Vision");
                    break;
                default:
                    break;
            }
            isHdr.append(HDRenum.toString());
        } else {
            isHdr.append(getString(R.string.notSupported));
        }

        // Get Wide Color Gamut
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (getResources().getConfiguration().isScreenWideColorGamut()) {
                StringIsWideColor = getString(R.string.supported);
            } else {
                StringIsWideColor = getString(R.string.notSupported);
            }
        } else {
            StringIsWideColor = getString(R.string.notSupported);
        }


        // Set Display Information
        model.setText(modelNumber2);
        resolution.setText(dm.widthPixels + " × " + dm.heightPixels + allSupportedResolutions);
        dimensions.setText((int) Math.ceil(dm.widthPixels / dm.density) + " × " + (int) Math.ceil(dm.heightPixels / dm.density));
        density.setText(dm.density + "x");
        isHDR.setText(isHdr.toString());
        isWideColor.setText(StringIsWideColor);
        refreshRate.setText((int) display.getRefreshRate() + "Hz" + allSupportedRefreshRates);

    }
}

