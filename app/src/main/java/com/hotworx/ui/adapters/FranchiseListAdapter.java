package com.hotworx.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hotworx.R;
import com.hotworx.helpers.TextViewHelper;
import com.hotworx.interfaces.OnViewHolderClick;
import com.hotworx.requestEntity.CompletedClassesPOJO;
import com.hotworx.requestEntity.FranchiseListResponseData;
import com.hotworx.ui.adapters.abstarct.RecyclerViewListAdapter;

public class FranchiseListAdapter extends RecyclerViewListAdapter<FranchiseListResponseData> {
    private LayoutInflater inflater;

    public FranchiseListAdapter(Context context, OnViewHolderClick listener) {
        super(context, listener);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        return inflater.inflate(R.layout.franchise_list_item, viewGroup, false);
    }

    @Override
    protected void bindView(FranchiseListResponseData item, RecyclerviewViewHolder viewHolder) {
        TextView txtLocation = (TextView) viewHolder.getView(R.id.txtLocation);
        TextViewHelper.setText(txtLocation, item.getLocation_name());
    }

    @Override
    protected int bindItemViewType(int position) {
        return 0;
    }

    @Override
    protected int bindItemId(int position) {
        return 0;
    }
}
