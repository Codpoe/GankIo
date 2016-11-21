package me.codpoe.gankio.main.gank.home;

import android.content.Context;

import java.util.List;

import me.codpoe.gankio.data.bean.AllBean;
import me.codpoe.gankio.data.bean.DayBean;
import me.codpoe.gankio.main.gank.item.GankSubItem;
import me.drakeet.multitype.Item;

/**
 * Created by Codpoe on 2016/11/13.
 */

public interface HomeContract {

    interface View {
        void showData(List<AllBean.ResultsBean> list);
        void showItem(Item item);
        void showAllItem(List<Item> itemList);
        void showMsg(String msg);
        void complete();
    }

    interface Presenter {
        void loadData(String day);
        void loadDataByType(String type, int page);
        String loadDate(boolean isNull);
        List<Item> flattenData(List<DayBean.ResultsBean.GankBean> postList);
        void saveData(GankSubItem item);
        void saveSheetData(AllBean.ResultsBean resultsBean);
        void deleteDataById(String id);
        boolean isExist(String id);
        void share(Context context, String url, String title);
    }

}
