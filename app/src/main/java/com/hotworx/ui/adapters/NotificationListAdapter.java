package com.hotworx.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.hotworx.R;
import com.hotworx.global.Constants;
import com.hotworx.helpers.UIHelper;
import com.hotworx.interfaces.OnClickItemListener;
import com.hotworx.models.NotificationHistory.Data;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.florent37.shapeofview.ShapeOfView;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.VH> {
    private Context context;
    private List<Data> notificationResponseEntData;
    private LayoutInflater inflater;
    private OnClickItemListener onClickItemListener;

    private String hasAccessToHashId = "";
    private String notificationId = "";

    private int highlightedPosition = -1;



    public  NotificationListAdapter(Context context, String hasAccessToHashId, List<Data> notificationResponseEntData, OnClickItemListener onClickItemListener) {
        this.context = context;

        this.notificationId = hasAccessToHashId;
        this.hasAccessToHashId = hasAccessToHashId;
        this.notificationResponseEntData = notificationResponseEntData;
        this.onClickItemListener = onClickItemListener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notification, parent, false);
        return new VH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NotificationListAdapter.VH holder, int position) {
        Data notificationData = notificationResponseEntData.get(position);

        holder.tvTitle.setText(notificationData.getTitle());
        holder.tvDetail.setText(notificationData.getBody());
        holder.tvtime.setText(notificationData.getSent_at());


        if (!hasAccessToHashId.equals("")) {
            hasAccessToHashId = "";
//            onClickItemListener.onItemClick(notificationData, "FIRST_TIME_CALLING");
        }

        if (notificationData.getImage_url() != null) {
            holder.imgRect.setVisibility(View.VISIBLE);
            holder.tvDetail.setVisibility(View.GONE);
            Glide.with(context)
                    .load(notificationData.getImage_url())
                    .transform(new RoundedCorners(10))
                    .into(holder.ivBanner);
        } else {
            holder.imgRect.setVisibility(View.GONE);
            holder.tvDetail.setVisibility(View.VISIBLE);
        }

        holder.ivReadImg.setVisibility(View.GONE);
        holder.mainView.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_rectangle_notification));

        if (notificationData.getRead_status()) {
            holder.mainView.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_rectangle_notification_white));
            holder.ivReadImg.setVisibility(View.VISIBLE);
           }

        holder.mainView.setOnClickListener(v -> {
            onClickItemListener.onItemClick(notificationData, "Come_From_Click_Notification_Adapter");
        });

        holder.ivBanner.setOnClickListener(v -> {
            onClickItemListener.onItemClick(notificationData, "COME_FROM_IMAGE_CLICK");
        });


        if (notificationData.getId().equals(notificationId)) {
            notificationId = "";
            new Handler().postDelayed(() -> {
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(300);
                anim.setRepeatMode(Animation.REVERSE);
                holder.mainView.startAnimation(anim);
                anim.setRepeatCount(4);
                new Handler().postDelayed(() -> holder.mainView.clearAnimation(), 3000);
            }, 1000);

         }
    }

    @Override
    public int getItemCount() {
        return notificationResponseEntData.size();
    }

    public void markNotificationAsRead(String notificationId) {
        int position = getPositionById(notificationId);
        if (position != -1) {
            Data notificationData = notificationResponseEntData.get(position);
            notificationData.setRead_status(true);
            notificationResponseEntData.set(position, notificationData);
            notifyItemChanged(position);
            Log.d("NotificationAdapter", "Notification marked as read: " + notificationId);
        } else {
            Log.d("NotificationAdapter", "Notification not found: " + notificationId);
        }
    }

    public int getPositionById(String notificationId) {
        for (int i = 0; i < notificationResponseEntData.size(); i++) {
            if (notificationResponseEntData.get(i).getId().equals(notificationId)) {
                return i;
            }
        }
        return -1;
    }

    class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView tvTitle;
        @BindView(R.id.time)
        TextView tvtime;
        @BindView(R.id.detail)
        TextView tvDetail;
        @BindView(R.id.readImg)
        ImageView ivReadImg;
        @BindView(R.id.banner)
        ImageView ivBanner;
        @BindView(R.id.imgRect)
        ShapeOfView imgRect;
        @BindView(R.id.mainView)
        CardView mainView;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



}

