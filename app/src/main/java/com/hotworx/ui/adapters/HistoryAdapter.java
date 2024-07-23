package com.hotworx.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hotworx.R;
import com.hotworx.global.Constants;
import com.hotworx.helpers.UIHelper;
import com.hotworx.room.model.SessionEnt;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<SessionEnt> arrayList;
    private OnItemClickListener onItemClickListener;

    public HistoryAdapter(Context context, List<SessionEnt> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_session_history, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SessionEnt item = arrayList.get(position);
        holder.tvSessionType.setText(item.getWorkout_type());
        holder.tvDate.setText(UIHelper.getFormattedDate(item.getStart_date(), Constants.DATE_FORMAT, Constants.DATE_TIME_FORMAT_TWO));
        holder.tvCalories.setText(item.getStart_calories() + " - " + item.getEnd_calories() + " Calories");
        holder.tvStatus.setText(item.getIs_cancelled().toLowerCase().equals("yes") ? "Cancelled" : "Completed");
        holder.tvStatus.setBackgroundColor(item.getIs_cancelled().toLowerCase().equals("yes") ? context.getResources().getColor(R.color.colorRed) : context.getResources().getColor(R.color.shaded_blue));
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null)
                    onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSessionType)
        TextView tvSessionType;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvCalories)
        TextView tvCalories;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.ivDelete)
        ImageView ivDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void remove(SessionEnt r) {
        int position = arrayList.indexOf(r);
        if (position > -1) {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(SessionEnt sessionEnt);
    }
}
