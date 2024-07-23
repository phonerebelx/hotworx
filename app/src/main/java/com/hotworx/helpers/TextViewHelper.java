package com.hotworx.helpers;

import android.net.Uri;
import android.text.Html;
import android.widget.TextView;



public class TextViewHelper {
    public static void setText(TextView textView, String text) {
        if (textView == null) return;
        if (Utils.isNotNullEmpty(text)) {
            textView.setText(text);
        }
    }

    public static void setHtmlText(TextView textView, String textToBold, String val) {
        if (textView == null) return;
        if (Utils.isNotNullEmpty(val)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                textView.setText(Html.fromHtml(textToBold, Html.FROM_HTML_MODE_COMPACT, ));
//            } else
                textView.setText(Html.fromHtml("<b>" + textToBold + "</b>" + val));
        }
    }

}
