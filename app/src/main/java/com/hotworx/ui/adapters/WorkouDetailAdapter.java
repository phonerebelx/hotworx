package com.hotworx.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bikomobile.donutprogress.DonutProgress;
import com.hotworx.R;
import com.hotworx.activities.DockActivity;
import com.hotworx.global.Constants;
import com.hotworx.requestEntity.CaloriesDetailPojo;
import com.hotworx.room.RoomBuilder;

import static com.hotworx.global.Constants.WORKOUT_AFTER_BURN_NAME;


public class WorkouDetailAdapter extends RecyclerView.Adapter<WorkouDetailAdapter.VH> {

    private final DockActivity myDockActivity;
    private CaloriesDetailPojo durationList;

    public WorkouDetailAdapter(DockActivity myDockActivity, CaloriesDetailPojo durationList) {
        this.myDockActivity = myDockActivity;
        this.durationList = durationList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(myDockActivity)
                .inflate(R.layout.item_workoutdetail, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (position < durationList.getForty_burnt().size()) {
            holder.tvType.setText(durationList.getForty_burnt().get(position).getActivity_name());
            holder.tvDuration.setText(durationList.getForty_burnt().get(position).getActivity_time() + ":00");
            holder.donut_progress.setProgressWithAnimation(100, 20);
            holder.tvCalBurnedCount.setText(durationList.getForty_burnt().get(position).getCalorie_burnt());
        } else  {
            //One hour after burn session
            holder.tvType.setText(WORKOUT_AFTER_BURN_NAME);
//            if(RoomBuilder.getHotWorxDatabase(myDockActivity).getWorkoutTypeDao().getWorkoutType()!=null)
//            holder.tvDuration.setText(RoomBuilder.getHotWorxDatabase(myDockActivity).getWorkoutTypeDao().getWorkoutType().getSixty_min_time() + ":00");
            holder.tvDuration.setText(Constants.WORKOUT_AFTER_BURN_DURATION + ":00");
            holder.donut_progress.setProgressWithAnimation(100, 20);
            holder.tvCalBurnedCount.setText(durationList.getSixty_burnt());
        }
    }

    @Override
    public int getItemCount() {
        return durationList.getForty_burnt().size() + 1;
    }

    public void addAll(CaloriesDetailPojo durationList) {
        this.durationList = durationList;
    }

    class VH extends RecyclerView.ViewHolder {
        TextView tvType;
        TextView tvDuration;
        TextView tvCalBurnedCount;
        DonutProgress donut_progress;

        public VH(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvCalBurnedCount = itemView.findViewById(R.id.tvCalBurnedCount);
            donut_progress = itemView.findViewById(R.id.donut_progress);
        }
    }
}
