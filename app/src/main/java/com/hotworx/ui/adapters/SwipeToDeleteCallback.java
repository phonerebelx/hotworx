package com.hotworx.ui.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.hotworx.R;
import com.hotworx.interfaces.OnItemClickInterface;
import com.hotworx.interfaces.OnSwipeListener;
import com.hotworx.ui.fragments.Nutritionist.NutritionistFragment;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private NutritionistFragment mAdapter;
    private Drawable icon;
    private final OnSwipeListener mOnItemSwipe;

    public SwipeToDeleteCallback(NutritionistFragment adapter, OnSwipeListener onItemSwipe) {
        super(0, ItemTouchHelper.LEFT);
        mAdapter = adapter;
        mOnItemSwipe = onItemSwipe;
        icon = ContextCompat.getDrawable(mAdapter.requireContext(),
                R.drawable.ic_trash);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//        mAdapter.deletedItemPosition = viewHolder.getAdapterPosition();
//        mAdapter.deleteItem(mAdapter.deletedItemPosition);
        mOnItemSwipe.onItemSwipe(viewHolder.getAdapterPosition(), String.valueOf(viewHolder.itemView.getTag()));
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;
        ColorDrawable background;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

//        if (dX > 0) { // Swiping to the right
//            int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
//            int iconRight = itemView.getLeft() + iconMargin;
//         //   icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
//            background = new ColorDrawable(Color.RED);
//            background.setBounds(itemView.getLeft(), itemView.getTop(),
//                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
//                    itemView.getBottom());
//
//        } else
            if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
//            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background = new ColorDrawable(Color.RED);
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background = new ColorDrawable(Color.RED);
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(c);
        icon.draw(c);
    }
}
