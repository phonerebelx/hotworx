package com.hotworx.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hotworx.R;
import com.hotworx.activities.DockActivity;
import com.hotworx.interfaces.OnItemClickInterface;
import com.hotworx.requestEntity.Branded;
import com.hotworx.requestEntity.GetFavoriteFoodsResponse;
import com.hotworx.retrofit.GsonFactory;

import java.util.List;

public class FreeFormFavoriteFoodAdapter extends RecyclerView.Adapter<FreeFormFavoriteFoodAdapter.VH> {

    private final DockActivity myDockActivity;
    private List<GetFavoriteFoodsResponse.Food_list> dayData;
    private final Context context;
    private static final RequestOptions requestOptions = new RequestOptions();
    private final OnItemClickInterface mClickListener;

    public FreeFormFavoriteFoodAdapter(DockActivity myDockActivity, List<GetFavoriteFoodsResponse.Food_list> durationList, Context context, OnItemClickInterface mClickListener){
        this.myDockActivity = myDockActivity;
        this.dayData = durationList;
        this.context = context;
        this.mClickListener = mClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(myDockActivity).inflate(R.layout.item_freeform_favoritefoods, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        GetFavoriteFoodsResponse.Food_list item = dayData.get(position);
        Branded branded_item = new Branded();

        holder.name.setText(item.getProduct_name());
        holder.calories.setText(String.valueOf(item.getCalories()));
//        Glide.with(context)
//                .setDefaultRequestOptions(requestOptions)
//                .load(item.getPhoto().getThumb())
//                .into(holder.imageView);
//
        holder.itemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branded_item.setFood_name(item.getProduct_name());
                branded_item.setNf_calories(item.getCalories());
                branded_item.setServing_qty(1);
                branded_item.setNix_item_id(item.getProduct_id());
                String json = GsonFactory.getConfiguredGson().toJson(branded_item);
                mClickListener.onItemClick(json);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dayData.size();
    }

    public void addAll(List<GetFavoriteFoodsResponse.Food_list> durationList) {
        this.dayData = durationList;
    }

    class VH extends RecyclerView.ViewHolder {
        TextView name;
        TextView calories;
        ConstraintLayout itemLL;

        public VH(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_freeform_fav_food);
            calories = itemView.findViewById(R.id.calories_freeform_fav_food);
            itemLL = itemView.findViewById(R.id.cl_1);
        }
    }

}
