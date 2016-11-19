package me.codpoe.gankio.search;

import java.util.List;

import me.codpoe.gankio.data.bean.SearchBean;

/**
 * Created by Codpoe on 2016/10/23.
 */

public interface SearchContract {
    interface View {
        void showSearch(List<SearchBean.ResultsBean> list);
        void showMsg(String msg);
    }

    interface Presenter {
        void loadSearch(String query, String category, int page);
        void saveData(SearchBean.ResultsBean resultsBean);
        void deleteataById(String id);
        boolean isExist(String id);
    }
}
