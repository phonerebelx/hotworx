package com.hotworx.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hotworx.R;
import com.hotworx.activities.DockActivity;
import com.hotworx.interfaces.OnNutritionistItemClick;
import com.hotworx.requestEntity.DayData;
import com.hotworx.requestEntity.ParentNutritionistItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NutritionistParentItemAdapter extends RecyclerView.Adapter<NutritionistParentItemAdapter.VH> {

    private final DockActivity myDockActivity;
    public ArrayList<ParentNutritionistItem> dayData = new ArrayList<>();
    public Context context;
    private static RequestOptions requestOptions = new RequestOptions();
    private final OnNutritionistItemClick mClickListener;
    public String mCurrentDate;

    // An object of RecyclerView.RecycledViewPool
    // is created to share the Views
    // between the child and
    // the parent RecyclerViews
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public NutritionistParentItemAdapter(DockActivity myDockActivity, Context context, OnNutritionistItemClick clickListener, String currentDate) {
        this.myDockActivity = myDockActivity;
        this.context = context;
        this.mClickListener = clickListener;
        this.mCurrentDate = currentDate;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(myDockActivity)
                .inflate(R.layout.parent_item_nutritionist, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, @SuppressLint("RecyclerView") int position) {
        ParentNutritionistItem item = dayData.get(position);

        SimpleDateFormat ymdFormat = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
        ;
        //ymdFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = ymdFormat.format(new Date());

        if (mCurrentDate.equals(gmtTime)) {
            holder.imageView.setVisibility(View.VISIBLE);
        }

        holder.name.setText(item.getParentItemTitle());
        holder.totalCal.setText(item.getTotalCal());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                holder
                        .childRv
                        .getContext(),
                LinearLayoutManager.VERTICAL,
                false);

        layoutManager.setInitialPrefetchItemCount(item.getChildItemList().size());
        holder.itemView.setTag("add"+item.getParentItemTitle());
        NutritionistChildItemAdapter childItemAdapter = new NutritionistChildItemAdapter(context,mClickListener,item.getChildItemList(),mCurrentDate);
        holder.childRv.setLayoutManager(layoutManager);
        holder.childRv.setAdapter(childItemAdapter);
        holder.childRv.setRecycledViewPool(viewPool);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
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

    public void addAll(List<ParentNutritionistItem> durationList) {
        this.dayData.clear();
        this.dayData.addAll(durationList);
        notifyDataSetChanged();
    }


    class VH extends RecyclerView.ViewHolder {

        TextView name, totalCal;
        ImageView imageView;
        RecyclerView childRv;

        public VH(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_breakfast);
            imageView = itemView.findViewById(R.id.add_breakfast);
            totalCal = itemView.findViewById(R.id.total_cal_breakfast);
            childRv = itemView.findViewById(R.id.child_rv);

        }
    }
}

