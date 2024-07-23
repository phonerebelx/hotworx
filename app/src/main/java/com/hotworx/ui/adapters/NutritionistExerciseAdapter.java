package com.hotworx.ui.adapters;

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
import com.hotworx.requestEntity.ExerciseData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NutritionistExerciseAdapter extends RecyclerView.Adapter<NutritionistExerciseAdapter.VH> {

    private List<ExerciseData> dayData = new ArrayList<>();
    private Context context;
    private static RequestOptions requestOptions = new RequestOptions();
    public String mCurrentDate;
    private final OnNutritionistItemClick mClickListener;

    public NutritionistExerciseAdapter(Context context,String currentDate, OnNutritionistItemClick clickListener) {
        this.mCurrentDate = currentDate;
        this.mClickListener = clickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_item_nutritionist, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ExerciseData item = dayData.get(position);

        SimpleDateFormat ymdFormat = new SimpleDateFormat("MM/dd", Locale.ENGLISH);;
        //ymdFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = ymdFormat.format(new Date());

        if (mCurrentDate.equals(gmtTime)) {
            holder.ivTrash.setVisibility(View.VISIBLE);
        }

        if (item.getSession_name().isEmpty()) {
            holder.nameTv.setText("No exercises logged yet.\n");
            holder.qtyTv.setVisibility(View.GONE);
            holder.calTv.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
            holder.ivTrash.setVisibility(View.GONE);
            return;
        }

        holder.itemView.setTag("exercise");
        holder.nameTv.setText(item.getSession_name());
        holder.qtyTv.setText(item.getTime_in_min() + "min");
        holder.calTv.setText(String.valueOf(Math.round(Float.parseFloat(item.getCalorie_burn()))));
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(item.getImg_url())
                .placeholder(R.drawable.ic_user)
                .into(holder.imageView);


        holder.ivTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(holder.getAdapterPosition(),"exercise");
            }
        });

    }

    @Override
    public int getItemCount() {
        return dayData.size();
    }

    public void addAll(List<ExerciseData> durationList) {
        dayData.clear();
        dayData.addAll(durationList);
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
