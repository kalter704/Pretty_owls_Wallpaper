package com.aleksandr.nikitin.pretty_owls_wallpaper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPageAdapter extends FragmentPagerAdapter {

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
