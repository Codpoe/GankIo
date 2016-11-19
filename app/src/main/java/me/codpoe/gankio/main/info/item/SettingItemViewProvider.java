package me.codpoe.gankio.main.info.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Codpoe on 2016/11/15.
 */
public class SettingItemViewProvider extends ItemViewProvider<SettingItem, SettingItemViewProvider.ViewHolder> {

    private static final String DAY_NIGHT_MODE = "day_night_mode";
    private static final String NIGHT_MODE = "night_mode";

    private OnClickListener mOnClickListener;

    public SettingItemViewProvider(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_info_setting, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull SettingItem settingItem) {
        holder.dayNightSwitch.setChecked(settingItem.mIsNightMode);
        holder.dayNightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mOnClickListener.onNightModeSwitch(b);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.day_night_switch)
        SwitchCompat dayNightSwitch;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickListener {
        void onNightModeSwitch(boolean isChecked);
    }

}