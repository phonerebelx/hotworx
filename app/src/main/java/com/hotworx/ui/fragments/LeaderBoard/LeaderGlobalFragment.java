package com.hotworx.ui.fragments.LeaderBoard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;
import com.hotworx.requestEntity.LeaderBoardPOJO;
import com.hotworx.ui.adapters.LeaderGlobalAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LeaderGlobalFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.list_activity_global)
    ListView listView;
    List<LeaderBoardPOJO> mCaloriesObject;


    public static LeaderGlobalFragment newInstance(boolean checked) {
        LeaderGlobalFragment fragment = new LeaderGlobalFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isGlobalChecked", checked);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment_global, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LeaderBoardFragment leaderBoardFragment = ((LeaderBoardFragment) LeaderGlobalFragment.this.getParentFragment());
        if(leaderBoardFragment!=null){
            if (getArguments().getBoolean("isGlobalChecked")) {
                mCaloriesObject = leaderBoardFragment.getGlobal_list();
            } else {
                mCaloriesObject = leaderBoardFragment.getLocal_list();

            }
        }
        listView.setAdapter(new LeaderGlobalAdapter(myDockActivity, mCaloriesObject));
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
     //   unbinder.unbind();
    }
}
