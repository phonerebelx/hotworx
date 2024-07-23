package com.hotworx.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hotworx.R;
import com.hotworx.activities.BaseActivity;
import com.hotworx.ui.views.TitleBar;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ViewPagerFragment extends BaseFragment {
    Unbinder unbinder;

    private MyPagerAdapter adapterViewPager;
    @BindView(R.id.vpPager)
    ViewPager vpPager;
    @BindView(R.id.nextBtn)
    TextView nextBtn;
    @BindView(R.id.skipBtn)
    TextView skipBtn;
    @BindView(R.id.tabDots)
    TabLayout tabLayout;

    int itemsize = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);
        unbinder = ButterKnife.bind(this, view);
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager());
        tabLayout.setupWithViewPager(vpPager, true);
        vpPager.setAdapter(adapterViewPager);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == itemsize - 1)
                    nextBtn.setText(R.string.done);
                else
                    nextBtn.setText(R.string.next);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
      //  unbinder.unbind();
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return itemsize;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ViewPagerChildFragment.newInstance(0, R.drawable.icon_1, getString(R.string.pager_calories_burn), getString(R.string.pager_calories_message));
                case 1:
                    return ViewPagerChildFragment.newInstance(1, R.drawable.icon_2, getString(R.string.pager_leader_board), getString(R.string.pager_leaderboard_message));
                case 2:
                    return ViewPagerChildFragment.newInstance(2, R.drawable.icon_3, getString(R.string.pager_rewards), getString(R.string.pager_rewards_message));
                default:
                    return null;
            }
        }
    }

    @OnClick({R.id.skipBtn, R.id.nextBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skipBtn:
                myDockActivity.emptyBackStack();
                myDockActivity.replaceDockableFragment(new HomeFragment());
                break;

            case R.id.nextBtn:
                if (vpPager.getCurrentItem() == itemsize - 1) {
                    nextBtn.setText(R.string.done);
                    myDockActivity.emptyBackStack();
                    myDockActivity.replaceDockableFragment(new HomeFragment());

                } else {
                    // Otherwise, select the previous step.
                    nextBtn.setText(R.string.next);
                    vpPager.setCurrentItem(vpPager.getCurrentItem() + 1, true);
                }
                break;
        }
    }

}
