package me.codpoe.gankio.main.mark;

import io.realm.RealmResults;
import me.codpoe.gankio.data.bean.MarkBean;

/**
 * Created by Codpoe on 2016/10/10.
 */

public interface MarkContract {
    interface View {
        void showData(RealmResults<MarkBean> markBean);
        void showNoContent();
        void showMsg(String msg);
    }
    interface Presenter {
        void loadDataByType(String type);
        void deleteDataById(String id);
        void detachView();
    }
}
