package me.codpoe.gankio.main.info;

/**
 * Created by Codpoe on 2016/11/1.
 */

public interface InfoContract {

    interface View {
        void showMsg(String msg);
    }

    interface Presenter {
        void share(String text);
    }

}
