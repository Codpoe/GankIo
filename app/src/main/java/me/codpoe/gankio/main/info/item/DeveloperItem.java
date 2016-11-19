package me.codpoe.gankio.main.info.item;

import me.drakeet.multitype.Item;

/**
 * Created by Codpoe on 2016/11/15.
 */
public class DeveloperItem implements Item {

    public int mAvatar;
    public String mDeveloperDesc;
    public String mWeiboUrl;
    public String mGitHubUrl;
    public String mMail;

    public DeveloperItem(int avatar, String developerDesc, String weiboUrl, String gitHubUrl, String mail) {
        mAvatar = avatar;
        mDeveloperDesc = developerDesc;
        mWeiboUrl = weiboUrl;
        mGitHubUrl = gitHubUrl;
        mMail = mail;
    }

}