package me.codpoe.gankio.main.gank.item;

import me.drakeet.multitype.Item;

/**
 * Created by Codpoe on 2016/11/14.
 */
public class GankImgSubItem extends GankSubItem implements Item {

    public String mImgUrl;

    public GankImgSubItem(String id, String title, String who, String type, String publishAt, String url, String imgUrl) {
        super(id, title, who, type, publishAt, url);
        mImgUrl = imgUrl;
    }

}