package me.codpoe.gankio.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import me.codpoe.gankio.R;

import static android.R.attr.orientation;

/**
 * Created by Codpoe on 2016/11/5.
 */

public class TagView extends CardView {

    private static final int LEFT_TOP = 1;
    private static final int RIGHT_TOP = 2;
    private static final int LEFT_BOTTOM = 3;
    private static final int RIGHT_BOTTOM = 4;

    private static final int DEFAULT_DISTANCE = 40;
    private static final int DEFAULT_HEIGHT = 20;
    private static final int DEFAULT_TEXT_SIZE = 14;
    private static final int DEFAULT_BACKGROUND_COLOR = 0x9F27CDC0;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_ALPHA = 127;
    private static final int DEFAULT_ORIENTATION = LEFT_TOP;

    private Context mContext;

    private int mDistance; // 交点跟顶点的距离
    private int mTagHeight; // tag 的高度
    private String mTag;
    private int mBackgroundColor;
    private int mTextSize;
    private int mTextColor;
    private int mAlpha;
    private boolean mIsVisible = true;
    private int mOrientation;

    private float mStartPosX;
    private float mStartPosY;
    private float mEndPosX;
    private float mEndPosY;

    private Paint mRectPaint;
    private Paint mTextPaint;
    private Path mRectPath;
    private Rect mTextBound;

    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!mIsVisible || mTag == null) {
            return;
        }

        float realDistance = mDistance + mTagHeight / 2;
        calcOffset(realDistance, getMeasuredWidth(), getMeasuredHeight());

        mRectPaint.setColor(mBackgroundColor);
        if (mAlpha != 0) {
            mRectPaint.setAlpha(mAlpha);
        }
        mRectPaint.setStrokeWidth(mTagHeight);

        mRectPath.reset();
        mRectPath.moveTo(mStartPosX, mStartPosY);
        mRectPath.lineTo(mEndPosX, mEndPosY);
        canvas.drawPath(mRectPath, mRectPaint);

        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.getTextBounds(mTag, 0, mTag.length(), mTextBound);

        float begin_w_offset = (1.4142135f * realDistance) / 2 - mTextBound.width() / 2;
        if (begin_w_offset < 0) begin_w_offset = 0;

        canvas.drawTextOnPath(mTag, mRectPath, begin_w_offset, mTextBound.height() / 2, mTextPaint);
    }

    /**
     * initialise the attributes
     * @param attrs
     * @param defStyleAttr
     */
    private void init(AttributeSet attrs, int defStyleAttr) {
        // obtain the attributes
        TypedArray attributes = mContext.obtainStyledAttributes(attrs, R.styleable.TagView, defStyleAttr, 0);
        mTag = attributes.getString(R.styleable.TagView_tag_text);
        mDistance = attributes.getDimensionPixelSize(R.styleable.TagView_tag_distance, DEFAULT_DISTANCE);
        mTagHeight = attributes.getDimensionPixelSize(R.styleable.TagView_tag_height, DEFAULT_HEIGHT);
        mBackgroundColor = attributes.getColor(R.styleable.TagView_tag_backgroundColor, DEFAULT_BACKGROUND_COLOR);
        mTextSize = attributes.getDimensionPixelSize(R.styleable.TagView_tag_textSize, DEFAULT_TEXT_SIZE);
        mTextColor = attributes.getColor(R.styleable.TagView_tag_textColor, DEFAULT_TEXT_COLOR);
        mAlpha = attributes.getInt(R.styleable.TagView_tag_alpha, DEFAULT_ALPHA);
        mIsVisible = attributes.getBoolean(R.styleable.TagView_tag_visible, true);
        mOrientation = attributes.getInteger(R.styleable.TagView_tag_orientation, DEFAULT_ORIENTATION);
        attributes.recycle();

        // set up the rect paint
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeJoin(Paint.Join.ROUND);
        mRectPaint.setStrokeCap(Paint.Cap.ROUND);

        // set up the text paint
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeJoin(Paint.Join.ROUND);
        mTextPaint.setStrokeCap(Paint.Cap.ROUND);

        mRectPath = new Path();
        mTextBound = new Rect();
    }

    /**
     * calculate the start point and the end point of tag view
     * according to the orientation of tag view
     * @param realDistance
     * @param measuredWidth
     * @param measuredHeight
     */
    private void calcOffset(float realDistance, int measuredWidth, int measuredHeight) {
        switch (orientation) {
            case 1:
                mStartPosX = 0;
                mStartPosY = realDistance;
                mEndPosX = realDistance;
                mEndPosY = 0;
                break;
            case 2:
                mStartPosX = measuredWidth - realDistance;
                mStartPosY = 0;
                mEndPosX = measuredWidth;
                mEndPosY = realDistance;
                break;
            case 3:
                mStartPosX = 0;
                mStartPosY = measuredHeight - realDistance;
                mEndPosX = realDistance;
                mEndPosY = measuredHeight;
                break;
            case 4:
                mStartPosX = measuredWidth - realDistance;
                mStartPosY = measuredHeight;
                mEndPosX = measuredWidth;
                mEndPosY = measuredHeight - realDistance;
                break;
        }
    }

    public void setTag(String tag) {
        mTag = tag;
        invalidate();
    }

    public void setVisibility(boolean isVisible) {
        mIsVisible = isVisible;
        invalidate();
    }

}
