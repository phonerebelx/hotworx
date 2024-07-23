package com.hotworx.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hotworx.R;
import com.hotworx.activities.DockActivity;
import com.hotworx.interfaces.BasketClickListener;
import com.hotworx.interfaces.OnItemClickInterface;
import com.hotworx.requestEntity.Branded;
import com.hotworx.retrofit.GsonFactory;

import java.util.List;

public class BasketItemAdapter extends RecyclerView.Adapter<BasketItemAdapter.VH> {

    private final DockActivity myDockActivity;
    private List<Branded> dayData;
    private final Context context;
    private static final RequestOptions requestOptions = new RequestOptions();
    private final BasketClickListener mClickListener;
    private final Boolean isFasting;

    public BasketItemAdapter(DockActivity myDockActivity, List<Branded> durationList, Context context, BasketClickListener mClickListener, Boolean mIsFasting) {
        this.myDockActivity = myDockActivity;
        this.dayData = durationList;
        this.context = context;
        this.mClickListener = mClickListener;
        this.isFasting = mIsFasting;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(myDockActivity)
                .inflate(R.layout.item_basket, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, @SuppressLint("RecyclerView") int position) {
        Branded item = dayData.get(position);

        //String[] separated = String.valueOf(item.getServing_qty()).split(".0");
        holder.name.setText(item.getFood_name());
        if (item.getServing_unit() != null) {
            holder.desc.setText(item.getServing_qty() + " " + item.getServing_unit());
        } else {
            holder.desc.setText(String.valueOf(item.getServing_qty()));
        }
        holder.quantity.setText(String.valueOf(item.getServing_qty()));

        if (!isFasting) {
            holder.calories.setText(String.valueOf(item.getNf_calories() * item.getServing_qty()));
            try {
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions)
                        .load(item.getPhoto().getThumb())
                        .into(holder.imageView);
            } catch (Exception e) {
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions)
                        .load("")
                        .into(holder.imageView);
            }

        }



        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    float qty = Float.parseFloat(editable.toString());
                    item.setServing_qty(qty);
                    holder.calories.setText(String.valueOf(item.getNf_calories() * qty));
                    mClickListener.onUpdateQuantity(qty, position);
                } catch (NumberFormatException ex) {
                    // Not a float
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayData.size();
    }

    public void addAll(List<Branded> durationList) {
        this.dayData = durationList;
    }

    class VH extends RecyclerView.ViewHolder {
        TextView name;
        EditText quantity;
        TextView calories;
        TextView desc;
        ImageView imageView;

        public VH(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.food_name_basket);
            quantity = itemView.findViewById(R.id.tv_qty_basket);
            calories = itemView.findViewById(R.id.cal_basket);
            desc = itemView.findViewById(R.id.tv_desc_basket);
            imageView = itemView.findViewById(R.id.iv_basket);
        }
    }
}

