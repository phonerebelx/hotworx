package com.hotworx.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hotworx.R;
import com.hotworx.activities.DockActivity;
import com.hotworx.interfaces.OnItemClickInterface;
import com.hotworx.requestEntity.Branded;
import com.hotworx.requestEntity.NintyDaysCaloriesSession;
import com.hotworx.retrofit.GsonFactory;

import java.util.List;

public class NutritionistSummaryAdapter extends RecyclerView.Adapter<NutritionistSummaryAdapter.VH> {

    private final DockActivity myDockActivity;
    private List<NintyDaysCaloriesSession> dayData;
    private final Context context;
    private static final RequestOptions requestOptions = new RequestOptions();


    public NutritionistSummaryAdapter(DockActivity myDockActivity, List<NintyDaysCaloriesSession> durationList, Context context) {
        this.myDockActivity = myDockActivity;
        this.dayData = durationList;
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(myDockActivity)
                .inflate(R.layout.item_nutritionist_summary, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        NintyDaysCaloriesSession item = dayData.get(position);

        holder.date.setText(item.getDate());
        holder.calReading.setText(item.getCalorie_reading());
        int bmrNum = Integer.parseInt(item.getBmr_deficit());
        if (bmrNum > 0){
            holder.avgBmr.setTextColor(context.getResources().getColor(R.color.colorRed));
        }
        holder.avgBmr.setText(item.getBmr_deficit() + "%");

    }

    @Override
    public int getItemCount() {
        return dayData.size();
    }

    public void addAll(List<NintyDaysCaloriesSession> durationList) {
        this.dayData = durationList;
    }

    class VH extends RecyclerView.ViewHolder {
        TextView date;
        TextView calReading;
        TextView avgBmr;

        public VH(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            calReading = itemView.findViewById(R.id.cal_reading);
            avgBmr = itemView.findViewById(R.id.avg_bmr);
        }
    }
}

