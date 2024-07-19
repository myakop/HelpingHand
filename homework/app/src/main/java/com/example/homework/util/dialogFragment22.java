package com.example.homework.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
// alert for adding successfully using AlertDialog And DialogFragment for admin
public class dialogFragment22 extends DialogFragment {
      static int i=0;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Well done")
                .setMessage("successfully Added")
                .setPositiveButton("OK", (dialog, which) -> {
                    Context context = getContext();
                    if (context != null) {
                        Intent intent = new Intent(context, admin.class); // back to  admin home
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

