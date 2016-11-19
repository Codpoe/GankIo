package me.codpoe.gankio.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by Codpoe on 2016/11/18.
 */

public class AnimationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    private int mDuration = 300;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mLastPosition = -1;
    private boolean mIsFirstShowOnly = false;

    public AnimationAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        mAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mAdapter.onBindViewHolder(holder, position);
        int adapterPosition = holder.getAdapterPosition();

        if (!mIsFirstShowOnly || adapterPosition > mLastPosition) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(getAnimator(holder.itemView));
            animatorSet.setDuration(mDuration).setInterpolator(mInterpolator);
            animatorSet.start();
            mLastPosition = adapterPosition;
        } else {
            ViewCompat.setAlpha(holder.itemView, 1);
            ViewCompat.setScaleY(holder.itemView, 1);
            ViewCompat.setScaleX(holder.itemView, 1);
            ViewCompat.setTranslationY(holder.itemView, 0);
            ViewCompat.setTranslationX(holder.itemView, 0);
            ViewCompat.setRotation(holder.itemView, 0);
            ViewCompat.setRotationY(holder.itemView, 0);
            ViewCompat.setRotationX(holder.itemView, 0);
            ViewCompat.setPivotY(holder.itemView, holder.itemView.getMeasuredHeight() / 2);
            ViewCompat.setPivotX(holder.itemView, holder.itemView.getMeasuredWidth() / 2);
            ViewCompat.animate(holder.itemView).setInterpolator(null).setStartDelay(0);
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        mAdapter.onViewRecycled(holder);
        super.onViewRecycled(holder);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        mAdapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        mAdapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    public Animator[] getAnimator(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.85f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.85f, 1f);
        return new ObjectAnimator[] {scaleX, scaleY};
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setFirstShowOnly(boolean firstShowOnly) {
        mIsFirstShowOnly = firstShowOnly;
    }

}
