package com.hotworx.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hotworx.R;
import com.hotworx.activities.DockActivity;
import com.hotworx.requestEntity.CaloriesObject;
import com.hotworx.ui.fragments.ActivityScreen.ActivityDetailFragment;
import com.hotworx.ui.fragments.ActivityScreen.ActivityScreenFragment;
import com.hotworx.ui.fragments.LeaderBoard.LeaderBoardFragment;

import java.util.List;

public class ActivityListAdapter extends BaseAdapter {

    private List<CaloriesObject> mList;
    private  DockActivity dockActivity;
    private LayoutInflater mLayoutInflater;

    private class ViewHolder {
        private TextView date;
        private TextView cal_burned;
        private TextView after_one_hour;
        private LinearLayout row_container;
    }

    public ActivityListAdapter(DockActivity dockActivity, List<CaloriesObject> caloriesObjectList) {
        this.dockActivity = dockActivity;
        mList = caloriesObjectList;
        mLayoutInflater = (LayoutInflater) dockActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.list_activity_item, viewGroup, false);
            holder.date = (TextView) view.findViewById(R.id.ac_tv_date);
            holder.cal_burned = (TextView) view.findViewById(R.id.ac_tv_cal);
            holder.after_one_hour = (TextView) view.findViewById(R.id.ac_tv_after);
            holder.row_container = (LinearLayout) view.findViewById(R.id.activity_forward);

            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        if (position % 2 == 1) {
            view.setBackgroundColor(Color.WHITE);
        } else {
            view.setBackgroundColor(Color.WHITE );
        }

        final CaloriesObject object = mList.get(position);

        if (object != null) {

            if (object.getDate() != null)
                holder.date.setText(object.getDate());

            if (object.getCalories_forty_session_burned() != null)
                holder.cal_burned.setText(object.getCalories_forty_session_burned());

            if (object.getCalories_sixty_session_burned_hour() != null)
                holder.after_one_hour.setText(object.getCalories_sixty_session_burned_hour() );

            holder.row_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString("parent_activity_id",object.getActivity_id());
                    ActivityDetailFragment activityDetailFragment = new ActivityDetailFragment();
                    activityDetailFragment.setArguments(args);
                    dockActivity.replaceDockableFragment(activityDetailFragment);
                }
            });
        }

        return view;
    }
}
