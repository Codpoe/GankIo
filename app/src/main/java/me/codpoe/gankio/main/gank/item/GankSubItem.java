package me.codpoe.gankio.main.gank.item;

import me.drakeet.multitype.Item;

/**
 * Created by Codpoe on 2016/11/14.
 */
public class GankSubItem implements Item {

    public String mId;
    public String mTitle;
    public String mWho;
    public String mType;
    public String mPublishAt;
    public String mUrl;

    public GankSubItem(String id, String title, String who, String type, String publishAt, String url) {
        mId = id;
        mTitle = title;
        mWho = who;
        mType = type;
        mPublishAt = publishAt;
        mUrl = url;
    }

}