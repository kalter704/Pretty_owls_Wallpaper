package com.aleksandr.nikitin.pretty_owls_wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;

import java.io.IOException;

public class MainPresenter {

    private final String CURRENT_PAGE = "current_page";

    public static final int DRAWER_ID_WALLPAPER = 1;
    public static final int DRAWER_ID_PRETTY_KITTENS = 2;
    public static final int DRAWER_ID_PRETTY_PUPPIES = 3;
    public static final int DRAWER_ID_PRETTY_OWLS = 4;
    public static final int DRAWER_ID_RACCOON_AND_FOX = 5;
    public static final int DRAWER_ID_SEAL_AND_WHALE = 6;

    private boolean isAdReady = false;

    private MainView mView;

    private int mCountOfSwipedPages;
    private int mNumberOfSwipedPages = Wallpapers.images.length - 1;

    private int mCurrentPage;

    public void init(Context context, MainView view) {
        mView = view;
        mCountOfSwipedPages = 0;
        mCurrentPage = PreferenceManager.getDefaultSharedPreferences(context).getInt(CURRENT_PAGE, 0);
        mView.initProgress(
                mCurrentPage,
                Wallpapers.images.length - 1
        );
        mView.setInvisibleCircleBar();
        mView.initBanner(getRequestForAds());
    }

    public void onResume(Context context){
        mCurrentPage = PreferenceManager.getDefaultSharedPreferences(context).getInt(CURRENT_PAGE, 0);
        mView.setCurrentPage(
                mCurrentPage
        );
    }

    public void onPause(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(CURRENT_PAGE, mCurrentPage)
                .apply();
    }

    public void onBindView(MainView view) {
        mView = view;
    }

    public void onUnbindView() {
        mView = null;
    }

    public void onPageSwipe(int page) {
        mCurrentPage = page;
        mView.setProgressToProgBar(page);

        mCountOfSwipedPages++;

        if (isAdReady) {
            mView.showInterstitialAd();
            isAdReady = false;
            mCountOfSwipedPages = 0;
        } else if (mCountOfSwipedPages >= mNumberOfSwipedPages) {
            mCountOfSwipedPages = 0;
            mView.requestInterstitialAd(getRequestForAds());
        }
    }

    public void onReadyInterstitialAd() {
        isAdReady = true;
    }

    public void onEndFadeIn() {
        mView.setVisibleCircleBar();
    }

    public void onEndFadeOut() {
        mView.setInvisibleCircleBar();
    }

    public void onSelectedMenuItem(int id) {
        Integer stringID = null;
        switch (id) {
            case DRAWER_ID_PRETTY_KITTENS:
                stringID = R.string.url_pretty_kittens;
                break;
            case DRAWER_ID_PRETTY_PUPPIES:
                stringID = R.string.url_pretty_puppies;
                break;
            case DRAWER_ID_PRETTY_OWLS:
                stringID = R.string.url_pretty_owls;
                break;
            case DRAWER_ID_RACCOON_AND_FOX:
                stringID = R.string.url_raccoon_and_fox;
                break;
            case DRAWER_ID_SEAL_AND_WHALE:
                stringID = R.string.url_seal_and_whale;
                break;
        }
        if (stringID != null) {
            mView.openPlayMarket(stringID);
        }
    }

    public void onSetWallpaper(MainActivity activity) {
        new SetWallpaperAsyncTask().execute(activity);
    }

    private AdRequest getRequestForAds() {

        return new AdRequest.Builder().build();

        // EMULATOR

//        return new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("3E0DC5B8245C21520131AB58878FDCE7")
//                .build();

        // Highscreen ICE 2
/*
        return new AdRequest.Builder()
                .addTestDevice("3E0DC5B8245C21520131AB58878FDCE7")
                .build();
*/
        // HUAWEI
        /*
        return new AdRequest.Builder()
                .addTestDevice("5A43B1E3FEA266FCDB1E781CF0903804")
                .build();
                */

        // ASUS
        /*
        return new AdRequest.Builder()
                .addTestDevice("3D7BF0D7FAA1EEBFFA72EA203BF60414")
                .build();
                */
    }


    private void setWallpaperToBackground(MainActivity activity) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int dispayWidth = metrics.widthPixels;
        int dispayHeight = metrics.heightPixels;

        Log.d("QWERTY", String.valueOf(dispayWidth));
        Log.d("QWERTY", String.valueOf(dispayHeight));

        wallpaperManager.suggestDesiredDimensions(dispayWidth, wallpaperManager.getDesiredMinimumHeight());
        wallpaperManager.setWallpaperOffsetSteps(1, 1);

        if (DisplayInfo.isCorrespondsToTheDensityResolution(dispayWidth, dispayHeight)) {
            try {
                wallpaperManager.setResource(Wallpapers.images[mCurrentPage]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), Wallpapers.images[mCurrentPage]);
            bitmap = Bitmap.createScaledBitmap(bitmap, dispayWidth, wallpaperManager.getDesiredMinimumHeight(), true);

            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private class SetWallpaperAsyncTask extends AsyncTask<MainActivity, Void, Void> {

        @Override
        protected Void doInBackground(MainActivity... activities) {
            setWallpaperToBackground(activities[0]);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mView.disableButton();
            mView.fadeInCircleBar();
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);

            mView.CompleteSetWallpaper();

            mView.enableButton();
            mView.fadeOutCircleBar();

        }
    }

}
