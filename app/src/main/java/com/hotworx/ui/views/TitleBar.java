package com.hotworx.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hotworx.R;
import com.hotworx.helpers.BasePreferenceHelper;


public class TitleBar extends RelativeLayout {

    private ImageView txtTitle;
    private TextView txtTitle2;
    private ImageView btnLeft;
    private ImageView ivSync;
    private ImageView ivPassio;
    private ImageView ivBrivo;
    private ImageView ivNotification;
    private TextView tvNotificationNo;
    public BasePreferenceHelper prefHelper;
    private OnClickListener menuButtonListener;
    private OnClickListener backButtonListener;
    private OnClickListener syncButtonListener;
    private OnClickListener brivoButtonListener;
    private OnClickListener notificationButtonListener;
    private OnClickListener passioButtonListener;


    private Context context;


    public TitleBar(Context context) {
        super(context);
        this.context = context;
        initLayout(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
    }

    private void bindViews() {
        txtTitle = this.findViewById(R.id.txt_subHead);
        txtTitle2 = this.findViewById(R.id.txt_subHead2);
        ivNotification = this.findViewById(R.id.ivNotification);
        tvNotificationNo = this.findViewById(R.id.tvNotificationNo);

        btnLeft = this.findViewById(R.id.btnLeft);
        ivSync = this.findViewById(R.id.ivSync);
        ivPassio = this.findViewById(R.id.ivPassio);
        ivBrivo = this.findViewById(R.id.ivBrivo);
    }

    private void initLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header_main, this);
        prefHelper = new BasePreferenceHelper(context);

        bindViews();
    }

    public void showBackButton() {
        btnLeft.setImageResource(R.drawable.ic_arrow_black);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(backButtonListener);
    }

    public void showBrivoBtn() {
        ivBrivo.setImageResource(R.drawable.solar_item);
        ivBrivo.setVisibility(View.VISIBLE);
        ivBrivo.setOnClickListener(brivoButtonListener);
    }
    public void hideBrivoBtn(){
        ivBrivo.setImageResource(0);
        ivBrivo.setVisibility(View.INVISIBLE);
    }

    public void showSyncBtn() {
        ivSync.setImageResource(R.drawable.sync_btn_img_removebg);
        ivSync.setVisibility(View.VISIBLE);
        ivSync.setOnClickListener(brivoButtonListener);
    }

    public void showPassioBtn() {
//        ivPassio.setImageResource(R.drawable.ic_food_scanner);
        ivPassio.setVisibility(View.VISIBLE);
        ivPassio.setOnClickListener(passioButtonListener);
    }

    public void hideSyncBtn(){
        ivSync.setImageResource(0);
        ivSync.setVisibility(View.INVISIBLE);
    }
    public void showNotificationBtn(String notificationValue){
        tvNotificationNo.setVisibility(View.VISIBLE);
        tvNotificationNo.setText(notificationValue);
        ivNotification.setImageResource(R.drawable.notification_bell);
        ivNotification.setVisibility(View.VISIBLE);
        ivNotification.setOnClickListener(notificationButtonListener);
    }


    public void hideNotificationText(){
        tvNotificationNo.setVisibility(View.INVISIBLE);
    }

    public void hideNotificationBtn(){
        tvNotificationNo.setVisibility(View.INVISIBLE);
        ivNotification.setImageResource(0);
        ivNotification.setVisibility(View.INVISIBLE);
    }

    public void showMenuButton() {
        btnLeft.setImageResource(R.drawable.ic_menu);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(menuButtonListener);
    }

    public void setSubHeading(String heading) {
        if (heading != getResources().getString(R.string.activity)){
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle2.setVisibility(View.GONE);
            txtTitle.setImageResource(R.drawable.hotworks_heading);
        }else if (heading == getResources().getString(R.string.activity)){
            txtTitle.setVisibility(View.GONE);
            txtTitle2.setVisibility(View.VISIBLE);
            txtTitle2.setText(heading);
        }
    }


    public String getSubHeading() {
        return "";
    }

    public void showTitleBar() {
        this.setVisibility(View.VISIBLE);
    }


    public void hideTitleBar() {
        this.setVisibility(View.GONE);
    }

    public void setMenuButtonListener(OnClickListener listener) {
        menuButtonListener = listener;
    }

    public void setBackButtonListener(OnClickListener listener) {
        backButtonListener = listener;
    }

    public void setSyncButtonListener(OnClickListener listener) {
        syncButtonListener = listener;
    }
    public void setBrivoButtonListener(OnClickListener listener) { brivoButtonListener = listener; }
    public void setPassioButtonListener(OnClickListener listener) {
        passioButtonListener = listener;
    }

  public void setNotificationButtonListener(OnClickListener listener) {
      notificationButtonListener = listener;
    }


}
