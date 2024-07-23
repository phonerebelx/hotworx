package com.hotworx.ui.fragments.ActivityScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;
import com.hotworx.requestEntity.CaloriesObject;
import com.hotworx.ui.adapters.ActivityListAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ListNinetyDaysFragment extends BaseFragment {
    Unbinder unbinder;
    private static final String NINETY_KEY = "ninety_key";
    @BindView(R.id.list_activity)
    ListView listView;

    public static ListNinetyDaysFragment newInstance(List<CaloriesObject> caloriesObjectList) {
        ListNinetyDaysFragment fragment = new ListNinetyDaysFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(NINETY_KEY, (Serializable) caloriesObjectList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_ninety,container,false);
        unbinder = ButterKnife.bind(this,view);
        List<CaloriesObject> mCaloriesObject = (List<CaloriesObject>) getArguments().getSerializable(NINETY_KEY);
        listView.setAdapter(new ActivityListAdapter(myDockActivity, mCaloriesObject));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideTitleBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //  unbinder.unbind();
    }
}
