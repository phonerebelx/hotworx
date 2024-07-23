package com.hotworx.ui.fragments.ActivityScreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.UIHelper;
import com.hotworx.helpers.Utils;
import com.hotworx.requestEntity.CaloriesDetailPojo;
import com.hotworx.requestEntity.FinalSummaryPojo;
import com.hotworx.requestEntity.ViewSummaryResponse;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.adapters.WorkouDetailAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.hotworx.global.Constants.FACEBOOK_REQUEST_COUNTER;

public class ActivityDetailFragment extends BaseFragment {
    Unbinder unbinder;
    private static int SHARE_IMAGE_REQUEST_CODE = 101;
    WorkouDetailAdapter workouDetailAdapter;
    @BindView(R.id.rvDetail)
    RecyclerView rvDetail;
    @BindView(R.id.btnFbShare)
    Button btnFbShare;
    String parentActivityId = "0";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle args = getArguments();
        if (args != null) {
            parentActivityId = args.getString("parent_activity_id");
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!parentActivityId.equals("0")){
            getServiceHelper().enqueueCall(getWebService().getFinalSummary(ApiHeaderSingleton.apiHeader(requireContext()),parentActivityId), WebServiceConstants.GET_FULL_SUMMARY,true);
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.detail));
    }

    @OnClick(R.id.btnFbShare)
    public void onClick(){
        shareImageOnFacebook();
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

        } else  {
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

    private void apiCallForShareFacebookActivity(String parentActivityId){
        getServiceHelper().enqueueCall(getWebService().shareFacebook(ApiHeaderSingleton.apiHeader(requireContext()),parentActivityId,"ANDROID"),WebServiceConstants.SHARE_FACEBOOK,true);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag){

            case WebServiceConstants.GET_FULL_SUMMARY:
                FinalSummaryPojo mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, FinalSummaryPojo.class);
                if(mContentPojo!=null && mContentPojo.getAllData().get(0).getCalorieDetails()!=null && mContentPojo.getAllData().get(0).getCalorieDetails().getForty_burnt()!=null)
                 updateAdapter(mContentPojo.getAllData().get(0).getCalorieDetails());
                else
                    UIHelper.showShortToastInCenter(myDockActivity,getString(R.string.internal_exception_messsage));
                break;
            case WebServiceConstants.SHARE_FACEBOOK:
                break;

        }
    }

    private void updateAdapter(CaloriesDetailPojo caloriesDetailPojo){
        workouDetailAdapter = new WorkouDetailAdapter(myDockActivity, caloriesDetailPojo);
        rvDetail.setLayoutManager((new LinearLayoutManager(myDockActivity, LinearLayoutManager.VERTICAL, false)));
        rvDetail.setAdapter(workouDetailAdapter);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        //unbinder.unbind();
    }
}
