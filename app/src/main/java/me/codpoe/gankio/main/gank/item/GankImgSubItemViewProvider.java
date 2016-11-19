package me.codpoe.gankio.main.gank.item;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;
import me.codpoe.gankio.web.WebActivity;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Codpoe on 2016/11/14.
 */
public class GankImgSubItemViewProvider extends ItemViewProvider<GankImgSubItem, GankImgSubItemViewProvider.ViewHolder> {

    private GankSubItemViewProvider.OnGankItemClickListener mListener;

    public GankImgSubItemViewProvider(GankSubItemViewProvider.OnGankItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_home_gank_sub_img, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final GankImgSubItem gankImgSubItem) {

        holder.titleTv.setText(gankImgSubItem.mTitle);
        holder.whoTv.setText(gankImgSubItem.mWho);
        Glide.with(holder.itemView.getContext())
                .load(gankImgSubItem.mImgUrl)
                .placeholder(R.color.colorPrimary)
                .into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = WebActivity.newIntent(view.getContext(), gankImgSubItem.mUrl, gankImgSubItem.mTitle);
                view.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mListener.onGankItemLongClick(gankImgSubItem);
                return true;
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.who_tv)
        TextView whoTv;
        @BindView(R.id.img)
        ImageView img;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}