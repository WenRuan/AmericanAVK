package com.tensorflow.AVK.CS426.demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.tensorflow.lite.examples.demo.R;

public class LocationErrorDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Location Permissions Denied
        builder.setTitle(R.string.dialog_location_error)
                .setMessage(R.string.dialog_location_message)
                .setNeutralButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: Nothing. This issue is now in the user's hands >:(
                    }
                });


        return builder.create();
    }
}