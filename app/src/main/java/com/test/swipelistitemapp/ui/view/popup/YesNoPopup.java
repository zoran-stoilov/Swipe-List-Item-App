package com.test.swipelistitemapp.ui.view.popup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Zoran on 09.03.2016.
 */
public class YesNoPopup extends DialogFragment {

    private String title;
    private String msg;
    private YesNoCallback callback;

    public static YesNoPopup newInstance(String title, String msg, String actionYes, String actionNo, YesNoCallback callback) {

        YesNoPopup f = new YesNoPopup();
        f.setCallback(callback);
        Bundle args = new Bundle();
        args.putString("msg", msg);
        args.putString("title", title);
        args.putString("actionYes", actionYes);
        args.putString("actionNo", actionNo);
        f.setArguments(args);
        return f;
    }

    private void setCallback(YesNoCallback callback) {
        this.callback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int themeResId;
        if (Build.VERSION.SDK_INT >= 22) {
            themeResId = android.R.style.Theme_DeviceDefault_Light_Dialog_Alert;
        } else {
            themeResId = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), themeResId);
        title = getArguments().getString("title");
        if (title != null) {
            builder.setTitle(title);
        }
        msg = getArguments().getString("msg");
        if (msg != null) {
            builder.setMessage(msg);
        }
        builder.setNegativeButton(getArguments().getString("actionNo"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                callback.cancel();
            }
        });

        builder.setPositiveButton(getArguments().getString("actionYes"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                callback.action();
            }
        });

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public interface YesNoCallback {

        void action();

        void cancel();
    }
}
