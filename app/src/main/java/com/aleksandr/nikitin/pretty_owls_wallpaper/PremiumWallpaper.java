package com.aleksandr.nikitin.pretty_owls_wallpaper;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.HashMap;
import java.util.Map;

public class PremiumWallpaper {

    private SharedPreferences sPref;

    public static final int NOT_PREMIUM_WALLPAPER = 0;
    public static final int OPENED_PREMIUM_WALLPAPER = 1;
    public static final int CLOSED_PREMIUM_WALLPAPER = 2;

    /*
    private final String STATE_FIRST_PREMIUM_WALLPAPER = "first_premium_wallpaper";
    private final String STATE_SECOND_PREMIUM_WALLPAPER = "second_premium_wallpaper";
    private final String STATE_THIRD_PREMIUM_WALLPAPER = "third_premium_wallpaper";
    */

    private Context context;
    private HashMap<String, Integer> mapPremWallpaper;

    /*
    public static int firstPremiumWallpaper;
    public static int secondPremiumWallpaper;
    public static int thirdPremiumWallpaper;

    private int stateFirstPremiumWallpaper;
    private int stateSecondPremiumWallpaper;
    private int stateThirdPremiumWallpaper;
    */

    public PremiumWallpaper(Context context) {
        this.context = context;
        mapPremWallpaper = new HashMap<String, Integer>();
    }

    /*
    public PremiumWallpaper(Context context, int firstPremiumWallpaper, int secondPremiumWallpaper, int thirdPremiumWallpaper) {
        this.context = context;
        PremiumWallpaper.firstPremiumWallpaper = firstPremiumWallpaper;
        PremiumWallpaper.secondPremiumWallpaper = secondPremiumWallpaper;
        PremiumWallpaper.thirdPremiumWallpaper = thirdPremiumWallpaper;

        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        stateFirstPremiumWallpaper = sPref.getInt(STATE_FIRST_PREMIUM_WALLPAPER, CLOSED_PREMIUM_WALLPAPER);
        stateSecondPremiumWallpaper = sPref.getInt(STATE_SECOND_PREMIUM_WALLPAPER, CLOSED_PREMIUM_WALLPAPER);
        stateThirdPremiumWallpaper = sPref.getInt(STATE_THIRD_PREMIUM_WALLPAPER, CLOSED_PREMIUM_WALLPAPER);
    }
    */

    public void add(int num) {
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        mapPremWallpaper.put(
                String.valueOf(num),
                sPref.getInt(String.valueOf(num), CLOSED_PREMIUM_WALLPAPER)
                );
    }

    public boolean equals(int i) {
        if(mapPremWallpaper.containsKey(String.valueOf(i))) {
            return true;
        } else {
            return false;
        }
    }

    public int getStateByNumber(int i) {
        if(mapPremWallpaper.containsKey(String.valueOf(i))) {
            int state = mapPremWallpaper.get(String.valueOf(i));
            if (state != NOT_PREMIUM_WALLPAPER) {
                return state;
            } else {
                return NOT_PREMIUM_WALLPAPER;
            }
        } else {
            return NOT_PREMIUM_WALLPAPER;
        }
    }

    public void setStateByNumber(int i, int state) {
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        Editor ed =  sPref.edit();
        ed.putInt(String.valueOf(i), state);
        ed.commit();
        mapPremWallpaper.put(String.valueOf(i), state);
    }

    /*
    public int getFirstPremiumWallpaper() {
        return firstPremiumWallpaper;
    }

    public int getSecondPremiumWallpaper() {
        return secondPremiumWallpaper;
    }

    public int getThirdPremiumWallpaper() {
        return thirdPremiumWallpaper;
    }

    public int getStateFirstPremiumWallpaper() {
        return stateFirstPremiumWallpaper;
    }

    public int getStateSecondPremiumWallpaper() {
        return stateSecondPremiumWallpaper;
    }

    public int getStateThirdPremiumWallpaper() {
        return stateThirdPremiumWallpaper;
    }
    */

    /*
    public int getStateByNumber(int i) {
        if(i == firstPremiumWallpaper) {
            return getStateFirstPremiumWallpaper();
        } else if(i == secondPremiumWallpaper) {
            return getStateSecondPremiumWallpaper();
        } else if(i == thirdPremiumWallpaper) {
            return getStateThirdPremiumWallpaper();
        } else {
            return NOT_PREMIUM_WALLPAPER;
        }
    }
    */

    /*
    public void setStateByNumber(int i, int state) {
        if(i == firstPremiumWallpaper) {
            setStateFirstPremiumWallpaper(state);
        } else if(i == secondPremiumWallpaper) {
            setStateSecondPremiumWallpaper(state);
        } else if(i == thirdPremiumWallpaper) {
            setStateThirdPremiumWallpaper(state);
        }
    }
    */

    /*
    public void setStateFirstPremiumWallpaper(int stateFirstPremiumWallpaper) {
        this.stateFirstPremiumWallpaper = stateFirstPremiumWallpaper;
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        Editor ed =  sPref.edit();
        ed.putInt(STATE_FIRST_PREMIUM_WALLPAPER, stateFirstPremiumWallpaper);
        ed.commit();
    }

    public void setStateSecondPremiumWallpaper(int stateSecondPremiumWallpaper) {
        this.stateSecondPremiumWallpaper = stateSecondPremiumWallpaper;
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        Editor ed =  sPref.edit();
        ed.putInt(STATE_SECOND_PREMIUM_WALLPAPER, stateSecondPremiumWallpaper);
        ed.commit();
    }

    public void setStateThirdPremiumWallpaper(int stateThirdPremiumWallpaper) {
        this.stateThirdPremiumWallpaper = stateThirdPremiumWallpaper;
        sPref = PreferenceManager.getDefaultSharedPreferences(context);
        Editor ed =  sPref.edit();
        ed.putInt(STATE_THIRD_PREMIUM_WALLPAPER, stateThirdPremiumWallpaper);
        ed.commit();
    }
    */

}
