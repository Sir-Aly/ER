package com.example.easyreach;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private myadapter mAdapter1;
    private Drawable deleteIcon;
    private ColorDrawable background;
    private ColorDrawable swipeBackground;
    private int swipeDeleteBackground;
    private int deleteIconMargin;
    private Context context;
    private myadapter_jobs mAdapter2;

    public SwipeToDeleteCallback(myadapter adapter, Context context) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT); // Specify the swipe directions
        mAdapter1 = adapter;
        this.context = context;
        background = new ColorDrawable(ContextCompat.getColor(context, R.color.swipeDeleteBackground)); // Replace with your desired background color
        deleteIconMargin = context.getResources().getDimensionPixelSize(R.dimen.delete_icon_margin); // Replace with the margin size for the delete icon
        deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete);
        swipeDeleteBackground = ContextCompat.getColor(context, R.color.swipeDeleteBackground);
    }

    public SwipeToDeleteCallback(myadapter_jobs adapter, Context context) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT); // Specify the swipe directions
        mAdapter2 = adapter;
        this.context = context;
        deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete);
        swipeDeleteBackground = ContextCompat.getColor(context, R.color.swipeDeleteBackground);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCanceled = dX == 0 && !isCurrentlyActive;

        if (isCanceled) {
            clearCanvas(canvas, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        // Draw the red background
        Paint paint = new Paint();
        paint.setColor(swipeDeleteBackground);
        canvas.drawRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom(), paint);


        int deleteIconMargin = context.getResources().getDimensionPixelSize(R.dimen.delete_icon_margin);
        int deleteIconTop = itemView.getTop() + (itemHeight - deleteIcon.getIntrinsicHeight()) / 2;
        int deleteIconBottom = deleteIconTop + deleteIcon.getIntrinsicHeight();
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - deleteIcon.getIntrinsicWidth();
        int deleteIconRight = itemView.getRight() - deleteIconMargin;

        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        deleteIcon.draw(canvas);
    }

    private void clearCanvas(Canvas canvas, float left, float top, float right, float bottom) {
        Paint paint = new Paint();
        paint.setColor(swipeDeleteBackground);
        canvas.drawRect(left, top, right, bottom, paint);
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position;
        if (mAdapter1 != null) {
            position = viewHolder.getAdapterPosition();
            mAdapter1.deleteItem(position);
        } else if (mAdapter2 != null) {
            position = viewHolder.getAdapterPosition();
            mAdapter2.deleteItem(position);
        }
    }
}
