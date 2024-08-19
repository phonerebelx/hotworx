package com.hotworx.models;


import androidx.fragment.app.Fragment;

import com.hotworx.ui.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class NavigationItem {


    private int mText;
    private int mDrawable;
    private BaseFragment fragment;
    private String url;
    private String action;
    private List<NavigationItem> subItems; // Add this field


    public NavigationItem(int text, int drawable,BaseFragment fragment,String url,String action) {
        mText = text;
        mDrawable = drawable;
        this.fragment = fragment;
        this.url = url;
        this.action = action;
        this.subItems = new ArrayList<>(); // Initialize subItems
    }

    public int getText() {
        return mText;
    }

    public void setText(int text) {
        mText = text;
    }

    public int getDrawable() {
        return mDrawable;
    }

    public void setDrawable(int drawable) {
        mDrawable = drawable;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    // Add a method to set child items
    public void setSubItems(List<NavigationItem> subItems) {
        this.subItems = subItems;
    }

    public List<NavigationItem> getSubItems() {
        return subItems;
    }
}
