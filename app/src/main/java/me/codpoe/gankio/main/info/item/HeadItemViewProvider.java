package me.codpoe.gankio.main.info.item;

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
 * Created by Codpoe on 2016/11/15.
 */
public class HeadItemViewProvider
        extends ItemViewProvider<HeadItem, HeadItemViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_info_head, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull HeadItem headItem) {
        holder.typeTv.setText(headItem.mType);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.type_tv)
        TextView typeTv;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}