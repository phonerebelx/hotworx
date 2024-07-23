package com.hotworx.ui.adapters.abstarct;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hotworx.interfaces.OnViewHolderClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class RecyclerViewListAdapter<T> extends RecyclerView.Adapter<RecyclerViewListAdapter.RecyclerviewViewHolder> {
    private List<T> mList = new ArrayList<>();
    private Context mContext;
    private OnViewHolderClick mListener;

    public RecyclerViewListAdapter(Context context) {
        this(context, null);
    }

    public RecyclerViewListAdapter(Context context, OnViewHolderClick listener) {
        super();
        mContext = context;
        mListener = listener;
    }

    protected abstract View createView(Context context, ViewGroup viewGroup, int viewType);

    protected abstract void bindView(T item, RecyclerviewViewHolder viewHolder);

    protected abstract int bindItemViewType(int position);

    protected abstract int bindItemId(int position);

    @Override
    public int getItemViewType(int position) {
        return bindItemViewType(position);
    }

    @Override
    public RecyclerviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new RecyclerviewViewHolder(createView(mContext, viewGroup, viewType), mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerviewViewHolder viewHolder, int position) {
        bindView(getItem(position), viewHolder);
    }

    @Override
    public long getItemId(int position) {
        return bindItemId(position);
    }

    @Override
    public int getItemCount() {
        if (mList == null) return 0;
        return mList.size();
    }

    public T getItem(int index) {
        return ((mList != null && index < mList.size()) ? mList.get(index) : null);
    }

    public Context getContext() {
        return mContext;
    }

    public void addAll(List<T> list) {
        if (list == null) return;
        mList = list;
        notifyDataSetChanged();
    }

    public void addItem(@NonNull T entity) {
        if (mList == null) return;
        mList.add(entity);
        notifyDataSetChanged();
    }


    public void updateItem(int position, @NonNull T entity) {
        if (position > mList.size()) return;
        mList.set(position, entity);
        notifyItemChanged(position);
    }

    public void removeItem(int index) {
        if (index > mList.size()) return;
        mList.remove(index);
        notifyItemRemoved(index);
    }

    public void removeItem(@NonNull T entity) {
        mList.remove(entity);
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return mList;
    }

    public void setClickListener(OnViewHolderClick listener) {
        mListener = listener;
    }

    public void restoreItem(T deletedItem, int deletedIndex, boolean refreshList) {
        if (deletedItem == null && deletedIndex > mList.size()) return;

        mList.add(deletedIndex, deletedItem);
        // notify item added by position
        if (refreshList)
            notifyDataSetChanged();
        else
            notifyItemInserted(deletedIndex);
    }

    public static class RecyclerviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Map<Integer, View> mMapView;
        private OnViewHolderClick mListener;

        public RecyclerviewViewHolder(View view, OnViewHolderClick listener) {
            super(view);
            mMapView = new HashMap<>();
            mMapView.put(0, view);
            mListener = listener;

            if (mListener != null)
                view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null)
                mListener.onItemClick(view, getAdapterPosition());
        }

        public void initViewList(int[] idList) {
            for (int id : idList)
                initViewById(id);
        }

        public void initViewById(int id) {
            View view = (getView() != null ? getView().findViewById(id) : null);

            if (view != null)
                mMapView.put(id, view);
        }

        public View getView() {
            return getView(0);
        }

        public View getView(int id) {
            if (mMapView.containsKey(id))
                return mMapView.get(id);
            else
                initViewById(id);

            return mMapView.get(id);
        }
    }

    public void append(List<T> list) {
        if (list == null) return;
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        while (getItemCount() > 0) {
            removeItem(getItem(0));
        }
    }

}