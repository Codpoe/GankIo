package me.codpoe.gankio.main.gank.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Codpoe on 2016/11/14.
 */
public class GankHeadItemViewProvider extends ItemViewProvider<GankHeadItem, GankHeadItemViewProvider.ViewHolder> {

    private OnGankHeadClickListener mListener;
    public GankHeadItemViewProvider(OnGankHeadClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_home_gank_head, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GankHeadItem gankHeadItem) {
        holder.typeTv.setText(gankHeadItem.mType);
        if (mListener != null) {
            holder.typeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onGankHeadClick(gankHeadItem.mType);
                }
            });
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.type_tv)
        TextView typeTv;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnGankHeadClickListener {
        void onGankHeadClick(String type);
    }

}