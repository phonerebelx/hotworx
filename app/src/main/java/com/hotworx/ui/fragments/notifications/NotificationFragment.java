package com.hotworx.ui.fragments.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.Utils;
import com.hotworx.interfaces.OnClickItemListener;
import com.hotworx.models.NotificationHistory.Data;
import com.hotworx.models.NotificationHistory.NotificationHistoryModel;
import com.hotworx.models.NotificationHistory.NotificationRead.NotificationReadModel;
import com.hotworx.requestEntity.NotificationResponseEnt;
import com.hotworx.requestEntity.VideoEnt;
import com.hotworx.requestEntity.VideoResponseEnt;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.adapters.NotificationListAdapter;
import com.hotworx.ui.adapters.VideoCategoryAdapter;
import com.hotworx.ui.adapters.VideoListAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.fragments.HomeFragment;
import com.hotworx.ui.fragments.VPT.VideoPlayerActivty;
import com.hotworx.ui.fragments.notifications.ReadNotification.LargeImageView.LargeImageViewDialogFragment;
import com.hotworx.ui.fragments.notifications.ReadNotification.NotificationReadFragment;
import com.hotworx.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NotificationFragment extends BaseFragment implements OnClickItemListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerViewNotification)
    RecyclerView recyclerViewNotification;
    @BindView(R.id.tvNotifications)
    TextView tvNotifications;
    private NotificationListAdapter notificationListAdapter;

    NotificationHistoryModel notificationHistoryModel;
    NotificationReadModel notificationReadModel;
    NotificationReadModel attachmentReadModel;
    String hasAccessToHashId = "";
    String highlightedNotificationId = "";

    public static NotificationFragment newInstance(String navigateTo,String hashId,String notification_type, String custom_message, String booking_date, String title, String objid, String calender_title, int duration) {
        Bundle bundle = new Bundle();
        bundle.putString("navigateTo", navigateTo);
        bundle.putString("hashId", hashId);
        bundle.putString(Constants.NOTIFICATION_TYPE, notification_type);
        bundle.putString(Constants.CUSTOM_MESSAGE, custom_message);
        bundle.putString(Constants.BOOKING_DATE, booking_date);
        bundle.putString(Constants.CUSTOM_TITLE, title);
        bundle.putString(Constants.OBJ_ID, objid);
        bundle.putString(Constants.CALENDER_TITLE, calender_title);
        bundle.putInt(Constants.DURATION, duration);
        NotificationFragment notificationFragment = new NotificationFragment();
        notificationFragment.setArguments(bundle);
        return notificationFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments()!= null && getArguments().get("hashId") != null){
            hasAccessToHashId = getArguments().get("hashId").toString();
        }

        getNotifications();
    }

    private void getNotifications() {
        getServiceHelper().enqueueCall(getWebService().getNotificationHistory(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_NOTIFICATION_HISTORY, true);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.hideNotificationBtn();
        titleBar.setSubHeading(getString(R.string.notifications));
    }
    @Override
    public void ResponseFailure(String message, String tag) {
        super.ResponseFailure(message, tag);
        tvNotifications.setText("No Notification Found");

    }
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_NOTIFICATION_HISTORY:

                notificationHistoryModel = GsonFactory.getConfiguredGson().fromJson(result, NotificationHistoryModel.class);

                if (notificationHistoryModel.getData() != null && !notificationHistoryModel.getData().isEmpty()) {
                    recyclerViewNotification.setLayoutManager(new LinearLayoutManager(myDockActivity));
                    notificationListAdapter = new NotificationListAdapter(myDockActivity, hasAccessToHashId,notificationHistoryModel.getData(),this);
                    recyclerViewNotification.setAdapter(notificationListAdapter);
                    tvNotifications.setVisibility(View.GONE);

                    if (!hasAccessToHashId.isEmpty()) {
                        scrollToNotification(hasAccessToHashId);
                    }

                    hasAccessToHashId = "";
                } else {
                    tvNotifications.setVisibility(View.VISIBLE);
                    tvNotifications.setText("No Notification Found");
                }

                break;
            case WebServiceConstants.MARK_NOTIFICATION_READ:
                notificationReadModel = GsonFactory.getConfiguredGson().fromJson(result, NotificationReadModel.class);
                if (notificationListAdapter != null) {

                    notificationListAdapter.markNotificationAsRead(notificationReadModel.getData().getId().toString());
                }
               break;
            case WebServiceConstants.MARK_ATTACHMENT_READ:
                attachmentReadModel = GsonFactory.getConfiguredGson().fromJson(result, NotificationReadModel.class);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void scrollToNotification(String notificationId) {
        recyclerViewNotification.post(() -> {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerViewNotification.getLayoutManager();
            int position = notificationListAdapter.getPositionById(notificationId);
            if (position != -1) {
                layoutManager.scrollToPositionWithOffset(position, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        myDockActivity.emptyBackStack();
        myDockActivity.replaceDockableFragment(new HomeFragment(), Constants.HomeFragment);
    }

    private void initNotificationDialog(Data data){
        NotificationReadFragment notificationReadFragment = new NotificationReadFragment();
        notificationReadFragment.notificationData = data;
        notificationReadFragment.show(getParentFragmentManager(), "NotificationReadFragment");
    }

    private void initImageDialog(Data data){
        LargeImageViewDialogFragment largeImageViewDialogFragment = new LargeImageViewDialogFragment();
        largeImageViewDialogFragment.notificationData = data;
        largeImageViewDialogFragment.show(
                getParentFragmentManager(),
                largeImageViewDialogFragment.getTag()
        );
    }

    @Override
    public <T> void onItemClick(T data, @NonNull String type) {

        switch (type) {
            case "Come_From_Click_Notification_Adapter", "FIRST_TIME_CALLING" -> {
                Data notificationData = (Data) data;
                initNotificationDialog(notificationData);
                if (notificationData.getId() != null && !notificationData.getRead_status()) {
                    serviceHelper.enqueueCall(
                            webService.getNotificationAsRead(
                                    ApiHeaderSingleton.apiHeader(requireContext()),
                                    notificationData.getId()
                            ),
                            WebServiceConstants.MARK_NOTIFICATION_READ,
                            false
                    );
                }
            }

            case "COME_FROM_ATTACHMENT_CLICK_DOWNLOAD" -> {
                String getIdForAttachment = (String) data;
                if (getIdForAttachment != null && !getIdForAttachment.equals("")){

                    serviceHelper.enqueueCall(
                            webService.getNotificationAttachmentAsRead(
                                    ApiHeaderSingleton.apiHeader(requireContext()),
                                    getIdForAttachment
                            ),
                            WebServiceConstants.MARK_ATTACHMENT_READ,
                            false
                    );
                }
            }

            case "COME_FROM_IMAGE_CLICK" -> {
                Data notificationData = (Data) data;
//                initImageDialog(notificationData);
            }

        }
    }




}
