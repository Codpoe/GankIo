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

    private static final int TOP_TO_BOTTOM = 0;
    private static final int BOTTOM_TO_TOP = 1;
    private static final int LEFT_TO_RIGHT = 2;
    private static final int RIGHT_TO_LEFT = 4;

    private int mWidth;
    private int mHeight;
    private int mCenterX;
    private int mCenterY;
    private float mOffset;
    private Paint mPaint;
    private int mPaintWidth;
    private int mColor;
    private int mOrientation;

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
            mOrientation = typedArray.getInt(R.styleable.ArrowView_arrow_orientation, TOP_TO_BOTTOM);
            mColor = typedArray.getColor(R.styleable.ArrowView_arrow_color, getResources().getColor(R.color.colorAccent));
            mPaintWidth = typedArray.getDimensionPixelSize(R.styleable.ArrowView_arrow_width, 6);
            typedArray.recycle();
        }

        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mPaintWidth);
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
        switch (mOrientation) {
            case TOP_TO_BOTTOM:
                float leftRight1 = mHeight * (0.05f - 0.1f * mOffset);
                path.moveTo(-mWidth * 0.1f, leftRight1); // 0.05 <-> -0.05
                path.lineTo(0, mHeight * (0.1f * mOffset - 0.05f)); // -0.05 <-> 0.05
                path.lineTo(mWidth * 0.1f, leftRight1); // 0.05 <-> -0.05
                break;
            case BOTTOM_TO_TOP:
                float leftRight2 = mHeight * (0.1f * mOffset - 0.05f);
                path.moveTo(-mWidth * 0.1f, leftRight2); // -0.05 <-> 0.05
                path.lineTo(0, mHeight * (0.05f - 0.1f * mOffset)); // 0.05 <-> -0.05
                path.lineTo(mWidth * 0.1f, leftRight2); // -0.05 <-> 0.05
                break;
            case LEFT_TO_RIGHT:
                float topBottom1 = mWidth * (0.05f - 0.1f * mOffset);
                path.moveTo(topBottom1, -mHeight * 0.1f); // 0.05 <-> -0.05
                path.lineTo(mWidth * (0.1f * mOffset - 0.05f), 0); // -0.05 <-> 0.05
                path.lineTo(topBottom1, mHeight * 0.1f); // 0.05 <-> -0.05
                break;
            case RIGHT_TO_LEFT:
                float topBottom2 = mWidth * (0.1f * mOffset - 0.05f);
                path.moveTo(topBottom2, -mHeight * 0.1f); // -0.05 <-> 0.05
                path.lineTo(mWidth * (0.05f - 0.1f * mOffset), 0); // 0.05 <-> -0.05
                path.lineTo(topBottom2, mHeight * 0.1f); // -0.05 <-> 0.05
                break;
        }

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
