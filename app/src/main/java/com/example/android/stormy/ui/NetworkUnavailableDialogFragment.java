package com.example.android.stormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Gabriel on 08/16/15.
 */
public class NetworkUnavailableDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Opps!");
        builder.setMessage("No network available");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
