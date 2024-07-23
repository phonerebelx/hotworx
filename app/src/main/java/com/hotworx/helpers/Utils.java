package com.hotworx.helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.utils.MPPointF;
import com.hotworx.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created on 9/26/2017.
 */

public class Utils {
    public static String getTAG(Object o) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();

        int position = 0;
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].getFileName().contains(o.getClass().getSimpleName())
                    && !elements[i+1].getFileName().contains(o.getClass().getSimpleName())){
                position = i;
                break;
            }
        }

        StackTraceElement element = elements[position];
        String className = element.getFileName().replace(".java", "");
        return "[" + className + "](" + element.getMethodName() + ":" + element.getLineNumber() + ")";
    }

    public static String currencyFormat(String amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
        return formatter.format(Double.parseDouble(amount));
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static boolean isNotNullEmpty(@Nullable String string) {
        return !(string == null || string.trim().length() == 0);
    }

    public static int getParsedInteger(String discountAmount) {
        try {
            return Integer.parseInt(discountAmount);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static double getParsedDouble(String val) {
        try {
            return Double.parseDouble(val);
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public static long getParsedLong(String val) {
        try {
            return Long.parseLong(val);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static float getParsedFloat(String val) {
        try {
            return Float.parseFloat(val);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static void customToast(Context context, String message) {

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 0);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_custom_view, null);
        TextView text = (TextView) layout.findViewById(R.id.toast_tv);
        text.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        if (message != null) {
            text.setText(message);
            toast.setView(layout);
            toast.show();
        }
    }

    public static void customToastGreen(Context context, String message) {

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 0);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_custom_green_view, null);
        TextView text = (TextView) layout.findViewById(R.id.toast_tv);
        text.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        if (message != null) {
            text.setText(message);
            toast.setView(layout);
            toast.show();
        }
    }

    public static Bitmap getScreenshotOfActivity(Activity activity, int requestCode) {
        if (!isStoragePermissionGranted(activity, requestCode))
            return null;

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 70;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            Uri uri = Uri.fromFile(imageFile);
            return bitmap;

            //return uri;

        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }


    private static boolean isStoragePermissionGranted(Activity activity, int requestCode) {

        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.
                        requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                return false;
            }
        } else {
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }


    public static void drawXAxisValue(
            Canvas canvas,
            String text,
            float x,
            float y,
            Paint paint,
            MPPointF anchor,
            float angleDegrees
    ) {
        // Rotate the canvas if necessary
        if (angleDegrees != 0f) {
            canvas.save();
            canvas.rotate(angleDegrees, x, y);
        }

        // Draw the text on the canvas
        if (anchor != null) {
            canvas.drawText(text, x, y, paint);
        } else {
            // If no anchor point is specified, draw at the provided coordinates
            canvas.drawText(text, x, y, paint);
        }

        // Restore the canvas to its original state after rotation
        if (angleDegrees != 0f) {
            canvas.restore();
        }
    }


    public static float convertDpToPixel(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


}
