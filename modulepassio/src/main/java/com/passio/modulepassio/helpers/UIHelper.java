package com.passio.modulepassio.helpers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils.TruncateAt;
import android.text.format.DateFormat;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.passio.modulepassio.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UIHelper {
    private static long lastClickTime;
    private static Toast toast;
    private static Dialog dialog;

    public static void showLongToastInCenter(Context ctx, int messageId) {
        if (ctx == null) return;
        if (toast != null) toast.cancel();
        toast = Toast.makeText(ctx, messageId, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showLongToastInCenter(Context ctx, String message) {
        if (ctx == null || message == null) return;
        if (toast != null) toast.cancel();
        toast = Toast.makeText(ctx, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showShortToastInCenter(Context ctx, String message) {
        if (ctx == null || message == null) return;
        if (toast != null) toast.cancel();
        toast = Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showConnectionFailedToast(Context ctx) {
        showLongToastInCenter(ctx, R.string.msg_connection_failed);
    }

    public static void showConnectionErrorToast(Context ctx) {
        showLongToastInCenter(ctx, R.string.msg_connection_error);
    }

    public static void hideSoftKeyboard(Context context, EditText editText) {

        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }

    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public static void showAlertDialog(String message, CharSequence title,
                                       Context context, DialogInterface.OnClickListener dialogPositive, DialogInterface.OnClickListener dialogNegitive) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title).setCancelable(false)
                .setPositiveButton(R.string.yes, dialogPositive)
                .setNegativeButton(R.string.no, dialogNegitive);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null)
            return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            // Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    public static void textMarquee(TextView txtView) {
        // Use this to marquee Textview inside listview

        txtView.setEllipsize(TruncateAt.END);
        // Enable to Start Scroll

        // txtView.setMarqueeRepeatLimit(-1);
        // txtView.setHorizontallyScrolling(true);
        // txtView.setSelected(true);
    }

    public static void dimBehind(Dialog dialog) {
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        dialog.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
    }

    public static boolean doubleClickCheck() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return false;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return true;
    }

    public static Dialog createQuitDialog(Activity activity,
                                          DialogInterface.OnClickListener dialogPositive, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.yes, dialogPositive)
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                            }
                        });

        return builder.create();
    }

    public static Dialog createForceLogoutDialog(Activity activity,
                                          DialogInterface.OnClickListener dialogPositive, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, dialogPositive);

        return builder.create();
    }

    public static Dialog createAlertDialog(Activity activity,
                                                 DialogInterface.OnClickListener dialogPositive, String message, String positiveButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveButton, dialogPositive);

        return builder.create();
    }

    public static String getFormattedDate(String inputDate, String inputDateFormat, String outputDateFormat) {
        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat(inputDateFormat);
            Date date = sdfInput.parse(inputDate);
            SimpleDateFormat sdfOutput = new SimpleDateFormat(outputDateFormat);
            return sdfOutput.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static Date getDate(String dateString, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return date;
    }

}
