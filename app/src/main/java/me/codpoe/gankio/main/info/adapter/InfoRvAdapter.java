package me.codpoe.gankio.main.info.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;

/**
 * Created by Codpoe on 2016/10/30.
 */

public class InfoRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_INTRODUCE = 0;
    private static final int TYPE_SETTING = 1;
    private static final int TYPE_DEVELOPER = 2;
    private static final int TYPE_OPEN_SOURCE = 3;
    private static final int TYPE_FOOTER = 4;
    private static final String DAY_NIGHT_MODE = "day_night_mode";
    private static final String NIGHT_MODE = "night_mode";

    private Context mContext;
    private OnClickListener mClickListener;

    public InfoRvAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return 5;
    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_INTRODUCE;
            case 1:
                return TYPE_SETTING;
            case 2:
                return TYPE_DEVELOPER;
            case 3:
                return TYPE_OPEN_SOURCE;
            case 4:
                return TYPE_FOOTER;
        }
        return super.getItemViewType(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_INTRODUCE:
                return new IntroduceViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_info_introduce, parent, false));
            case TYPE_SETTING:
                return new SettingViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_info_setting, parent, false));
            case TYPE_DEVELOPER:
                return new DevelopViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_info_developer, parent, false));
            case TYPE_OPEN_SOURCE:
                return new OpenSourseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_info_open_source, parent, false));
            case TYPE_FOOTER:
                return new FooterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_info_footer, parent, false));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_INTRODUCE:
                if (mClickListener != null) {
                    ((IntroduceViewHolder) holder).shareBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mClickListener.onShareBtnClick();
                        }
                    });
                }
                break;
            case TYPE_SETTING:
                SharedPreferences sp = mContext.getSharedPreferences(DAY_NIGHT_MODE, Context.MODE_PRIVATE);
                ((SettingViewHolder) holder).dayNightSwitch.setChecked(sp.getBoolean(NIGHT_MODE, false));
                if (mClickListener != null) {
                    ((SettingViewHolder) holder).dayNightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            mClickListener.onNightModeSwitch(b);
                        }
                    });
                }
                break;
            case TYPE_DEVELOPER:
                if (mClickListener != null) {
                    ((DevelopViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mClickListener.onDeveloperClick();
                        }
                    });
                }
                break;
        }

    }

    public void setOnClickListener(OnClickListener listener) {
        mClickListener = listener;
    }

    static class IntroduceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.share_btn)
        Button shareBtn;

        public IntroduceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class SettingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.day_night_switch)
        SwitchCompat dayNightSwitch;

        public SettingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class DevelopViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.developer_card)
        CardView cardView;

        public DevelopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class OpenSourseViewHolder extends RecyclerView.ViewHolder {

        public OpenSourseViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnClickListener {
        void onShareBtnClick();
        void onNightModeSwitch(boolean isChecked);
        void onDeveloperClick();
    }

}
