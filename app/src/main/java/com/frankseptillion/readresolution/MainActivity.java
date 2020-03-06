package com.frankseptillion.readresolution;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView model, resolution, dimensions, density, isHDR, isWideColor, refreshRate;
    private String modelNumber2, StringIsWideColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing View
        model = findViewById(R.id.idModel);
        resolution = findViewById(R.id.idResolution);
        dimensions = findViewById(R.id.idDemensions);
        density = findViewById(R.id.idDesity);
        isHDR = findViewById(R.id.idIsHDR);
        isWideColor = findViewById(R.id.isWideColor);
        refreshRate = findViewById(R.id.refreshRate);

    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();


        // Get Display Information
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        StringBuilder isHdr = new StringBuilder("");

        // Get Device Name
        try {
            if(BluetoothAdapter.getDefaultAdapter().getName()!=null){
                modelNumber2 = BluetoothAdapter.getDefaultAdapter().getName();
            }else{
                modelNumber2 = Build.MODEL;
            }
        }catch (Exception e){
            modelNumber2 = Build.MODEL;
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
            switch (HDR10flag){
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
            if (getResources().getConfiguration().isScreenWideColorGamut()){
                StringIsWideColor = getString(R.string.supported);
            }else {
                StringIsWideColor = getString(R.string.notSupported);
            }
        }else {
            StringIsWideColor = getString(R.string.notSupported);
        }

        // Set Display Information
        model.setText(modelNumber2);
        resolution.setText(dm.widthPixels + " × " + dm.heightPixels);
        dimensions.setText((int)Math.ceil(dm.widthPixels/dm.density) + " × " + (int)Math.ceil(dm.heightPixels/dm.density));
        density.setText(dm.density+"x");
        isHDR.setText(isHdr.toString());
        isWideColor.setText(StringIsWideColor);
        refreshRate.setText((int)display.getRefreshRate()+"Hz");

    }
}
