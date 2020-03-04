package com.frankseptillion.readresolution;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing View
        TextView model = findViewById(R.id.idModel);
        TextView resolution = findViewById(R.id.idResolution);
        TextView dimensions = findViewById(R.id.idDemensions);
        TextView density = findViewById(R.id.idDesity);
        TextView isHDR = findViewById(R.id.idIsHDR);
        TextView refreshRate = findViewById(R.id.refreshRate);


        // Get Display Information
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        StringBuilder isHdr = new StringBuilder("");

        // Get Device Name
        String modelNumber2;
        if(BluetoothAdapter.getDefaultAdapter().getName()!=null){
            modelNumber2 = BluetoothAdapter.getDefaultAdapter().getName();
        }else{
            modelNumber2 = Build.MODEL;
        }


        // Get isHDR
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (display.isHdr()){
                isHdr = "Yes";
            }else{
                isHdr = "No";
            }
        }else {
            isHdr = "No";
        }
        */

        // Get HDR Capabilities
            int[] HDRcapabilities = display.getHdrCapabilities().getSupportedHdrTypes();
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
                        case 4:
                            HDR10flag = 2;
                            break;
                        case Display.HdrCapabilities.HDR_TYPE_DOLBY_VISION:
                            HDR10flag = 3;
                            break;
                        default:
                            HDRenum = new StringBuilder("No");
                    }
                }
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
                isHdr.append("No");
            }



        // Set Display Information
        model.setText(modelNumber2);
        resolution.setText(dm.widthPixels + " x " + dm.heightPixels);
        dimensions.setText((int)Math.ceil(dm.widthPixels/dm.density) + " x " + (int)Math.ceil(dm.heightPixels/dm.density));
        density.setText(dm.density+"x");
        isHDR.setText(isHdr.toString());
        refreshRate.setText((int)display.getRefreshRate()+"Hz");
    }

}
