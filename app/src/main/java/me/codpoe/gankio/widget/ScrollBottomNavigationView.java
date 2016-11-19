package me.codpoe.gankio.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;

import me.codpoe.gankio.util.DpPxUtil;

/**
 * Created by Codpoe on 2016/10/22.
 */

public class ScrollBottomNavigationView extends BottomNavigationView {

    private Context mContext;
    private ObjectAnimator mHideAnim;
    private ObjectAnimator mShowAnim;
    private boolean mIsHidden;
    private float mPxHeight;

    public ScrollBottomNavigationView(Context context) {
        super(context, null);
    }

    public ScrollBottomNavigationView(Context context, AttributeSet attr) {
        super(context, attr);
        mContext = context;
        init();

    }

    private void init() {
        mIsHidden = false;

        mPxHeight = DpPxUtil.dp2px(mContext, 56);

        mHideAnim = ObjectAnimator.ofFloat(this, "translationY", 0, mPxHeight)
                .setDuration(300);

        mShowAnim = ObjectAnimator.ofFloat(this, "translationY", mPxHeight, 0)
                .setDuration(300);

    }

    public void hide() {
        mIsHidden = true;
        mHideAnim.start();
    }

    public void show() {
        mIsHidden = false;
        mShowAnim.start();
    }

    public boolean isHidden() {
        return mIsHidden;
    }

}
