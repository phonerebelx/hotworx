package com.hotworx.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hotworx.R;
import com.hotworx.helpers.TextViewHelper;
import com.hotworx.requestEntity.CompletedClassesPOJO;
import com.hotworx.ui.adapters.abstarct.RecyclerViewListAdapter;

public class HomeAdapter extends RecyclerViewListAdapter<CompletedClassesPOJO> {
    private LayoutInflater inflater;

    public HomeAdapter(Context context) {
        super(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected View createView(Context context, ViewGroup viewGroup, int viewType) {
        return inflater.inflate(R.layout.item_home, viewGroup, false);

    }

    @Override
    protected void bindView(CompletedClassesPOJO item, RecyclerviewViewHolder viewHolder) {
        TextView hotYogaKey = (TextView) viewHolder.getView(R.id.hotYogaKey);
        TextView hotYogaValue = (TextView) viewHolder.getView(R.id.hotYogaValue);
        TextViewHelper.setText(hotYogaKey, item.getType());
        TextViewHelper.setText(hotYogaValue, "Burned " + item.getBurn_calories() + " calories");
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
