package com.hotworx.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hotworx.R;
import com.hotworx.requestEntity.VideoEnt;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VH> {
    private Context context;
    private ArrayList<VideoEnt> videoEnts;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public VideoListAdapter(Context context, ArrayList<VideoEnt> videoEnts,ItemClickListener itemClickListener ) {
        this.context = context;
        this.videoEnts = videoEnts;
        inflater = LayoutInflater.from(context);
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_video, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListAdapter.VH holder, @SuppressLint("RecyclerView") int position) {
        holder.txtVideoPosition.setText(String.valueOf(position + 1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener!=null)itemClickListener.onVideoClick(videoEnts.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoEnts.size();
    }

    class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.txtVideoPosition)
        TextView txtVideoPosition;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemClickListener {
        void onVideoClick(VideoEnt videoEnt);
    }
}
