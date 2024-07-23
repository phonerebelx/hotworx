package com.hotworx.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hotworx.R;
import com.hotworx.requestEntity.VideoDetailEnt;
import com.hotworx.requestEntity.VideoResponseEnt;
import com.hotworx.requestEntity.WorkOutPOJO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoCategoryAdapter extends RecyclerView.Adapter<VideoCategoryAdapter.VH> {
    private Context context;
    private ArrayList<VideoDetailEnt> videoResponseEnts;
    private LayoutInflater inflater;
    private VideoListAdapter videoListAdapter;
    private VideoListAdapter.ItemClickListener itemClickListener;


    public VideoCategoryAdapter(Context context, ArrayList<VideoDetailEnt> videoResponseEnts,VideoListAdapter.ItemClickListener itemClickListener) {
        this.context = context;
        this.videoResponseEnts = videoResponseEnts;
        inflater = LayoutInflater.from(context);
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_category_video, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoCategoryAdapter.VH holder, int position) {
        VideoDetailEnt videoDetailEnt = videoResponseEnts.get(position);
        holder.txtCategoryName.setText(videoDetailEnt.getCategory_name());
        holder.recyclerViewVideo.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        videoListAdapter = new VideoListAdapter(context,videoDetailEnt.getVideos(),itemClickListener);
        holder.recyclerViewVideo.setAdapter(videoListAdapter);
    }

    @Override
    public int getItemCount() {
        return videoResponseEnts.size();
    }


    class VH extends RecyclerView.ViewHolder {
        @BindView(R.id.txtCategoryName)
        TextView txtCategoryName;
        @BindView(R.id.recyclerViewVideo)
        RecyclerView recyclerViewVideo;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
