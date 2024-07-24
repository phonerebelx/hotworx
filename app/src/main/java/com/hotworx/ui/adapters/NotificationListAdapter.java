package com.hotworx.ui.adapters;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hotworx.R;
import com.hotworx.global.Constants;
import com.hotworx.helpers.UIHelper;
import com.hotworx.interfaces.OnClickItemListener;
import com.hotworx.models.NotificationHistory.Data;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.VH> {
    private Context context;
    private List<Data> notificationResponseEntData;
    private LayoutInflater inflater;

    private long downloadID;
    private long enq;
    private DownloadManager downloadManager;
    private OnClickItemListener onClickItemListener;
    private String readNotificationId = "";
    private String hasAccessToHashId = "";

    private String getIdForAttachment = "";

    private Animator currentAnimator;
    private int shortAnimationDuration;


    public NotificationListAdapter(Context context, String hasAccessToHashId, List<Data> notificationResponseEntData, OnClickItemListener onClickItemListener) {
        this.context = context;
        this.hasAccessToHashId = hasAccessToHashId;
        this.notificationResponseEntData = notificationResponseEntData;
        this.onClickItemListener = onClickItemListener;
        inflater = LayoutInflater.from(context);

        // Register BroadcastReceiver to track the download status
        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
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
        holder.tvTitle.setText(notificationResponseEntData.get(position).getTitle());
        holder.tvDetail.setText(notificationResponseEntData.get(position).getBody());
        holder.tvtime.setText(UIHelper.getFormattedDate(notificationResponseEntData.get(position).getSent_at(), Constants.DATE_FORMAT_2, Constants.DATE_TIME_FORMAT_TWO));

        if(notificationResponseEntData.get(position).getBanner() != ""){
            holder.ivBanner.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(notificationResponseEntData.get(position).getBanner())
                    .placeholder(R.drawable.ic_user)
                    .into(holder.ivBanner);
        }else {
            holder.ivBanner.setVisibility(View.GONE);
        }

        if (!hasAccessToHashId.equals("")) {
            hasAccessToHashId = "";
            onClickItemListener.onItemClick(notificationResponseEntData.get(position), "FIRST_TIME_CALLING");
        }
//
//        if (notificationResponseEntData.get(position).getAttachment_url() == null) {
//            holder.llAttachment.setVisibility(View.GONE);
//        }

        if (notificationResponseEntData.get(position).getImage_url() != null) {
            Glide.with(context)
                    .load(notificationResponseEntData.get(position).getImage_url())
                    .into(holder.ivNotification);
        }

        if (notificationResponseEntData.get(position).getId().equals(readNotificationId) || notificationResponseEntData.get(position).getRead_status()) {
            notificationResponseEntData.get(position).setRead_status(true);
            holder.ivReadImg.setVisibility(View.VISIBLE);
            holder.mainView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.notificationGrey));
        }

        holder.ivNotification.setOnClickListener(v -> {
            onClickItemListener.onItemClick(notificationResponseEntData.get(position),"COME_FROM_IMAGE_CLICK");
        });

//        holder.llAttachment.setOnClickListener(v -> {
//            getIdForAttachment = notificationResponseEntData.get(position).getId();
//            if (isImageFile(notificationResponseEntData.get(position).getAttachment_url())) {
//                downloadFile(notificationResponseEntData.get(position).getAttachment_url(), "image.jpg");
//
//            } else if (isPdfFile(notificationResponseEntData.get(position).getAttachment_url())) {
//                downloadFile(notificationResponseEntData.get(position).getAttachment_url(), "attachment.pdf");
//            }
//        });

        holder.mainView.setOnClickListener(view -> onClickItemListener.onItemClick(notificationResponseEntData.get(position), "Come_From_Click_Notification_Adapter"));
    }

    @Override
    public int getItemCount() {
        return notificationResponseEntData.size();
    }

    public void markNotificationAsRead(String notificationId) {
        int position = getPositionById(notificationId);
        if (position != -1) {
            readNotificationId = notificationId;
            notifyItemChanged(position); // Notify only the changed item
        }
    }

    private int getPositionById(String notificationId) {
        for (int i = 0; i < notificationResponseEntData.size(); i++) {
            if (notificationResponseEntData.get(i).getId().equals(notificationId)) {
                return i;
            }
        }
        return -1; // Item not found
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
        @BindView(R.id.ivNotification)
        ImageView ivNotification;

        @BindView(R.id.mainView)
        CardView mainView;

        public VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void downloadFile(String url, String fileName) {
        // Check for runtime permissions if necessary
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return;
        }

        String mimeType = getMimeType(url);
        if (mimeType == null) {
            Toast.makeText(context, "Unsupported file type", Toast.LENGTH_SHORT).show();
            return;
        }

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Downloading " + fileName);
        request.setDescription("Downloading attachment...");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType(mimeType);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        enq = downloadManager.enqueue(request);
        Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show();
    }

    private String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(enq);
                downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Cursor c = downloadManager.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                        String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                        openDownloadedFile(context, uriString);
                    }
                }
                onClickItemListener.onItemClick(getIdForAttachment,"COME_FROM_ATTACHMENT_CLICK_DOWNLOAD");
                c.close();
            }
        }
    };

    private void openDownloadedFile(Context context, String uriString) {
        if (uriString == null) {
            Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(Uri.parse(uriString).getPath());
        Uri fileUri = Uri.fromFile(file);

        Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
        openFileIntent.setDataAndType(fileUri, getMimeType(uriString));
        openFileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            context.startActivity(openFileIntent);
        } catch (Exception e) {
            Toast.makeText(context, "No application found to open this file", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isImageFile(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        return mimeType != null && mimeType.startsWith("image");
    }

    public boolean isPdfFile(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        return "pdf".equalsIgnoreCase(extension);
    }


}
