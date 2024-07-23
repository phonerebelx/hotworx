package com.hotworx.ui.fragments.History;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hotworx.R;
import com.hotworx.helpers.UIHelper;
import com.hotworx.interfaces.DialogBoxCallBack;
import com.hotworx.interfaces.InternetDialogBoxInterface;
import com.hotworx.interfaces.OnViewHolderClick;
import com.hotworx.requestEntity.CompletedClassesPOJO;
import com.hotworx.room.RoomBuilder;
import com.hotworx.room.model.SessionEnt;
import com.hotworx.ui.adapters.HistoryAdapter;
import com.hotworx.ui.adapters.HomeAdapter;
import com.hotworx.ui.adapters.MyCustomAdapter;
import com.hotworx.ui.adapters.abstarct.RecyclerViewListAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SessionHistoryFragment extends BaseFragment implements HistoryAdapter.OnItemClickListener{
    private Unbinder unbinder;
    @BindView(R.id.recyclerViewHistory)
    RecyclerView recyclerViewHistory;
    private HistoryAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_session_history, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initAdapter();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initAdapter() {
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getDockActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new HistoryAdapter(getDockActivity(), RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().getAllSession());
        adapter.setOnItemClickListener(this::onItemClick);
        recyclerViewHistory.setAdapter(adapter);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.sessions_history));
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        //super.onDestroy();
    }


    @Override
    public void onItemClick(SessionEnt sessionEnt) {
        UIHelper.showNoInternetDialog(myDockActivity, "Alert!", "Do you want to delete this Session?", "No", "Yes", new InternetDialogBoxInterface() {
          @Override
          public void onPositive() {
              adapter.remove(sessionEnt);
              RoomBuilder.getHotWorxDatabase(myDockActivity).getSessionTypeDao().deleteSessionByActivityId(sessionEnt.getActivity_id());
          }

          @Override
          public void onNegative() {

          }
      });

    }
}
