package com.bitjam.calendartracker.utility;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bitjam.calendartracker.R;

public class AlertDialogWithListener extends DialogFragment {

    private final Context context;
    private final DialogOnClickListener listener;
    private final int operation;
    private boolean showPositive = true;
    private boolean showNegative = true;

    private String name;

    private static final String TAG = "Confirmation";

    public interface DialogOnClickListener {
        void onConfirmClick();
        void onCancelClick();
    }

    public AlertDialogWithListener(Context context, int op, DialogOnClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.operation = op;
    }

    public AlertDialogWithListener(Context context, String name, int op, DialogOnClickListener listener) {
        this.context = context;
        this.name = name;
        this.operation = op;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String message = "";
        switch (operation) {
            case Constants.DIALOG_PERMISSION_LONG:
                message = context.getString(R.string.parse_require_permission_long);
                break;
            case Constants.DIALOG_PERMISSION_SHORT:
                message = context.getString(R.string.parse_require_permission_short);
                showNegative = false;
                break;
            case Constants.DIALOG_CREATE_EVENT:
                message = context.getString(R.string.parse_create_event_confirmation);
                break;
            case Constants.DIALOG_RECORD_DELETE_CONFIRMATION:
                message = context.getString(R.string.etc_ask_delete_confirmation, name);
                break;
            case Constants.DIALOG_PARSE_SINGLE_RECORD:
                message = context.getString(R.string.parse_single_item_confirmation);
                builder.setMessage(message).setPositiveButton(R.string.etc_do_nat_ask, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onConfirmClick();
                    }
                });
                showPositive = false;
                break;
            case Constants.DIALOG_DELETE_ACCOUNT:
                message = context.getString(R.string.setting_account_delete_warning);
                break;
        }

        if (showPositive) {
            builder.setMessage(message).setPositiveButton(R.string.etc_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    listener.onConfirmClick();
                }
            });
        }
        if (showNegative) {
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
