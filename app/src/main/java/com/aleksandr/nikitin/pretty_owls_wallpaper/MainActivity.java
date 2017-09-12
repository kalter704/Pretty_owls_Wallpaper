package com.aleksandr.nikitin.pretty_owls_wallpaper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

import static com.aleksandr.nikitin.pretty_owls_wallpaper.MainPresenter.DRAWER_ID_PRETTY_KITTENS;
import static com.aleksandr.nikitin.pretty_owls_wallpaper.MainPresenter.DRAWER_ID_PRETTY_OWLS;
import static com.aleksandr.nikitin.pretty_owls_wallpaper.MainPresenter.DRAWER_ID_PRETTY_PUPPIES;
import static com.aleksandr.nikitin.pretty_owls_wallpaper.MainPresenter.DRAWER_ID_RACCOON_AND_FOX;
import static com.aleksandr.nikitin.pretty_owls_wallpaper.MainPresenter.DRAWER_ID_SEAL_AND_WHALE;

public class MainActivity extends FragmentActivity implements MainView {

    private InterstitialAd mInterstitialAd;

    private Button mBtnSetWallPaper;

    private ViewPager mPager;

    private ProgressBar mProgressBar;
    private ProgressBar mProgressBarShowPosition;
    private Animation mAnimAlphaVisible;
    private Animation mAnimAlphaInvisible;

    private Drawer mDrawer;

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View header = this.getLayoutInflater().inflate(R.layout.drawer_header, null, false);

        Picasso.with(header.getContext())
                .load(R.mipmap.ic_launcher)
                .transform(new CircularTransformation())
                .into((ImageView) header.findViewById(R.id.imgHeader));

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withActionBarDrawerToggle(true)
                .withHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_wallpaper).withIcon(R.drawable.ic_wallpaper),
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
                            mPresenter.onSelectedMenuItem(id);
                        }
                        return false;
                    }
                })
                .build();

        Button btnOpenMenu = (Button) findViewById(R.id.btnMenu);
        btnOpenMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer();
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mProgressBarShowPosition = (ProgressBar) findViewById(R.id.progressBar2);

        mAnimAlphaVisible = AnimationUtils.loadAnimation(this, R.anim.alpha_vilible);
        mAnimAlphaInvisible = AnimationUtils.loadAnimation(this, R.anim.alpha_invilible);

        mAnimAlphaVisible.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPresenter.onEndFadeIn();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mAnimAlphaInvisible.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPresenter.onEndFadeOut();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBtnSetWallPaper = (Button) findViewById(R.id.btnSetWallpaper);
        mBtnSetWallPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onSetWallpaper(MainActivity.this);
            }
        });

        mPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new MyFragmentPageAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                mPresenter.onPageSwipe(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }

        });


        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.id_app_in_admob));

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mPresenter.onReadyInterstitialAd();
            }
        });

        mPresenter = new MainPresenter();
        mPresenter.init(this, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause(this);
    }

    @Override
    public void initBanner(AdRequest adRequest) {
        ((AdView) findViewById(R.id.adView)).loadAd(adRequest);
    }

    @Override
    public void initProgress(int current, int maxProgress) {
        mProgressBarShowPosition.setMax(maxProgress);
        mProgressBarShowPosition.setProgress(current);
    }

    @Override
    public void fadeInCircleBar() {
        mProgressBar.startAnimation(mAnimAlphaVisible);
    }

    @Override
    public void fadeOutCircleBar() {
        mProgressBar.startAnimation(mAnimAlphaInvisible);
    }

    @Override
    public void enableButton() {
        viewSetEnable(mBtnSetWallPaper, true);

    }

    @Override
    public void disableButton() {
        viewSetEnable(mBtnSetWallPaper, false);
    }

    @Override
    public void setVisibleCircleBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setInvisibleCircleBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void openPlayMarket(int stringID) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(stringID)));
        startActivity(intent);
    }

    @Override
    public void CompleteSetWallpaper() {
        CharSequence text = getResources().getString(R.string.successful_set_wallpaper);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    @Override
    public void setProgressToProgBar(int progress) {
        mProgressBarShowPosition.setProgress(progress);
    }

    @Override
    public void requestInterstitialAd(AdRequest request) {
        mInterstitialAd.loadAd(request);
    }

    @Override
    public void showInterstitialAd() {
        mInterstitialAd.show();
    }

    @Override
    public void setCurrentPage(int page) {
        mPager.setCurrentItem(page);
    }

    private void viewSetEnable(View view, boolean enable) {
        if (enable) {
            view.setAlpha((float) 1.0);
        } else {
            view.setAlpha((float) 0.4);
        }
        view.setEnabled(enable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onUnbindView();
    }
}