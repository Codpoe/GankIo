package me.codpoe.gankio.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import me.codpoe.gankio.R;

/**
 * Created by Codpoe on 2016/11/19.
 */

public class ArrowView extends View {

    private int mWidth;
    private int mHeight;
    private int mCenterX;
    private int mCenterY;
    private Paint mPaint;
    private int mColor;
    private float mOffset;

    public ArrowView(Context context) {
        this(context, null);
    }

    public ArrowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArrowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArrowView);
            mColor = typedArray.getColor(R.styleable.ArrowView_arrow_color, getResources().getColor(R.color.colorAccent));
            typedArray.recycle();
        }

        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCenterX = w / 2;
        mCenterY = h / 2;
        mOffset = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(mCenterX, mCenterY);

        Path path = new Path();
        path.moveTo(-mWidth * 0.1f, 0);
        path.lineTo(0, -mHeight * 0.1f + mHeight * 0.2f * mOffset);
        path.lineTo(mWidth * 0.1f, 0);

        canvas.drawPath(path, mPaint);
        canvas.restore();
    }

    public void setOffset(float offset) {
        if (offset >= 0) {
            mOffset = offset;
            invalidate();
        }
    }

}
