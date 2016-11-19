package me.codpoe.gankio.main.gank.adapter;

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
import me.codpoe.gankio.data.bean.AllBean;

/**
 * Created by Codpoe on 2016/10/9.
 */

public class CommonRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<AllBean.ResultsBean> mList;

    private OnCommonItemClickListener mListener;

    public CommonRvAdapter(Context context, List<AllBean.ResultsBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_gank, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder)holder).mTitleTv.setText(mList.get(position).getDesc());
        ((ViewHolder)holder).mWhoTv.setText("via. " + mList.get(position).getWho());
        ((ViewHolder)holder).mPublishTv.setText(mList.get(position).getPublishedAt().substring(0, 10));

        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCommonItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mListener.onCommonItemLongClick(holder.itemView, holder.getLayoutPosition());
                    return true;
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnCommonItemClickListener listener) {
        mListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_tv)
        TextView mTitleTv;
        @BindView(R.id.who_tv)
        TextView mWhoTv;
        @BindView(R.id.publish_tv)
        TextView mPublishTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnCommonItemClickListener {
        void onCommonItemClick(View view, int position);
        void onCommonItemLongClick(View view, int position);
    }

}
