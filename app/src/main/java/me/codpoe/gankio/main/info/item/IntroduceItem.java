package me.codpoe.gankio.main.info.item;

import me.drakeet.multitype.Item;

/**
 * Created by Codpoe on 2016/11/15.
 */
public class IntroduceItem implements Item {

    public String mIntroduceContent;

    public IntroduceItem(String introduceContent) {
        mIntroduceContent = introduceContent;
    }

}