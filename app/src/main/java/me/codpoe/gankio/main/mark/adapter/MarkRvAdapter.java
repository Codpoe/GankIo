package me.codpoe.gankio.main.mark.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;
import me.codpoe.gankio.data.bean.MarkBean;

/**
 * Created by Codpoe on 2016/10/10.
 */

public class MarkRvAdapter extends RecyclerView.Adapter<MarkRvAdapter.ViewHolder> {

    private Context mContext;
    private List<MarkBean> mList;

    private OnMarkItemClickListener mListener;

    public MarkRvAdapter(Context context, List<MarkBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_gank, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTitleTv.setText(mList.get(position).getDesc());
        holder.mWhoTv.setText("via. " + mList.get(position).getWho());
        holder.mPublishTv.setText(mList.get(position).getPublishedAt().substring(0, 10));
        holder.mTypeTv.setVisibility(View.VISIBLE);
        holder.mTypeTv.setText("type. " + mList.get(position).getType());

        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMarkItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnMarkItemClickListener listener) {
        mListener = listener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_tv)
        TextView mTitleTv;
        @BindView(R.id.who_tv)
        TextView mWhoTv;
        @BindView(R.id.publish_tv)
        TextView mPublishTv;
        @BindView(R.id.type_tv)
        TextView mTypeTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnMarkItemClickListener {
        void onMarkItemClick(View view, int position);
    }

}
