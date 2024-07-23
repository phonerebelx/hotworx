package com.hotworx.helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.hotworx.R;

public class Circle extends View {

    private static final int START_ANGLE_POINT = 90;

    private final Paint paint;
    //private final Paint textPaint;

    private RectF rect;
    int circleDimension = 0;
    private float angle;
    int strokeWidth;
    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);

        strokeWidth = (int) (context.getResources().getDimension(R.dimen.circle_stroke) / context.getResources().getDisplayMetrics().density);
        circleDimension = (int) (context.getResources().getDimension(R.dimen.circle_dimension) / context.getResources().getDisplayMetrics().density);

        paint = new Paint();
        //textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        //Circle color
        paint.setColor(context.getResources().getColor(R.color.colorPrimary));

        //size 200x200 example
        // rect = new RectF(100, 100, 500 + 100, 500 + 100);


        //Initial Angle (optional, it can be zero)
        angle = 0;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = canvas.getHeight() / 2;
        int width = canvas.getWidth() / 2;
        rect = new RectF();
        rect.set(strokeWidth, strokeWidth, width + circleDimension, height + circleDimension);

        canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paint);
        if(angle==360) {

        }
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}