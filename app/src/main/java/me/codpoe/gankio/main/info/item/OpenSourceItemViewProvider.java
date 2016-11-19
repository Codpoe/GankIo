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
public class OpenSourceItemViewProvider extends ItemViewProvider<OpenSourceItem, OpenSourceItemViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_info_open_source, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull OpenSourceItem openSourceItem) {
        holder.libTv.setText(openSourceItem.mLib);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lib_tv)
        TextView libTv;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}