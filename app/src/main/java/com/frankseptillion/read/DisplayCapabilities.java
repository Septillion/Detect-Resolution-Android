package com.frankseptillion.read;

import com.frankseptillion.readresolution.R;
import com.microsoft.device.display.DisplayMask;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.text.DecimalFormat;
import java.util.LinkedHashSet;
import java.util.Set;

public class DisplayCapabilities {

    private Context mContext;
    private WindowManager mWindowManager;
    private DisplayMetrics mDisplayMetrics;


    public DisplayCapabilities(Context context) {
        mWindowManager = ((Activity) context).getWindowManager();
        mContext = context;
        mDisplayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getRealMetrics(mDisplayMetrics);
    }

    // Surface Duo Specific
    public boolean isDualScreenDevice() {
        return mContext.getPackageManager().hasSystemFeature("com.microsoft.device.display.displaymask");
    }

    // Surface Duo Specific
    public boolean isAppSpanned() {
            return isDualScreenDevice() && !DisplayMask.fromResourcesRectApproximation(mContext).getBoundingRects().isEmpty();
    }

    public String getModelNumber() {
        String mModelNumber;
        try {
            if (BluetoothAdapter.getDefaultAdapter().getName() != null) {
                mModelNumber = BluetoothAdapter.getDefaultAdapter().getName();
            } else {
                mModelNumber = Build.MODEL;
            }
        } catch (Exception e) {
            mModelNumber = Build.MODEL;
        }
        if (isAppSpanned()) {
            mModelNumber += " Spanned";
        }
        return mModelNumber;
    }

    public String getSupportedResolutions() {
        String allSupportedResolutions = "";
        Set<Integer> supportedResolutions = new LinkedHashSet<Integer>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Display.Mode[] modes = mWindowManager.getDefaultDisplay().getSupportedModes();
            for (Display.Mode mode : modes) {
                supportedResolutions.add(mode.getPhysicalWidth());
            }
            if (supportedResolutions.size() > 1) {
                for (int i : supportedResolutions) {
                    if (!allSupportedResolutions.equals("")) {
                        allSupportedResolutions += ", ";
                    }
                    allSupportedResolutions = allSupportedResolutions + i + "p";
                }
            }

            if (!allSupportedResolutions.equals("")) {
                allSupportedResolutions = " (" + allSupportedResolutions + ")";
            }
        }
        return allSupportedResolutions;
    }

    public String getSupportedRefreshRates() {
        String allSupportedRefreshRates = "";
        Set<Float> supportedRefreshRates = new LinkedHashSet<Float>();
        DecimalFormat df = new DecimalFormat("###.###");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Display.Mode[] modes = mWindowManager.getDefaultDisplay().getSupportedModes();
            for (Display.Mode mode : modes) {
                supportedRefreshRates.add(mode.getRefreshRate());
            }
            if (supportedRefreshRates.size() > 1) {
                for (float i : supportedRefreshRates) {
                    if (!allSupportedRefreshRates.equals("")) {
                        allSupportedRefreshRates += ", ";
                    }
                    allSupportedRefreshRates = allSupportedRefreshRates + df.format(i);
                }
            }
            if (!allSupportedRefreshRates.equals("")) {
                allSupportedRefreshRates = " (" + allSupportedRefreshRates + ")";
            }
        }
        return allSupportedRefreshRates;
    }

    public boolean isHDR() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int[] HDRcapabilities = mWindowManager.getDefaultDisplay().getHdrCapabilities().getSupportedHdrTypes();
            return HDRcapabilities.length > 0;
        } else {
            return false;
        }
    }

    public String getHdrCapabilities() {
        StringBuilder isHdr = new StringBuilder("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int[] HDRcapabilities = mWindowManager.getDefaultDisplay().getHdrCapabilities().getSupportedHdrTypes();
            if (HDRcapabilities.length > 0) {
                StringBuilder HDRenum = new StringBuilder();
                int HDR10flag = 0;
                for (int i : HDRcapabilities) {
                    switch (i) {
                        case Display.HdrCapabilities.HDR_TYPE_HLG:
                            HDRenum.append(" HLG");
                            break;
                        case Display.HdrCapabilities.HDR_TYPE_HDR10:
                            if (HDR10flag < 1){
                                HDR10flag = 1;
                            }
                            HDRenum.append(" HDR10");
                            break;
                        case Display.HdrCapabilities.HDR_TYPE_HDR10_PLUS:
                            if (HDR10flag < 2){
                                HDR10flag = 2;
                            }
                            HDRenum.append(" HDR10+");
                            break;
                        case Display.HdrCapabilities.HDR_TYPE_DOLBY_VISION:
                            if (HDR10flag < 3){
                                HDR10flag = 3;
                            }
                            HDRenum.append(" DolbyVision");
                            break;
                        default:
                            HDRenum = new StringBuilder(mContext.getString(R.string.notSupported));
                    }
                }
                isHdr.append(HDRenum.toString());
            } else {
                isHdr.append(mContext.getString(R.string.notSupported));
            }
        }
        return isHdr.toString();
    }

    public boolean isWideColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return mContext.getResources().getConfiguration().isScreenWideColorGamut();
        } else {
            return false;
        }
    }

    public String getWideColorSupport() {
        if (isWideColor()) {
            return mContext.getString(R.string.supported);
        } else {
            return mContext.getString(R.string.notSupported);
        }
    }

    public String getDimensionsInDp() {
        return (int) Math.ceil(mDisplayMetrics.widthPixels / mDisplayMetrics.density) + " × " + (int) Math.ceil(mDisplayMetrics.heightPixels / mDisplayMetrics.density);
    }

    public String getResolutionsInPixels() {
        return mDisplayMetrics.widthPixels + " × " + mDisplayMetrics.heightPixels;
    }

    public String getAspectRatio() {
        return " (" + new DecimalFormat("#.#").format((float)mDisplayMetrics.heightPixels / (float)mDisplayMetrics.widthPixels * 9) + ":9)";
    }

    public int getRefreshRate() {
        return (int) mWindowManager.getDefaultDisplay().getRefreshRate();
    }

    public float getScaleFactor() {
        return mDisplayMetrics.density;
    }

    private ActivityManager.MemoryInfo getAvailableMemory(){
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    public String getMemoryInfo(){
        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();
        String isLow = "";
        if (memoryInfo.lowMemory){
            isLow = " (Low Memory!)";
        }
        return new DecimalFormat("#.##").format(Long.valueOf(memoryInfo.availMem).doubleValue()/1024/1024/1024)
                + "/"
                + new DecimalFormat("#.##").format(Long.valueOf(memoryInfo.totalMem).doubleValue()/1024/1024/1024)
                + "GB"
                + isLow;
    }

    public String getDPI(){
        return "("+ (int)mDisplayMetrics.xdpi +"ppi)";
    }

}
