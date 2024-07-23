package com.hotworx.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.hotworx.R;
import com.hotworx.models.NavigationItem;

import java.util.ArrayList;


public class LeftNavigationBinderAdapter extends BaseAdapter {

    LayoutInflater inflter;
    ArrayList<NavigationItem> drawerItems;
    Activity activity;

    public LeftNavigationBinderAdapter(Activity activity, ArrayList<NavigationItem> drawerItems) {
        this.drawerItems = drawerItems;
        this.activity = activity;
        inflter = (LayoutInflater.from(activity));

    }


    @Override
    public int getCount() {
        return drawerItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflter.inflate(R.layout.item_leftmenu, null);
        TextView textView = (TextView) view.findViewById(R.id.text);
        TextView catdTextView = (TextView) view.findViewById(R.id.tvTextView);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        ConstraintLayout clInviteFriend = (ConstraintLayout) view.findViewById(R.id.clInviteFriend);
        textView.setText(drawerItems.get(position).getText());
        image.setImageResource(drawerItems.get(position).getDrawable());

        if (drawerItems.get(position).getText() == R.string.invite){
            textView.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            catdTextView.setText( R.string.invite_friend);
            clInviteFriend.setVisibility(View.VISIBLE);

        } else if (drawerItems.get(position).getText() == R.string.business ) {
            textView.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            catdTextView.setText( R.string.business);
            clInviteFriend.setVisibility(View.VISIBLE);
        }


        return view;
    }

}
