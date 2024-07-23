package com.hotworx.ui.adapters;

import android.content.Context;
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
import com.hotworx.interfaces.OnFreeFormItemClick;
import com.hotworx.requestEntity.Branded;
import com.hotworx.requestEntity.GetFavoriteFoodsResponse;
import com.hotworx.retrofit.GsonFactory;
import java.util.List;

public class FreeFormAdapter extends RecyclerView.Adapter<FreeFormAdapter.VH> {

    private final DockActivity myDockActivity;
    private List<Branded> dayData;
    private List<GetFavoriteFoodsResponse.Food_list> favFoodList;
    private final Context context;
    private static final RequestOptions requestOptions = new RequestOptions();
    private final OnFreeFormItemClick mClickListener;


    public FreeFormAdapter(DockActivity myDockActivity, List<Branded> durationList, List<GetFavoriteFoodsResponse.Food_list> favFoodList, Context context, OnFreeFormItemClick mClickListener) {
        this.myDockActivity = myDockActivity;
        this.dayData = durationList;
        this.context = context;
        this.mClickListener = mClickListener;
        this.favFoodList = favFoodList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(myDockActivity).inflate(R.layout.item_freeform, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Branded item = dayData.get(position);

        for (int i=0; i < favFoodList.size(); i++) {
            if (item.getNix_item_id().equals(favFoodList.get(i).getProduct_id())) {
                holder.imageViewStar.setImageResource(R.drawable.ic_star_filled);
                holder.imageViewStar.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
            }
        }

        int value = (int) Math.round(item.getServing_qty());
        holder.name.setText(item.getFood_name());
        holder.quantity.setText(value + item.getServing_unit());
        holder.calories.setText(String.valueOf(item.getNf_calories()));
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(item.getPhoto().getThumb())
                .into(holder.imageView);

        holder.itemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = GsonFactory.getConfiguredGson().toJson(item);
                mClickListener.onItemClick(json);
            }
        });
        holder.imageViewStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageViewStar.setImageResource(R.drawable.ic_star_filled);
                holder.imageViewStar.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
                mClickListener.onItemClick(myDockActivity.prefHelper.getUserId(), item.getNix_item_id(),true);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (dayData != null)
            return dayData.size();
        else
            return 0;
    }

    public void addAll(List<Branded> durationList) {
        this.dayData = durationList;
    }

    class VH extends RecyclerView.ViewHolder {
        TextView name;
        TextView quantity;
        TextView calories;
        ImageView imageView;
        ImageView imageViewStar;
        LinearLayout itemLL;

        public VH(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_freeform);
            quantity = itemView.findViewById(R.id.quantity_freeform);
            calories = itemView.findViewById(R.id.calories_freeform);
            imageView = itemView.findViewById(R.id.iv_freeform);
            imageViewStar = itemView.findViewById(R.id.iv_star);
            itemLL = itemView.findViewById(R.id.main_linear);
        }
    }
}
