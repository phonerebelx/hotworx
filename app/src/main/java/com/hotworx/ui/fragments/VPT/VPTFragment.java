package com.hotworx.ui.fragments.VPT;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Operation;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.UIHelper;
import com.hotworx.helpers.Utils;
import com.hotworx.requestEntity.VideoEnt;
import com.hotworx.requestEntity.VideoResponseEnt;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.adapters.VideoCategoryAdapter;
import com.hotworx.ui.adapters.VideoListAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VPTFragment extends BaseFragment implements VideoListAdapter.ItemClickListener {
    Unbinder unbinder;
    @BindView(R.id.recyclerViewVPT)
    RecyclerView recyclerViewVpt;
    private VideoCategoryAdapter videoCategoryAdapter;
    private Boolean isHotworxVideos = false;

    public static VPTFragment newInstance(boolean isHotworxVideos) {
        VPTFragment myFragment = new VPTFragment();

        Bundle args = new Bundle();
        args.putBoolean("is_hotworx_videos", isHotworxVideos);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vpt,container,false);
        unbinder = ButterKnife.bind(this,view);
        isHotworxVideos = getArguments().getBoolean("is_hotworx_videos", false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getVptVideos();
    }

    private void getVptVideos(){
        String nodeType = isHotworxVideos ? "hah" : "vpt";
        getServiceHelper().enqueueCall(getWebService().getVptVideo(ApiHeaderSingleton.apiHeader(requireContext()), nodeType), WebServiceConstants.GET_VPT_VIDEO,true);
    }

    private void addToVideoHistory(String videoId) {
        getServiceHelper().enqueueCall(getWebService().addToVideoHistory(
                        ApiHeaderSingleton.apiHeader(requireContext()), videoId, "android"),
                WebServiceConstants.ADD_VIDEO_HISTORY, false);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(isHotworxVideos ? getString(R.string.hotworx_at_home) : getString(R.string.vpt));
    }

    public void ResponseSuccess(String result, String Tag) {
        switch (Tag){
            case WebServiceConstants.GET_VPT_VIDEO:
                VideoResponseEnt mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, VideoResponseEnt.class);
                if(mContentPojo != null && mContentPojo.getAllData().size() > 0 && mContentPojo.getAllData().get(0).getVideos()!=null && mContentPojo.getAllData().get(0).getVideos().size()>0){
                    recyclerViewVpt.setLayoutManager(new LinearLayoutManager(myDockActivity));
                    videoCategoryAdapter = new VideoCategoryAdapter(myDockActivity,mContentPojo.getAllData().get(0).getVideos(),this);
                    recyclerViewVpt.setAdapter(videoCategoryAdapter);
                }else{
                    // No Videos Found;
                }
                break;
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        // unbinder.unbind();
    }

    @Override
    public void onVideoClick(VideoEnt videoEnt) {
        Intent intent = new Intent(myDockActivity, VideoPlayerActivty.class);
        if(videoEnt.getAndroid_url()!=null && !videoEnt.getAndroid_url().equals("")){
            intent.putExtra("url",videoEnt.getVideo_link());
            startActivity(intent);

            // Log to watch video history
            addToVideoHistory(videoEnt.getVideo_id());
        }else{
            Utils.customToast(myDockActivity,"No Video Available");
        }

    }
}
