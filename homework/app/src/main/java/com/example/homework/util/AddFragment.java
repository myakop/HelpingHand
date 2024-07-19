package com.example.homework.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddFragment extends DialogFragment {
    static int i=0;
    @NonNull
    @Override

    // alert for adding successfully using AlertDialog And DialogFragment for user
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Well done")
                .setMessage("successfully Added")
                .setPositiveButton("OK", (dialog, which) -> { // back to  enterpage
                    Context context = getContext();
                    if (context != null) {
                        Intent intent = new Intent(context, enterpage.class);
                        startActivity(intent);
                    }
                    // Handle positive button click
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle negative button click
                });
        return builder.create();
    }

}
