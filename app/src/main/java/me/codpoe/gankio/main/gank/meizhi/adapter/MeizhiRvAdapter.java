package me.codpoe.gankio.main.gank.meizhi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;
import me.codpoe.gankio.app.GankIoApplication;
import me.codpoe.gankio.data.bean.AllBean;
import me.codpoe.gankio.util.DpPxUtil;

/**
 * Created by Codpoe on 2016/10/24.
 */

public class MeizhiRvAdapter extends RecyclerView.Adapter<MeizhiRvAdapter.ViewHolder> {

    private Context mContext;
    private List<AllBean.ResultsBean> mMeizhiList = new ArrayList<>();
    private int mImgMargin;

    private OnItemClickListener mListener;

    public MeizhiRvAdapter(Context context, List<AllBean.ResultsBean> meizhiList) {
        mContext = context;
        mMeizhiList = meizhiList;
        mImgMargin = DpPxUtil.dp2px(mContext, 4);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meizhi, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        /**
         * 大概、可能、或许解决了 StaggeredGridLayout 因「快速滚动」而出现的图片错乱问题，以及因「快速返回」而出现的图片错位问题
         */
        if (mMeizhiList.get(holder.getAdapterPosition()).getHeight() > 0) {
            holder.mMeizhiImg.getLayoutParams().height = mMeizhiList.get(holder.getAdapterPosition()).getHeight();
        }

        holder.mMeizhiImg.setImageResource(R.color.item_meizhi_bg);
        holder.mMeizhiImg.setTag(mMeizhiList.get(position).getId());
        Glide.with(mContext)
                .load(mMeizhiList.get(position).getUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(GankIoApplication.SCREEN_WIDTH / 2, GankIoApplication.SCREEN_HEIGHT / 2) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (holder.getAdapterPosition() != RecyclerView.NO_POSITION && holder.mMeizhiImg.getTag().equals(mMeizhiList.get(position).getId())) {
                            if (mMeizhiList.get(position).getHeight() == 0) {
                                int width = resource.getWidth();
                                int height = resource.getHeight();
                                int realWidth = GankIoApplication.SCREEN_WIDTH / 2 - mImgMargin;
                                int realHeight = (int) (realWidth * ((float) height / width));
                                mMeizhiList.get(position).setHeight(realHeight);
                                holder.mMeizhiImg.getLayoutParams().width = realWidth;
                                holder.mMeizhiImg.getLayoutParams().height = realHeight;
                            }
                            holder.mMeizhiImg.setImageBitmap(resource);
                        }
                    }
                });
        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return mMeizhiList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // click interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // view holder
    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.meizhi_img)
        ImageView mMeizhiImg;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
