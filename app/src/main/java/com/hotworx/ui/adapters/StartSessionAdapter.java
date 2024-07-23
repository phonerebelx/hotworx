package com.hotworx.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hotworx.R;
import com.hotworx.requestEntity.WorkOutPOJO;

import java.util.ArrayList;
public class StartSessionAdapter extends RecyclerView.Adapter<StartSessionAdapter.VH> {
    private final Context activity;
    private final ArrayList<WorkOutPOJO> sessionTypesList;
    private final String time;
    LayoutInflater inflater;
    private RadioButton prbDate;
    SessionInterface sessionInterface;

    public StartSessionAdapter(Context context, ArrayList<WorkOutPOJO> sessionTypesList, String time, SessionInterface sessionInterface) {
        this.time = time;
        this.activity = context;
        inflater = LayoutInflater.from(activity);
        this.sessionTypesList = sessionTypesList;
        this.sessionInterface = sessionInterface;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_session, parent, false);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VH holder, final int position) {

        holder.tvDate.setText(time);

        holder.rbDate.setText(sessionTypesList.get(position).getDuration()+" Min");
        holder.tvName.setText(sessionTypesList.get(position).getType());
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(holder.rbDate, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return sessionTypesList.size();
    }

    class VH extends RecyclerView.ViewHolder {

        LinearLayout llMain;
        RadioButton rbDate;
        TextView tvName;
        TextView tvDate;

        public VH(View itemView) {
            super(itemView);
            llMain = itemView.findViewById(R.id.llMain);
            rbDate = itemView.findViewById(R.id.rbDate);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);


        }
    }

    private void setSelected(RadioButton rbDate, int position) {
        rbDate.performClick();
        if (prbDate != null) {
            prbDate.setSelected(false);
        }
        prbDate = rbDate;
        sessionInterface.onRadioButton_Click(position);

    }

    private void setSelected(RadioButton rbDate, String sessionTypesList, String duration) {
        rbDate.performClick();
        sessionInterface.onRadioButton_Click(sessionTypesList, duration);
        if (prbDate != null) {
            prbDate.setChecked(false);
        }
        prbDate = rbDate;
    }

    public interface SessionInterface {
        void onRadioButton_Click(String sessionTypesList, String duration);
        void onRadioButton_Click(int position);
    }
}