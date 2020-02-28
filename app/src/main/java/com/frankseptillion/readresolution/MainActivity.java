package com.frankseptillion.readresolution;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;

//import com.jaredrummler.android.device.DeviceName;

public class MainActivity extends AppCompatActivity {

    private TextView model;
    private String modelNumber2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing View
        model = findViewById(R.id.idModel);
        TextView resolution = findViewById(R.id.idResolution);
        TextView dementions = findViewById(R.id.idDemensions);
        TextView density = findViewById(R.id.idDesity);
        TextView isHDR = findViewById(R.id.idIsHDR);


        // Get Display Information
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        String isHdr = new String();
        //String modelNumber = Build.MODEL;

        //String modelNumber1 = DeviceName.getDeviceName();

        if(BluetoothAdapter.getDefaultAdapter().getName()!=null){
            modelNumber2 = BluetoothAdapter.getDefaultAdapter().getName();
        }else{
            modelNumber2 = Build.MODEL;
        }


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
        model.setText(modelNumber2);
        resolution.setText(dm.widthPixels + " x " + dm.heightPixels);
        dementions.setText((int)Math.ceil(dm.widthPixels/dm.density) + " x " + (int)Math.ceil(dm.heightPixels/dm.density));
        density.setText(Integer.toString(dm.densityDpi));
        isHDR.setText(isHdr);
    }

}
