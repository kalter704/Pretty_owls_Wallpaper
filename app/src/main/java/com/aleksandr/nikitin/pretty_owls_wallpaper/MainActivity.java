package com.aleksandr.nikitin.pretty_owls_wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.IOException;

public class MainActivity extends FragmentActivity implements PageFragmentWithPremiumWallpaper.onShowVideoAdListener {

    private final String CURRENT_PAGE = "current_page";

    private int currentPage;
    private int pictureToBeOpened;

    private int countOfSwipedPages;
    private int numberOfSwipedPages;
    private boolean isShowFullscreenAds;
    private boolean isDoNewRequestForInterstitial;

    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;

    private Button btnSetWallPaper;
    private ImageButton btnExit;

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    //boolean isPagerWithShadow;

    //private LinearLayout linImg;
    //private ImageView img;
    private ProgressBar progressBar;
    private ProgressBar progressBarShowPosition;
    //private Animation animRotate;
    private Animation animAlphaVilible;
    private Animation animAlphaInvilible;
    //Animation animFadeIn;
    //Animation animFadeOut;

    private PremiumWallpaper premiumWallpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        currentPage = sPref.getInt(CURRENT_PAGE, 0);

        premiumWallpaper = new PremiumWallpaper(this);
        //premiumWallpaper = new PremiumWallpaper(this, Wallpapers.images.length - 3, Wallpapers.images.length - 2, Wallpapers.images.length - 1);
        premiumWallpaper.add(Wallpapers.images.length - 6);
        premiumWallpaper.add(Wallpapers.images.length - 5);
        premiumWallpaper.add(Wallpapers.images.length - 4);
        premiumWallpaper.add(Wallpapers.images.length - 3);
        premiumWallpaper.add(Wallpapers.images.length - 2);
        premiumWallpaper.add(Wallpapers.images.length - 1);

        //premiumWallpaper.setStateByNumber(Wallpapers.images.length - 3, PremiumWallpaper.CLOSED_PREMIUM_WALLPAPER);
        //premiumWallpaper.setStateByNumber(Wallpapers.images.length - 2, PremiumWallpaper.CLOSED_PREMIUM_WALLPAPER);
        //premiumWallpaper.setStateByNumber(Wallpapers.images.length - 1, PremiumWallpaper.CLOSED_PREMIUM_WALLPAPER);

        countOfSwipedPages = 0;
        numberOfSwipedPages = Wallpapers.images.length - 1;
        isShowFullscreenAds = false;
        isDoNewRequestForInterstitial = false;

        //linImg = (LinearLayout) findViewById(R.id.linImg);
        //img = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        progressBarShowPosition = (ProgressBar) findViewById(R.id.progressBar2);
        progressBarShowPosition.setMax(Wallpapers.images.length - 1);
        progressBarShowPosition.setProgress(currentPage);

        //animRotate = AnimationUtils.loadAnimation(this, R.anim.rotation_proccess);
        animAlphaVilible = AnimationUtils.loadAnimation(this, R.anim.alpha_vilible);
        animAlphaInvilible = AnimationUtils.loadAnimation(this, R.anim.alpha_invilible);
        //animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        //animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        animAlphaVilible.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //img.startAnimation(animRotate);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //linImg.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animAlphaInvilible.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                progressBar.setVisibility(View.INVISIBLE);
                //linImg.setVisibility(View.INVISIBLE);
                //img.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        /*
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("QWERTY", "animFadeIn onAnimationEnd");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    pager.setAlpha((float) 1.0);
                }
                img.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("QWERTY", "animFadeOut onAnimationEnd");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    pager.setAlpha((float) 0.3);
                }
                img.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        */

        //img.startAnimation(animation);

        /*


        SurfaceView v = (SurfaceView) findViewById(R.id.surfaceView);
        GifRun w = new GifRun();
        w.LoadGiff(v, this, R.drawable.proc123);
*/

/*
        ((Button) findViewById(R.id.btnFix)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int dispayWidth = metrics.widthPixels;
                int dispayHeight = metrics.heightPixels;


                Toast.makeText(getApplicationContext(), String.valueOf(dispayWidth), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), String.valueOf(dispayHeight), Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(), String.valueOf(wallpaperManager.getDesiredMinimumWidth()), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), String.valueOf(wallpaperManager.getDesiredMinimumHeight()), Toast.LENGTH_SHORT).show();
*/

                /*
                img.startAnimation(animRotate);
                linImg.setVisibility(View.VISIBLE);
                */
/*
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    requestNewInterstitial();
                }
*/
                /*
                wallpaperManager.suggestDesiredDimensions(720, 1280);

                Context context = getApplicationContext();
                int resID = R.drawable.iron_man_1;

                Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        getResources().getResourcePackageName(resID) + '/' +
                        getResources().getResourceTypeName(resID) + '/' +
                        getResources().getResourceEntryName(resID) );

                Intent intent = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    intent = wallpaperManager.getCropAndSetWallpaperIntent(uri);
                }
                startActivity(intent);
                */
                /*
                wallpaperManager.setWallpaperOffsetSteps(0, (float) 0.33);
                wallpaperManager.setWallpaperOffsets(getWindow().getDecorView().getRootView().getWindowToken(), (float) 0.8, (float) 0.8);
                */
/*
            }
        });
        */


        /*
        ((Button) findViewById(R.id.btnFluid1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additionalWidth = (float) 0.1;
                //img.clearAnimation();
                linImg.startAnimation(animAlphaInvilible);
            }
        });

        ((Button) findViewById(R.id.btnFluid2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additionalWidth = (float) 0.4;
            }
        });
        */

        btnSetWallPaper = (Button) findViewById(R.id.btnSetWallpaper);
        btnSetWallPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new setWallpaperAsyncTask()).execute();
            }
        });
/*
        btnExit = (ImageButton) findViewById(R.id.btnExitFromMyApp);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
*/
        //isPagerWithShadow = false;
        pager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new MyFragmentPageAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                //Toast.makeText(getApplicationContext(), "i = " + String.valueOf(i) + " v = " + String.valueOf(v) + " i2 = " + String.valueOf(i2), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int i) {
                progressBarShowPosition.setProgress(i);
                //Toast.makeText(getApplicationContext(), "i = " + String.valueOf(i), Toast.LENGTH_SHORT).show();
                if(premiumWallpaper.equals(i)) {
                    if(premiumWallpaper.getStateByNumber(i) == PremiumWallpaper.OPENED_PREMIUM_WALLPAPER) {
                        buttonSetEnabled(btnSetWallPaper, true);
                        //buttonSetEnabled(pager, true);
                        //setShadowForPager(false);
                    } else {
                        buttonSetEnabled(btnSetWallPaper, false);
                        //buttonSetEnabled(findViewById(R.id.linearLayoutWithViewPager), false);
                        //buttonSetEnabled(pager, false);
                        /*
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            pager.setAlpha((float) 0.1);
                            Toast.makeText(getApplicationContext(), "asefasefsef", Toast.LENGTH_SHORT).show();
                        }
                        */

                        //setShadowForPager(true);
                    }
                } else {
                    buttonSetEnabled(btnSetWallPaper, true);
                    //setShadowForPager(false);
                    //buttonSetEnabled(pager, true);
                }

                /*
                if(i == 2) {
                    buttonSetEnabled(btnSetWallPaper, false);
                } else {
                    buttonSetEnabled(btnSetWallPaper, true);
                }
                */
                if (isShowFullscreenAds) {
                    isShowFullscreenAds = false;
                    countOfSwipedPages = 0;
                    mInterstitialAd.show();
                }
                if (countOfSwipedPages < numberOfSwipedPages) {
                    countOfSwipedPages++;
                } else {
                    countOfSwipedPages = 0;
                    requestNewInterstitial();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //Toast.makeText(getApplicationContext(), "i = " + String.valueOf(i), Toast.LENGTH_SHORT).show();
            }

        });


        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.id_app_in_admob));

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = getRequestForAds();
        adView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                isShowFullscreenAds = true;
            }
        });

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem reward) {
                //Toast.makeText(getApplicationContext(), "onRewarded! currency: " + reward.getType() + "  amount: " + reward.getAmount(), Toast.LENGTH_SHORT).show();
                premiumWallpaper.setStateByNumber(pictureToBeOpened, PremiumWallpaper.OPENED_PREMIUM_WALLPAPER);
                PageFragmentWithPremiumWallpaper page = (PageFragmentWithPremiumWallpaper) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + pictureToBeOpened);
                if (page != null) {
                    page.openPicture();
                }
                buttonSetEnabled(btnSetWallPaper, true);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                //Toast.makeText(getApplicationContext(), "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                //Toast.makeText(getApplicationContext(), "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                //Toast.makeText(getApplicationContext(), "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
                if(errorCode == AdRequest.ERROR_CODE_NETWORK_ERROR) {
                    NetworkErrorDialog dlg = new NetworkErrorDialog();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        dlg.show(getFragmentManager(), "network_error_dlg");
                    }
                }
                PageFragmentWithPremiumWallpaper page = (PageFragmentWithPremiumWallpaper) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + pictureToBeOpened);
                if (page != null) {
                    page.reset();
                }
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                //Toast.makeText(getApplicationContext(), "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
                mRewardedVideoAd.show();
                PageFragmentWithPremiumWallpaper page = (PageFragmentWithPremiumWallpaper) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + pictureToBeOpened);
                if (page != null) {
                    page.reset();
                }
            }

            @Override
            public void onRewardedVideoAdOpened() {
                //Toast.makeText(getApplicationContext(), "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                //Toast.makeText(getApplicationContext(), "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mRewardedVideoAd.resume(this);
        pager.setCurrentItem(currentPage);
        //UnityAds.changeActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRewardedVideoAd.pause(this);
        currentPage = pager.getCurrentItem();
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed =  sPref.edit();
        ed.putInt(CURRENT_PAGE, pager.getCurrentItem());
        ed.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRewardedVideoAd.destroy(this);
    }

    private void requestNewInterstitial() {
        mInterstitialAd.loadAd(getRequestForAds());
    }

    private void requestNewRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.video_ad_unit_id), getRequestForAds());
    }

    private AdRequest getRequestForAds() {

        //return new AdRequest.Builder().build();

        // EMULATOR

        return new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("3E0DC5B8245C21520131AB58878FDCE7")
                .build();

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

    void setWallpaperToBackground() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int dispayWidth = metrics.widthPixels;
        int dispayHeight = metrics.heightPixels;

        Log.d("QWERTY", String.valueOf(dispayWidth));
        Log.d("QWERTY", String.valueOf(dispayHeight));

        wallpaperManager.suggestDesiredDimensions(dispayWidth, wallpaperManager.getDesiredMinimumHeight());
        wallpaperManager.setWallpaperOffsetSteps(1, 1);

        if (DisplayInfo.isCorrespondsToTheDensityResolution(dispayWidth, dispayHeight)) {
            try {
                wallpaperManager.setResource(Wallpapers.images[pager.getCurrentItem()]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), Wallpapers.images[pager.getCurrentItem()]);
            bitmap = Bitmap.createScaledBitmap(bitmap, dispayWidth, wallpaperManager.getDesiredMinimumHeight(), true);

            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onShowVideoAd(int i) {
        pictureToBeOpened = i;
        requestNewRewardedVideoAd();
        //buttonSetEnabled(btnSetWallPaper, true);
    }

    private class MyFragmentPageAdapter extends FragmentPagerAdapter {

        private int[] images = Wallpapers.images;
        private int imagesCount = images.length;

        public MyFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if(premiumWallpaper.getStateByNumber(i) == PremiumWallpaper.CLOSED_PREMIUM_WALLPAPER) {
                return PageFragmentWithPremiumWallpaper.newInstance(images[i], i);
            }
            return PageFragment.newInstance(images[i]);
        }

        @Override
        public int getCount() {
            return imagesCount;
        }

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            if (hasFocus) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    private class setWallpaperAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //buttonSetEnabled(btnExit, false);
            buttonSetEnabled(btnSetWallPaper, false);
            progressBar.startAnimation(animAlphaVilible);
            //linImg.startAnimation(animAlphaVilible);
        }

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);

            //buttonSetEnabled(btnExit, true);
            if(premiumWallpaper.equals(pager.getCurrentItem())) {
                if(premiumWallpaper.getStateByNumber(pager.getCurrentItem()) == PremiumWallpaper.OPENED_PREMIUM_WALLPAPER) {
                    buttonSetEnabled(btnSetWallPaper, true);
                } else {
                    buttonSetEnabled(btnSetWallPaper, false);
                }
            } else {
                buttonSetEnabled(btnSetWallPaper, true);
            }
            progressBar.startAnimation(animAlphaInvilible);
            //linImg.startAnimation(animAlphaInvilible);

            Context context = getApplicationContext();
            CharSequence text = getResources().getString(R.string.successful_set_wallpaper);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        @Override
        protected Void doInBackground(Void... param) {
            setWallpaperToBackground();
            return null;
        }
    }

    void buttonSetEnabled(View view, boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (enabled == true) {
                view.setAlpha((float) 1.0);
            } else {
                view.setAlpha((float) 0.4);
            }
        }
        view.setEnabled(enabled);
        /*
        if(enabled == true) {
            view.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            view.setTextColor(getResources().getColor(R.color.colorGrey));
        }
        */
    }

}