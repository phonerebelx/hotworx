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

import com.bumptech.glide.request.RequestOptions;
import com.hotworx.R;
import com.hotworx.activities.DockActivity;
import com.hotworx.interfaces.OnItemClick;
import com.hotworx.interfaces.OnNutritionistItemClick;
import com.hotworx.requestEntity.DayData;
import com.hotworx.requestEntity.GetIntermittentFood;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InterNutritionistAdapter extends RecyclerView.Adapter<InterNutritionistAdapter.VH> {

    private final DockActivity myDockActivity;
    public ArrayList<GetIntermittentFood.Food_list> dayData = new ArrayList<>();
    public Context context;
    private static RequestOptions requestOptions = new RequestOptions();
    private final OnNutritionistItemClick mClickListener;
    private final OnItemClick onItemClick;
    public String mCurrentDate;

    // An object of RecyclerView.RecycledViewPool
    // is created to share the Views
    // between the child and
    // the parent RecyclerViews
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public InterNutritionistAdapter(DockActivity myDockActivity, Context context, OnNutritionistItemClick clickListener, String currentDate, OnItemClick mOnItemClick) {
        this.myDockActivity = myDockActivity;
        this.context = context;
        this.mClickListener = clickListener;
        this.mCurrentDate = currentDate;
        this.onItemClick = mOnItemClick;
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
        GetIntermittentFood.Food_list item = dayData.get(position);

        SimpleDateFormat ymdFormat = new SimpleDateFormat("MM/dd", Locale.ENGLISH);

        String gmtTime = ymdFormat.format(new Date());

        holder.imageView.setVisibility(View.VISIBLE);

        holder.name.setText(item.getProduct_name());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                holder
                        .childRv
                        .getContext(),
                LinearLayoutManager.VERTICAL,
                false);


        layoutManager.setInitialPrefetchItemCount(dayData.size());
        ArrayList<GetIntermittentFood.Food_list> list = new ArrayList<GetIntermittentFood.Food_list>();

        GetIntermittentFood.Food_list foodItem = new GetIntermittentFood.Food_list();
        foodItem.setTotal_qty(item.getTotal_qty());
        list.add(foodItem);

        InterNutritionistChildAdapter childItemAdapter = new InterNutritionistChildAdapter(context,mClickListener,list,mCurrentDate);
        holder.childRv.setLayoutManager(layoutManager);
        holder.childRv.setAdapter(childItemAdapter);
        holder.childRv.setRecycledViewPool(viewPool);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onClick(String.valueOf(item.getProduct_name()), String.valueOf(item.getProduct_id()));
            }
        });


    }


    @Override
    public int getItemCount() {
        return dayData.size();
    }

    public void addAll(List<GetIntermittentFood.Food_list> durationList) {
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
