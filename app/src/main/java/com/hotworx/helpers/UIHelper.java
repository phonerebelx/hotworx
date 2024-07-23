package com.hotworx.helpers;

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

import com.hotworx.R;
import com.hotworx.global.Constants;
import com.hotworx.interfaces.DialogBoxCallBack;
import com.hotworx.interfaces.InternetDialogBoxInterface;

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

    public static void showWorkoutConfirmationAlertDialog(String message, CharSequence title,
                                       Context context, DialogInterface.OnClickListener dialogPositive, DialogInterface.OnClickListener dialogNegitive) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title).setCancelable(false)
                .setPositiveButton(R.string.workout, dialogPositive)
                .setNegativeButton(R.string.after_burn, dialogNegitive);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showCustomDialog(final Activity context, String titleTxt, String descTxt, final DialogBoxCallBack callback) {
        if (!context.isFinishing() && dialog != null) {
            dialog.dismiss();
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
        TextView description = (TextView) dialog.findViewById(R.id.dialog_description);
        TextView btnRecordManually = (TextView) dialog.findViewById(R.id.btnRecordManually);
        LinearLayout btnRefresh = (LinearLayout) dialog.findViewById(R.id.btnRefresh);
        title.setText(titleTxt);
        description.setText(descTxt);
        TextView ok = (TextView) dialog.findViewById(R.id.right_button);
        TextView cancel = (TextView) dialog.findViewById(R.id.left_button);
        dialog.setCanceledOnTouchOutside(false);

        btnRecordManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             dialog.dismiss();
             callback.onRedirect();
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onRefresh();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onManual();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callback.onPositive();
            }
        });


        if (!dialog.isShowing())
            dialog.show();
    }


    public static void showNoInternetDialog(final Context context, String titleTxt, String descTxt,String left_button,String right_button, final InternetDialogBoxInterface callback) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_box_internet);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
        TextView description = (TextView) dialog.findViewById(R.id.dialog_description);
        title.setText(titleTxt);
        description.setText(descTxt);
        TextView ok = (TextView) dialog.findViewById(R.id.right_button);
        TextView cancel = (TextView) dialog.findViewById(R.id.left_button);
        cancel.setText(left_button);
        ok.setText(right_button);
        dialog.setCanceledOnTouchOutside(false);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onNegative();

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callback.onPositive();
            }
        });

            dialog.show();
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static int getScreenWidth(Activity ctx) {
        Display display = ctx.getWindowManager().getDefaultDisplay();

        if (OSHelper.hasHoneycombMR2()) {
            Point size = new Point();
            display.getSize(size);
            return size.x;
        } else {
            return display.getWidth();
        }

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

    public static int getYear(String inputDate){
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        int year=-1;
        try {
            Date date = format.parse(inputDate);
            year = Integer.parseInt((String) DateFormat.format("yyyy", date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return year;
    }

    public static int getMonth(String inputDate){
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        int month=-1;
        try {
            Date date = format.parse(inputDate);
            month = Integer.parseInt((String) DateFormat.format("MM", date))-1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return month;
    }

    public static int getDay(String inputDate){
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        int day=-1;
        try {
            Date date = format.parse(inputDate);
            day = Integer.parseInt((String) DateFormat.format("dd", date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return day;
    }

    public static int getHours(String inputDate) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        Calendar calendar = null;
        try {
            Date  date = format.parse(inputDate);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert calendar != null;
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinutes(String inputDate) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        Calendar calendar = null;
        try {
            Date  date = format.parse(inputDate);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert calendar != null;
        return calendar.get(Calendar.MINUTE);
    }

    public static String convertTimeIntoUtc(String dateTxt){
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = format.parse(dateTxt);
            format.setTimeZone(TimeZone.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(date);
    }

    public static String convertTimeIntoLocal(String dateTxt){
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = format.parse(dateTxt);
            format.setTimeZone(TimeZone.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(date);
    }

    public static String convert24HourTo12Hour(String dateTxt){
        String time = "";
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_TIME);
            final Date dateObj = sdf.parse(dateTxt);
            time = new SimpleDateFormat(Constants.DATE_FORMAT_TIME_AM_PM).format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String convert12HourTo24Hour(String dateTxt) {
        String time = "";
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_TIME_AM_PM);
            final Date dateObj = sdf.parse(dateTxt);
            time = new SimpleDateFormat(Constants.DATE_FORMAT_TIME).format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
//        java.text.DateFormat date = new SimpleDateFormat("hh:mm:ss a");
        java.text.DateFormat date = new SimpleDateFormat("hh:mm a");
        return date.format(currentLocalTime);
    }

//    public static Boolean checkIfTimeWithinDay(String startTime, String endTime, String comparetime) {
//
//        int secsInADay = 86400;
//
//        int endTimeInSecs = convert12HourTimeToSeconds(endTime);
//        if (endTimeInSecs < 43200) { endTimeInSecs += secsInADay; }
//        int startTimeInSecs = convert12HourTimeToSeconds(startTime);
//        endTimeInSecs = Math.abs(endTimeInSecs - startTimeInSecs);
////        int compareTime = secsInADay;
//        int compareTime = convert12HourTimeToSeconds(comparetime);
//        compareTime = Math.abs(compareTime - startTimeInSecs);
//
//
//        if (endTimeInSecs < compareTime) {
//            System.out.println("true");
//            return true;
//        } else {
//            System.out.println("false");
//            return false;
//        }
//    }

    public static Boolean checkIfTimeWithinDay(String startTime, String endTime, int difference, String comparetime) {

//        int secsInADay = 86400;

        int endTimeInSecs = convert12HourTimeToSeconds(endTime);
//        if (endTimeInSecs < 43200) { endTimeInSecs += secsInADay; }
        int startTimeInSecs = convert12HourTimeToSeconds(startTime);
        endTimeInSecs = Math.abs(endTimeInSecs - startTimeInSecs);
        if (endTimeInSecs <= startTimeInSecs) {
            endTimeInSecs += difference;
        }
//        int compareTime = secsInADay;
        int compareTime = convert12HourTimeToSeconds(comparetime);
        compareTime = Math.abs(compareTime - startTimeInSecs);

        int startTimeInSecs2 = convert12HourTimeToSeconds(startTime);
        int compareTime2 = convert12HourTimeToSeconds(comparetime);


        if (endTimeInSecs > compareTime && compareTime2 <= startTimeInSecs2) {
            System.out.println("true");
            return true;
        } else {
            System.out.println("false");
            return false;
        }

//        if (endTimeInSecs < compareTime && compareTime2 >= startTimeInSecs2) {
//            System.out.println("true");
//            return true;
//        } else {
//            System.out.println("false");
//            return false;
//        }
    }

    public static int convert12HourTimeToSeconds(String time) {
        int totalSeconds = 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date myDate = sdf.parse(time);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            int hourToSeconds = calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60;
            int minutesToSeconds = calendar.get(Calendar.MINUTE) * 60;

            totalSeconds = hourToSeconds + minutesToSeconds; //+ seconds;

        } catch (Exception e) {
        }
        return totalSeconds;
    }


}
