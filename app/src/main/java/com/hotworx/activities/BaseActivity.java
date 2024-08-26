package com.hotworx.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hotworx.helpers.BasePreferenceHelper;

public abstract class BaseActivity extends AppCompatActivity {
    protected BasePreferenceHelper prefHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefHelper = new BasePreferenceHelper(this);
    }

}
