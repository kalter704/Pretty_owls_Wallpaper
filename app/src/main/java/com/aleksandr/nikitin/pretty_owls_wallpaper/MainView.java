package com.aleksandr.nikitin.pretty_owls_wallpaper;

import com.google.android.gms.ads.AdRequest;

public interface MainView {
    void initProgress(int current, int maxProgress);

    void initBanner(AdRequest adRequest);

    void showInterstitialAd();

    void requestInterstitialAd(AdRequest request);

    void fadeInCircleBar();

    void fadeOutCircleBar();

    void setVisibleCircleBar();

    void setInvisibleCircleBar();

    void openPlayMarket(int stringID);

    void setCurrentPage(int page);

    void setProgressToProgBar(int progress);

    void enableButton();

    void disableButton();

    void CompleteSetWallpaper();
}
