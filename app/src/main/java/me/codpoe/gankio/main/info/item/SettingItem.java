package me.codpoe.gankio.main.info.item;

import me.drakeet.multitype.Item;

/**
 * Created by Codpoe on 2016/11/15.
 */
public class SettingItem implements Item {

    public boolean mIsNightMode;

    public SettingItem(boolean isNightMode) {
        mIsNightMode = isNightMode;
    }

}