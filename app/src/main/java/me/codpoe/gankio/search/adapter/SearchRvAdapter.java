package me.codpoe.gankio.search.adapter;

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
import me.codpoe.gankio.data.bean.SearchBean;

/**
 * Created by Codpoe on 2016/10/23.
 */

public class SearchRvAdapter extends RecyclerView.Adapter<SearchRvAdapter.ViewHolder> {

    private Context mContext;
    private List<SearchBean.ResultsBean> mList;

    public SearchRvAdapter(Context context, List<SearchBean.ResultsBean> list) {
        mContext = context;
        mList = list;
    }

    private OnSearchItemClickListener mListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_gank, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mTitleTv.setText(mList.get(position).getDesc());
        holder.mWhoTv.setText("via. " + mList.get(position).getWho());
        holder.mTypeTv.setVisibility(View.VISIBLE);
        holder.mTypeTv.setText("type. " + mList.get(position).getType());
        holder.mPublishTv.setText(mList.get(position).getPublishedAt().substring(0, 10));

        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSearchItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mListener.onSearchItemLongClick(holder.itemView, holder.getLayoutPosition());
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnSearchItemClickListener listener) {
        mListener = listener;
    }

    public interface OnSearchItemClickListener {
        void onSearchItemClick(View view, int position);
        void onSearchItemLongClick(View view, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_tv)
        TextView mTitleTv;
        @BindView(R.id.who_tv)
        TextView mWhoTv;
        @BindView(R.id.type_tv)
        TextView mTypeTv;
        @BindView(R.id.publish_tv)
        TextView mPublishTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
