package com.hotworx.helpers;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hotworx.R;


public class ImageLoaderHelper {
    private static ImageLoaderHelper Instance;
    private static Context ctx;
    private static RequestOptions requestOptions;

    private ImageLoaderHelper(Context context){
        ctx = context;
    }

    public static synchronized ImageLoaderHelper getInstance(){
        if(Instance==null){
            Instance = new ImageLoaderHelper(ctx);
            requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.placeholder_image);
            requestOptions.error(R.drawable.placeholder_image);
        }
        return Instance;
    }


    public static void loadImageWithGlide(Context context, String url, ImageView imageView) {
        if (imageView == null || TextUtils.isEmpty(url)) return;
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(url)
                .into(imageView);
    }

    public static void loadUriWithGlide(Context context, Uri uri, ImageView imageView) {
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(uri)
                .into(imageView);
    }


}
