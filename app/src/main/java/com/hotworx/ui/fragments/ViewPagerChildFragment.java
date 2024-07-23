package com.hotworx.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ViewPagerChildFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.pagerHeading)
    TextView pagerHeading;
    @BindView(R.id.pagerDesc)
    TextView pagerDesc;
    @BindView(R.id.pagerImg)
    ImageView pagerImg;
    private String title, desc;
    private int page, drawable;

    public static ViewPagerChildFragment newInstance(int page, int drawable, String title, String desc) {
        ViewPagerChildFragment fragmentFirst = new ViewPagerChildFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putInt("drawable", drawable);
        args.putString("someTitle", title);
        args.putString("desc", desc);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        drawable = getArguments().getInt("drawable");
        title = getArguments().getString("someTitle");
        desc = getArguments().getString("desc");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_child_viewpager,container,false);
        unbinder = ButterKnife.bind(this,view);
        pagerHeading.setText(title);
        pagerImg.setBackgroundResource(drawable);
        pagerDesc.setText(desc);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // unbinder.unbind();
    }
}
