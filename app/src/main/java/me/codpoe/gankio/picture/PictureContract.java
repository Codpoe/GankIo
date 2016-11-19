package me.codpoe.gankio.picture;

import android.net.Uri;

import rx.Observable;

/**
 * Created by Codpoe on 2016/10/27.
 */

public interface PictureContract {

    interface View {
        void showMsg(String msg);
    }

    interface Presenter {
        void copyUrl(String url);
        Observable<Uri> savePic(String url, String id);
        void sharePic(Uri uri);
    }

}
