package com.example.homework.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class nullfagment extends DialogFragment {

    static int i=0;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("error")
                .setMessage("you must set te clock")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Get the context from the dialog
                    Context context = getContext();
                    if (context != null) {

                    }
                });

        return builder.create();
    }
}
