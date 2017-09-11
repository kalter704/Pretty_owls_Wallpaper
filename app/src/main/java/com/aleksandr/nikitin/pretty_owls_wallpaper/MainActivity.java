package com.aleksandr.nikitin.pretty_owls_wallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class MainActivity extends FragmentActivity {

    private final String TAG = "main_activity_tag";

    private final String CURRENT_PAGE = "current_page";

    private final String CACHE_IMAGE_NAME = "cached_image.jpg";

    private final int DRAWER_ID_WALLPAPER = 1;
    private final int DRAWER_ID_PRETTY_KITTENS = 2;
    private final int DRAWER_ID_PRETTY_PUPPIES = 3;
    private final int DRAWER_ID_PRETTY_OWLS = 4;
    private final int DRAWER_ID_RACCOON_AND_FOX = 5;
    private final int DRAWER_ID_SEAL_AND_WHALE = 6;

    private int currentPage;

    private int countOfSwipedPages;
    private int numberOfSwipedPages;
    private boolean isShowFullscreenAds;

    private InterstitialAd mInterstitialAd;

    private Button btnSetWallPaper;

    private ViewPager pager;

    private ProgressBar progressBar;
    private ProgressBar progressBarShowPosition;
    private Animation animAlphaVisible;
    private Animation animAlphaInvisible;

    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Log.d(TAG, "1");

        View header = this.getLayoutInflater().inflate(R.layout.drawer_header, null, false);

        Picasso.with(header.getContext())
                .load(R.mipmap.ic_launcher)
                .transform(new CircularTransformation())
                .into((ImageView) header.findViewById(R.id.imgHeader));

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withActionBarDrawerToggle(true)
                .withHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_wallpaper).withIcon(R.drawable.ic_wallpaper).withIdentifier(DRAWER_ID_WALLPAPER),
                        new SectionDrawerItem().withName(R.string.drawer_item_our_applications),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_pretty_kittens).withIcon(R.drawable.ic_pretty_kittens).withIdentifier(DRAWER_ID_PRETTY_KITTENS),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_pretty_puppies).withIcon(R.drawable.ic_pretty_puppies).withIdentifier(DRAWER_ID_PRETTY_PUPPIES),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_raccoon_and_fox).withIcon(R.drawable.ic_raccoon_and_fox).withIdentifier(DRAWER_ID_RACCOON_AND_FOX),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_seal_and_whale).withIcon(R.drawable.ic_seal_and_whale).withIdentifier(DRAWER_ID_SEAL_AND_WHALE),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_like).withIcon(R.drawable.ic_like).withIdentifier(DRAWER_ID_PRETTY_OWLS)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            int id = (int) drawerItem.getIdentifier();
                            if (id == DRAWER_ID_WALLPAPER) {
                                return false;
                            } else {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                if (id == DRAWER_ID_PRETTY_KITTENS) {
                                    intent.setData(Uri.parse(getString(R.string.url_pretty_kittens)));
                                } else if (id == DRAWER_ID_PRETTY_PUPPIES) {
                                    intent.setData(Uri.parse(getString(R.string.url_pretty_puppies)));
                                } else if (id == DRAWER_ID_PRETTY_OWLS) {
                                    intent.setData(Uri.parse(getString(R.string.url_pretty_owls)));
                                } else if (id == DRAWER_ID_RACCOON_AND_FOX) {
                                    intent.setData(Uri.parse(getString(R.string.url_raccoon_and_fox)));
                                } else if (id == DRAWER_ID_SEAL_AND_WHALE) {
                                    intent.setData(Uri.parse(getString(R.string.url_seal_and_whale)));
                                }
                                startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .build();

        Button btnOpenMenu = (Button) findViewById(R.id.btnMenu);
        btnOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer();
            }
        });

        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        currentPage = sPref.getInt(CURRENT_PAGE, 0);

        //premiumWallpaper = new PremiumWallpaper(this);

        countOfSwipedPages = 0;
        numberOfSwipedPages = Wallpapers.images.length - 1;
        isShowFullscreenAds = false;

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        progressBarShowPosition = (ProgressBar) findViewById(R.id.progressBar2);
        progressBarShowPosition.setMax(Wallpapers.images.length - 1);
        progressBarShowPosition.setProgress(currentPage);

        animAlphaVisible = AnimationUtils.loadAnimation(this, R.anim.alpha_vilible);
        animAlphaInvisible = AnimationUtils.loadAnimation(this, R.anim.alpha_invilible);

        animAlphaVisible.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animAlphaInvisible.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        btnSetWallPaper = (Button) findViewById(R.id.btnSetWallpaper);
        btnSetWallPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new setWallpaperAsyncTask().execute();
            }
        });

        pager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new MyFragmentPageAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                progressBarShowPosition.setProgress(i);
                buttonSetEnabled(btnSetWallPaper, true);

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

    }

    @Override
    protected void onResume() {
        super.onResume();
        pager.setCurrentItem(currentPage);
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentPage = pager.getCurrentItem();
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(CURRENT_PAGE, pager.getCurrentItem());
        ed.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void requestNewInterstitial() {
        mInterstitialAd.loadAd(getRequestForAds());
    }

    private AdRequest getRequestForAds() {

        return new AdRequest.Builder().build();

        // EMULATOR
/*
        return new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("3E0DC5B8245C21520131AB58878FDCE7")
                .build();
*/
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

    private Uri setWallpaperToBackground() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int displayWidth = metrics.widthPixels;
        int displayHeight = metrics.heightPixels;

        Log.d("QWERTY", String.valueOf(displayWidth));
        Log.d("QWERTY", String.valueOf(displayHeight));

        try {
            File file = new File(getFilesDir().toString(), CACHE_IMAGE_NAME);

            OutputStream outputStream = openFileOutput(CACHE_IMAGE_NAME, Context.MODE_WORLD_READABLE);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), Wallpapers.images[pager.getCurrentItem()]);

            if (!DisplayInfo.isCorrespondsToTheDensityResolution(displayWidth, displayHeight)) {
                bitmap = Bitmap.createScaledBitmap(bitmap, displayWidth, wallpaperManager.getDesiredMinimumHeight(), true);
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);

            outputStream.flush();
            outputStream.close();

            return Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class MyFragmentPageAdapter extends FragmentPagerAdapter {

        private int[] images = Wallpapers.images;
        private int imagesCount = images.length;

        MyFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return PageFragment.newInstance(images[i]);
        }

        @Override
        public int getCount() {
            return imagesCount;
        }

    }

/*
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
*/

    private class setWallpaperAsyncTask extends AsyncTask<Void, Void, Uri> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonSetEnabled(btnSetWallPaper, false);
            progressBar.startAnimation(animAlphaVisible);
        }

        @Override
        protected Uri doInBackground(Void... param) {
            return setWallpaperToBackground();
        }

        @Override
        protected void onPostExecute(Uri param) {
            super.onPostExecute(param);

            Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setDataAndType(param, "image/*");
            intent.putExtra("mimeType", "image/*");
            startActivity(intent);

            buttonSetEnabled(btnSetWallPaper, true);

            progressBar.startAnimation(animAlphaInvisible);

//            Context context = getApplicationContext();
//            CharSequence text = getResources().getString(R.string.successful_set_wallpaper);
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        }
    }

    void buttonSetEnabled(View view, boolean enabled) {
        if (enabled) {
            view.setAlpha((float) 1.0);
        } else {
            view.setAlpha((float) 0.4);
        }
        view.setEnabled(enabled);
    }

}