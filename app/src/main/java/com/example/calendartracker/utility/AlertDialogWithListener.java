package com.example.calendartracker.utility;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.calendartracker.R;

public class AlertDialogWithListener extends DialogFragment {

    private final Context context;
    private final DialogOnClickListener listener;
    private final int operation;
    private boolean showOnlyPositive = false;

    private String name;

    private static final String TAG = "Confirmation";

    public interface DialogOnClickListener {
        void onConfirmClick();
        void onCancelClick();
    }

    public AlertDialogWithListener(Context context, int op, DialogOnClickListener listener) {
        Log.d(TAG, "DeleteConfirmationDialog: ");
        this.context = context;
        this.listener = listener;
        this.operation = op;
    }

    public AlertDialogWithListener(Context context, String name, int op, DialogOnClickListener listener) {
        Log.d(TAG, "DeleteConfirmationDialog: ");
        this.context = context;
        this.name = name;
        this.operation = op;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: ");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String message = "";
        switch (operation) {
            case Constants.DIALOG_PERMISSION_LONG:
                message = context.getString(R.string.parse_require_permission_long);
                break;
            case Constants.DIALOG_PERMISSION_SHORT:
                message = context.getString(R.string.parse_require_permission_short);
                showOnlyPositive = true;
                break;
            case Constants.DIALOG_CREATE_EVENT:
                message = context.getString(R.string.parse_create_event_confirmation);
                break;
            case Constants.DIALOG_DELETE_CONFIRMATION:
                message = context.getString(R.string.etc_ask_delete_confirmation, name);
                break;
        }
        builder.setMessage(message).setPositiveButton(R.string.etc_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // START THE GAME!
                        listener.onConfirmClick();
                    }
                });
        if (!showOnlyPositive) {
            builder.setMessage(message).setNegativeButton(R.string.etc_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    listener.onCancelClick();
                }
            });
        }
        builder.setTitle(R.string.etc_warning);
        builder.setIcon(R.drawable.warning_48px);
        return builder.create();
    }

}
