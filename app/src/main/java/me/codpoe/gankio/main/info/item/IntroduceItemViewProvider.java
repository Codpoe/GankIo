package me.codpoe.gankio.main.info.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Codpoe on 2016/11/15.
 */
public class IntroduceItemViewProvider extends ItemViewProvider<IntroduceItem, IntroduceItemViewProvider.ViewHolder> {

    private OnClickListener mOnClickListener;

    public IntroduceItemViewProvider(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_info_introduce, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull IntroduceItem introduceItem) {
        holder.introduceTv.setText(R.string.introduce_content);
        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onShareBtnClick();
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.introduce_tv)
        TextView introduceTv;
        @BindView(R.id.share_btn)
        Button shareBtn;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickListener {
        void onShareBtnClick();
    }

}