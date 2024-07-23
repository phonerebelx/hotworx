package com.hotworx.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.hotworx.R;
import com.hotworx.activities.DockActivity;
import com.hotworx.requestEntity.LeaderBoardPOJO;

import java.util.List;

public class LeaderGlobalAdapter extends BaseAdapter {

    private List<LeaderBoardPOJO> mList;
    private DockActivity dockActivity;
    private LayoutInflater mLayoutInflater;

    private class ViewHolder {
        private TextView date;
        private TextView cal_burned;
        private TextView rank;
        private ImageView after_one_hour;
    }

    public LeaderGlobalAdapter(DockActivity dockActivity, List<LeaderBoardPOJO> caloriesObjectList) {
        this.dockActivity = dockActivity;
        mList = caloriesObjectList;
        mLayoutInflater = (LayoutInflater) dockActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mList != null)
            return mList.size();
        else
            return 0;
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
            view = mLayoutInflater.inflate(R.layout.list_leader_board_item, viewGroup, false);
            holder.date = (TextView) view.findViewById(R.id.leader_tv_name);
            holder.cal_burned = (TextView) view.findViewById(R.id.leader_tv_cal);
            holder.rank = (TextView) view.findViewById(R.id.leader_tv_rank);
            holder.after_one_hour = (ImageView) view.findViewById(R.id.leader_tv_dash);

            view.setTag(holder);
        } else
            holder = (ViewHolder) view.getTag();

        if (position % 2 == 1) {
            view.setBackgroundColor(Color.WHITE);
        } else {
            view.setBackgroundColor(Color.WHITE);
        }


        LeaderBoardPOJO object = mList.get(position);

        if (object != null) {

            if (object.getUsername() != null)
                holder.date.setText(object.getUsername());

            if (object.getTotalCaloriesBurnt() != null)
                holder.cal_burned.setText(this.dockActivity.getString(R.string.leaderboard_cal_burnt, object.getTotalCaloriesBurnt()));

            if (object.getReward() != null) {
                switch (object.getReward()) {
                    case "gold":
                        holder.rank.setVisibility(View.GONE);
                        holder.after_one_hour.setVisibility(View.VISIBLE);
                        holder.after_one_hour.setImageResource(R.drawable.goldtrophy);
                        break;
                    case "silver":
                        holder.rank.setVisibility(View.GONE);
                        holder.after_one_hour.setVisibility(View.VISIBLE);
                        holder.after_one_hour.setImageResource(R.drawable.silvertrophy);
                        break;
                    case "bronze":
                        holder.rank.setVisibility(View.GONE);
                        holder.after_one_hour.setVisibility(View.VISIBLE);
                        holder.after_one_hour.setImageResource(R.drawable.bronzetrophy);
                        break;
                    default:
                        holder.rank.setVisibility(View.VISIBLE);
                        holder.after_one_hour.setVisibility(View.GONE);
                        holder.rank.setText(object.getReward());
                        break;
                }
            } else holder.after_one_hour.setImageResource(R.drawable.hyphen);
        }

        return view;
    }
}
