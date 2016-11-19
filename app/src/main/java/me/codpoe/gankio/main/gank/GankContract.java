package me.codpoe.gankio.main.gank;

import android.content.Context;

import java.util.List;

import me.codpoe.gankio.data.bean.AllBean;

/**
 * Created by Codpoe on 2016/10/8.
 */

public interface GankContract {
    interface View {
        void showData(List<AllBean.ResultsBean> list);
        void showMsg(String msg);
    }

    interface Presenter {
        void loadData(String type, int page);
        void saveData(AllBean.ResultsBean resultsBean);
        void deleteDataById(String id);
        boolean isExist(String id);
        void share(Context context, String url, String title);
    }
}
