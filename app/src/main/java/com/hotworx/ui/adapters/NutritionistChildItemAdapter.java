package com.hotworx.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hotworx.R;
import com.hotworx.activities.DockActivity;
import com.hotworx.interfaces.OnNutritionistItemClick;
import com.hotworx.requestEntity.DayData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class NutritionistChildItemAdapter extends RecyclerView.Adapter<NutritionistChildItemAdapter.VH> {

    public List<DayData> dayData;
    public Context context;
    private static RequestOptions requestOptions = new RequestOptions();
    private final OnNutritionistItemClick mClickListener;
    public String mCurrentDate;

    public NutritionistChildItemAdapter(Context context, OnNutritionistItemClick clickListener, List<DayData> mDayData ,String currentDate) {
        this.context = context;
        this.mClickListener = clickListener;
        this.mCurrentDate = currentDate;
        this.dayData = mDayData;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item_nutritionist, parent, false);

        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, @SuppressLint("RecyclerView") int position) {
        DayData item = dayData.get(position);

        SimpleDateFormat ymdFormat = new SimpleDateFormat("MM/dd", Locale.ENGLISH);

        String gmtTime = ymdFormat.format(new Date());

        if (item.getProduct_name().isEmpty()) {
            holder.nameTv.setText("No food logged yet.\n");
            holder.qtyTv.setVisibility(View.GONE);
            holder.calTv.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
            holder.ivTrash.setVisibility(View.GONE);
            return;
        }

        if (mCurrentDate.equals(gmtTime)) {
            holder.ivTrash.setVisibility(View.VISIBLE);
        }

        holder.nameTv.setText(item.getProduct_name());
        holder.qtyTv.setText(item.getQty() + " " + item.getUnit());
        holder.calTv.setText(String.valueOf(Math.round(Float.parseFloat(item.getClaorie_count()))));
        if (!Objects.equals(item.getImg_url(), "")) {
            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(item.getImg_url())
                    .into(holder.imageView);
        }
        holder.itemView.setTag(item.getType());

        holder.ivTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(position, String.valueOf(holder.itemView.getTag()));
            }
        });

    }


    @Override
    public int getItemCount() {
        return dayData.size();
    }

    public void addAll(List<DayData> durationList) {
        this.dayData.clear();
        this.dayData.addAll(durationList);
        notifyDataSetChanged();
    }


    class VH extends RecyclerView.ViewHolder {

        TextView nameTv, calTv, qtyTv;
        ImageView imageView, ivTrash;


        public VH(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name);
            calTv = itemView.findViewById(R.id.calories);
            qtyTv = itemView.findViewById(R.id.quantity);
            imageView = itemView.findViewById(R.id.iv);
            ivTrash = itemView.findViewById(R.id.iv_trash);

        }
    }
}
