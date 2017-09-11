package com.aleksandr.nikitin.pretty_owls_wallpaper;

import android.util.DisplayMetrics;

public class DisplayInfo {
    private static final int WIDTH_MEDIUM = 360;
    private static final int HEIGHT_MEDIUM = 640;

    private static final int WIDTH_HIGH = 540;
    private static final int HEIGHT_HIGH = 960;

    private static final int WIDTH_XHIGH = 720;
    private static final int HEIGHT_XHIGH = 1280;

    private static final int WIDTH_XXHIGH = 1080;
    private static final int HEIGHT_XXHIGH = 1920;

    static boolean isCorrespondsToTheDensityResolution(int width, int height) {
        int tempXdpi, tempYdpi;
        switch(width) {
            case WIDTH_MEDIUM:
                tempXdpi = DisplayMetrics.DENSITY_MEDIUM;
                break;
            case WIDTH_HIGH:
                tempXdpi = DisplayMetrics.DENSITY_HIGH;
                break;
            case WIDTH_XHIGH:
                tempXdpi = DisplayMetrics.DENSITY_XHIGH;
                break;
            case WIDTH_XXHIGH:
                tempXdpi = DisplayMetrics.DENSITY_XXHIGH;
                break;
            default:
                tempXdpi = -1;
        }
        switch(height) {
            case HEIGHT_MEDIUM:
                tempYdpi = DisplayMetrics.DENSITY_MEDIUM;
                break;
            case HEIGHT_HIGH:
                tempYdpi = DisplayMetrics.DENSITY_HIGH;
                break;
            case HEIGHT_XHIGH:
                tempYdpi = DisplayMetrics.DENSITY_XHIGH;
                break;
            case HEIGHT_XXHIGH:
                tempYdpi = DisplayMetrics.DENSITY_XXHIGH;
                break;
            default:
                tempYdpi = -2;
        }

        return tempXdpi == tempYdpi;
    }

}
