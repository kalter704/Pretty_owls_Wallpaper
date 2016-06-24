package com.aleksandr.nikitin.pretty_owls_wallpaper;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class PageFragmentWithPremiumWallpaper extends Fragment {
    static final String ARGUMENT_IMAGE_ID = "arg_image_id";
    static final String ARGUMENT_IMAGE_NUMBER = "arg_image_number";
    int imageId;
    int numberOfImg;
    int statePicture;
    ImageView img;

    LinearLayout linLayoutWithTextAndBtn;

    //LinearLayout linImg;
    //ImageView imgRotate;
    ProgressBar progressBar;
    Animation animRotate;
    Animation animAlphaVilible;
    Animation animAlphaInvilible;

    onShowVideoAdListener showVideoAdListener;

    static Fragment newInstance(int image, int number) {
        PageFragmentWithPremiumWallpaper pageFragment = new PageFragmentWithPremiumWallpaper();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_IMAGE_ID, image);
        arguments.putInt(ARGUMENT_IMAGE_NUMBER, number);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageId = getArguments().getInt(ARGUMENT_IMAGE_ID);
        numberOfImg = getArguments().getInt(ARGUMENT_IMAGE_NUMBER);
        statePicture = PremiumWallpaper.CLOSED_PREMIUM_WALLPAPER;

        //animRotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotation_proccess);
        animAlphaVilible = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_vilible);
        animAlphaInvilible = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_invilible);

        animAlphaVilible.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //imgRotate.startAnimation(animRotate);
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
                //imgRotate.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_with_premium_wallpaper, null);

        img = (ImageView) view.findViewById(R.id.imgView);
        img.setImageResource(imageId);

        linLayoutWithTextAndBtn = (LinearLayout) view.findViewById(R.id.linLayoutWithTextAndBtn);

        if(statePicture == PremiumWallpaper.OPENED_PREMIUM_WALLPAPER)
            onCreateOpenPage();
        else
            onCreateClosePage(view);

        return view;

    }

    private void onCreateOpenPage() {
        linLayoutWithTextAndBtn.setVisibility(View.INVISIBLE);
    }

    private void onCreateClosePage(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            img.setAlpha((float) 0.3);
        }

        //linImg = (LinearLayout) view.findViewById(R.id.linImg);
        //imgRotate = (ImageView) view.findViewById(R.id.imageRotate);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        Button btnWatchVideo = (Button) view.findViewById(R.id.btnWatchVideo);
        btnWatchVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linLayoutWithTextAndBtn.setVisibility(View.INVISIBLE);
                progressBar.startAnimation(animAlphaVilible);
                //linImg.startAnimation(animAlphaVilible);
                showVideoAdListener.onShowVideoAd(numberOfImg);
            }
        });
    }

    public void reset() {
        linLayoutWithTextAndBtn.setVisibility(View.VISIBLE);
        progressBar.startAnimation(animAlphaInvilible);
        //linImg.startAnimation(animAlphaInvilible);
    }

    public void openPicture() {
        linLayoutWithTextAndBtn.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            img.setAlpha((float) 1.0);
        }
        statePicture = PremiumWallpaper.OPENED_PREMIUM_WALLPAPER;
    }

    public interface onShowVideoAdListener {
        public void onShowVideoAd(int i);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            showVideoAdListener = (onShowVideoAdListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

}
