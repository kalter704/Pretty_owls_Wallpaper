package com.aleksandr.nikitin.pretty_owls_wallpaper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class PageFragment extends Fragment {

    static final String ARGUMENT_IMAGE_ID = "arg_image_id";
    int imageId;

    static Fragment newInstance(int image) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments =  new Bundle();
        arguments.putInt(ARGUMENT_IMAGE_ID, image);
        pageFragment.setArguments(arguments);
        return pageFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageId = getArguments().getInt(ARGUMENT_IMAGE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.imgView);
        imageView.setImageResource(imageId);

        return view;

    }
}
