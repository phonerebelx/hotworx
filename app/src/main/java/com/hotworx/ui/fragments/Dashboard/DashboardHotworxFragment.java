package com.hotworx.ui.fragments.Dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.Utils;
import com.hotworx.interfaces.OnViewHolderClick;
import com.hotworx.requestEntity.FranchiseListResponse;
import com.hotworx.requestEntity.FranchiseListResponseData;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.adapters.FranchiseListAdapter;
import com.hotworx.ui.adapters.abstarct.RecyclerViewListAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DashboardHotworxFragment extends BaseFragment implements OnViewHolderClick {

    Unbinder unbinder;
    @BindView(R.id.recyclerViewFranchiseList)
    RecyclerView rvFranchiseList;
    private RecyclerViewListAdapter<FranchiseListResponseData> adapter;
    private ArrayList<FranchiseListResponseData> arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboardhotworx,container,false);
        unbinder = ButterKnife.bind(this,view);
        initAdapter();
        callApi();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.dashboard));
    }

    private void callApi(){
        getServiceHelper().enqueueCall(getWebService().getFranchiseList(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.FRANCHISE_LIST, true);
    }

    private void initAdapter() {
        rvFranchiseList.setLayoutManager(new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new FranchiseListAdapter(getDockActivity(),this);
        rvFranchiseList.setAdapter(adapter);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.FRANCHISE_LIST:
                FranchiseListResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, FranchiseListResponse.class);
                if (mContentPojo != null) {
                    arrayList = mContentPojo.getData();
                   adapter.addAll(mContentPojo.getData());
                } else
                    Utils.customToast(myDockActivity, getString(R.string.error_failure));
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
       // unbinder.unbind();
    }

    @Override
    public void onItemClick(View view, int position) {
        if(arrayList!=null && arrayList.size()>0){
            FranchiseListResponseData franchiseListResponseData = arrayList.get(position);
            FranchiseSummaryFragment franchiseSummaryFragment = FranchiseSummaryFragment.newInstance(franchiseListResponseData.getLocation_id());
            myDockActivity.replaceDockableFragment(franchiseSummaryFragment);
        }
    }
}
