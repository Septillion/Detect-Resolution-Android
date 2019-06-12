package com.frankseptillion.readresolution;

import android.graphics.Point;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing View
        TextView model = findViewById(R.id.idModel);
        TextView resolution = findViewById(R.id.idResolution);
        TextView dementions = findViewById(R.id.idDemensions);
        TextView density = findViewById(R.id.idDesity);
        TextView isHDR = findViewById(R.id.idIsHDR);
        //TextView hdrcap = findViewById(R.id.idHDRcap);


        // Get Display Information
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        //int[] hdrCapEnum = display.getHdrCapabilities().getSupportedHdrTypes();
        //String hdrCapabilities = new String();
        String isHdr = new String();
        String modelNumber = Build.MODEL;


        /*
        for (int i:hdrCapEnum
             ) {
            switch (i) {
                case 1:
                    hdrCapabilities = hdrCapabilities + "Dolby Vision ";
                    break;
                case 2:
                    hdrCapabilities = hdrCapabilities + "HDR10 ";
                    break;
                case 3:
                    hdrCapabilities = hdrCapabilities + "HDR10+ ";
                    break;
                case 4:
                    hdrCapabilities = hdrCapabilities + "HLG ";
                    break;
            }
        }
        */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (display.isHdr()){
                isHdr = "Yes";
            }else{
                isHdr = "No";
            }
        }else {
            isHdr = "No";
        }


        // Set Display Information
        model.setText(modelNumber);
        resolution.setText(dm.widthPixels + " x " + dm.heightPixels);
        dementions.setText((int)Math.ceil(dm.widthPixels/dm.density) + " x " + (int)Math.ceil(dm.heightPixels/dm.density));
        density.setText(Integer.toString(dm.densityDpi));
        isHDR.setText(isHdr);
        //hdrcap.setText(hdrCapabilities);
    }

}
