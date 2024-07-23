package com.hotworx.ui.fragments.SessionFlow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.InternetHelper;
import com.hotworx.helpers.Utils;
import com.hotworx.requestEntity.BurntPojo;
import com.hotworx.requestEntity.CaloriesDetailPojo;
import com.hotworx.requestEntity.FinalSummaryPojo;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.room.RoomBuilder;
import com.hotworx.room.model.SessionEnt;
import com.hotworx.ui.adapters.WorkouDetailAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.fragments.HomeFragment;
import com.hotworx.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.hotworx.global.Constants.FACEBOOK_REQUEST_COUNTER;

public class WorkoutSummaryFragment extends BaseFragment {
    private Unbinder unbinder;
    private static int SHARE_IMAGE_REQUEST_CODE = 101;
    private WorkouDetailAdapter workouDetailAdapter;
    @BindView(R.id.rvDetail)
    RecyclerView rvDetail;
    private String parentActivityId = "0";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_summary, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        extrasWork();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.work));
    }

    @OnClick(R.id.btnFbShare)
    public void onClick() {
        shareImageOnFacebook();
    }

    private void extrasWork() {
        //Get active session from intent
        if (getArguments() != null) {
            String parentActivityId = getArguments().getString("parentActivityId");
            this.parentActivityId = parentActivityId;
            fetchDataFromRoom();
        }

    }

    private void shareImageOnFacebook() {
        Bitmap bitmap = Utils.getScreenshotOfActivity(myDockActivity, FACEBOOK_REQUEST_COUNTER);
        if (bitmap != null) {
            String filePath = MediaStore.Images.Media.insertImage(myDockActivity.getContentResolver(), bitmap, "", null);
            Uri imageUri = Uri.parse(filePath);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/jpg");

            startActivityForResult(Intent.createChooser(shareIntent, "Share via"), SHARE_IMAGE_REQUEST_CODE);

        } else {
            Utils.customToast(myDockActivity, "Failed to take screenshot of the screen");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SHARE_IMAGE_REQUEST_CODE) {
            //Call webservice
            apiCallForShareFacebookActivity(parentActivityId);
        }

    }


    private void apiCallForShareFacebookActivity(String parentActivityId) {
        getServiceHelper().enqueueCall(getWebService().shareFacebook(ApiHeaderSingleton.apiHeader(requireContext()),parentActivityId, "ANDROID"), WebServiceConstants.SHARE_FACEBOOK, true);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.SHARE_FACEBOOK:
                break;
        }
    }

    private void updateAdapter(CaloriesDetailPojo caloriesDetailPojo) {
        workouDetailAdapter = new WorkouDetailAdapter(myDockActivity, caloriesDetailPojo);
        rvDetail.setLayoutManager((new LinearLayoutManager(myDockActivity, LinearLayoutManager.VERTICAL, false)));
        rvDetail.setAdapter(workouDetailAdapter);
    }

    private void fetchDataFromRoom() {
        List<SessionEnt> sessionEntList = RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().getAllSessionByParentId(parentActivityId);
        CaloriesDetailPojo caloriesDetailPojo = new CaloriesDetailPojo();
        ArrayList<BurntPojo> sessionList = new ArrayList<>();
        for (int i = 0; i < sessionEntList.size(); i++) {
            if (sessionEntList.get(i).isIs_afterburn()) {
                // Getting Burnt calories by subtracting from burnt out session
                caloriesDetailPojo.setSixty_burnt(String.valueOf(Integer.parseInt(sessionEntList.get(i).getEnd_calories()) - Integer.parseInt(sessionEntList.get(i).getStart_calories())));
            } else {
                BurntPojo burntPojo = new BurntPojo(sessionEntList.get(i).getSession_type_start(),
                        String.valueOf(Integer.parseInt(sessionEntList.get(i).getEnd_calories()) - Integer.parseInt(sessionEntList.get(i).getStart_calories())),
                        sessionEntList.get(i).getSession_time());

                sessionList.add(burntPojo);
            }

        }
        caloriesDetailPojo.setForty_burnt(sessionList);
        updateAdapter(caloriesDetailPojo);
    }


    private void startHomeActivity() {
        myDockActivity.emptyBackStack();
        myDockActivity.replaceDockableFragment(new HomeFragment(), Constants.HomeFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        startHomeActivity();
    }
}
