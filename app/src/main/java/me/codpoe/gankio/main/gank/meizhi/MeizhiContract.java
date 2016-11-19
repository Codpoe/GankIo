package me.codpoe.gankio.main.gank.meizhi;

import java.util.List;

import me.codpoe.gankio.data.bean.AllBean;

/**
 * Created by Codpoe on 2016/10/24.
 */

public interface MeizhiContract {
    interface View {
        void showMeizhi(List<AllBean.ResultsBean> meizhiList);
        void showMsg(String msg);
    }

    interface Presenter {
        void loadMeizhi(int page);
    }
}
