package com.passio.modulepassio.helpers;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.utils.MPPointF;
import com.passio.modulepassio.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Date;

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

    public static float convertDpToPixel(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
