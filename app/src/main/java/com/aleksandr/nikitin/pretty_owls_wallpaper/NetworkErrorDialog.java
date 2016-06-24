package com.aleksandr.nikitin.pretty_owls_wallpaper;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NetworkErrorDialog extends DialogFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Network");
        View view = inflater.inflate(R.layout.dialog_network_error, null);
        view.findViewById(R.id.btnOk).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
