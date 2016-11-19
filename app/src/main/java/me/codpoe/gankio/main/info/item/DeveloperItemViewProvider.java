package me.codpoe.gankio.main.info.item;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;
import me.codpoe.gankio.util.DialogUtil;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Codpoe on 2016/11/15.
 */
public class DeveloperItemViewProvider extends ItemViewProvider<DeveloperItem, DeveloperItemViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_info_developer, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final DeveloperItem developerItem) {
        holder.avatarImg.setImageResource(developerItem.mAvatar);
        holder.developerDesc.setText(developerItem.mDeveloperDesc);
        holder.developerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showDeveloperBottomDialog(view.getContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.weibo_tv:
                                Intent weiboIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(developerItem.mWeiboUrl));
                                view.getContext().startActivity(weiboIntent);
                                break;
                            case R.id.github_tv:
                                Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(developerItem.mGitHubUrl));
                                view.getContext().startActivity(githubIntent);
                                break;
                            case R.id.mail_tv:
                                Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(developerItem.mMail));
                                view.getContext().startActivity(mailIntent);
                                break;
                        }
                        DialogUtil.hideBottomDialog();
                    }
                });
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.developer_card)
        CardView developerCard;
        @BindView(R.id.avatar_img)
        ImageView avatarImg;
        @BindView(R.id.developer_desc)
        TextView developerDesc;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}