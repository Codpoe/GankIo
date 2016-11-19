package me.codpoe.gankio.data;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.codpoe.gankio.data.bean.AllBean;
import me.codpoe.gankio.data.bean.DayBean;
import me.codpoe.gankio.data.bean.MarkBean;
import me.codpoe.gankio.data.bean.SearchBean;
import me.codpoe.gankio.data.retrofit.BaseRetrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by Codpoe on 2016/10/9.
 */

public class Repository {

    private static Repository sRepository;
    private Realm mRealm;

    private Repository() {
        mRealm = Realm.getDefaultInstance();
    }

    public synchronized static Repository getInstance() {
        if (sRepository == null) {
            sRepository = new Repository();
        }
        return sRepository;
    }

    /**
     * get data of a specific date
     * @param day
     * @return
     */
    public Observable<DayBean.ResultsBean> getDayData(String day) {

        return BaseRetrofit.getInstance().getDayFromNet(day)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<DayBean, Observable<DayBean.ResultsBean>>() {
                    @Override
                    public Observable<DayBean.ResultsBean> call(DayBean dayBean) {
                        if (dayBean.getCategory().size() != 0) {
                            return Observable.just(dayBean.getResults());
                        } else {
                            return Observable.error(new Exception());
                        }
                    }
                });

    }

    /**
     * get main data
     * @param type
     * @param page
     * @return
     */
    public Observable<List<AllBean.ResultsBean>> getAll(String type, int page) {

        return BaseRetrofit.getInstance().getAllFromNet(type, page)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<AllBean, Observable<AllBean.ResultsBean>>() {
                    @Override
                    public Observable<AllBean.ResultsBean> call(AllBean allBean) {
                        return Observable.from(allBean.getResults());
                    }
                })
                .toList();
    }

    /**
     * get search results
     * @param query
     * @param category
     * @param page
     * @return
     */
    public Observable<List<SearchBean.ResultsBean>> getSearch(String query, String category, int page) {

        return BaseRetrofit.getInstance().getSearchFromNet(query, category, page)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<SearchBean, Observable<SearchBean.ResultsBean>>() {
                    @Override
                    public Observable<SearchBean.ResultsBean> call(SearchBean searchBean) {
                        return Observable.from(searchBean.getResults());
                    }
                })
                .toList();
    }

    /**
     * insert one item
     * @param markBean
     */
    public void insertMark(final MarkBean markBean) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.copyToRealmOrUpdate(markBean);
            }
        });
    }

    /**
     * delete one item by id
     * @param id
     */
    public void deleteMarkById(String id) {
        final RealmResults<MarkBean> results = mRealm.where(MarkBean.class).equalTo("id", id).findAll();
        mRealm.beginTransaction();
        results.deleteAllFromRealm();
        mRealm.commitTransaction();
    }

    /**
     * query all item
     * @return
     */
    public RealmResults<MarkBean> queryMark() {
        return mRealm.where(MarkBean.class).findAll();
    }

    /**
     * query item by type
     * @return
     */
    public RealmResults<MarkBean> queryMarkByType(String type) {
        if (type.equals("All")) {
            return mRealm.where(MarkBean.class).findAll();
        } else {
            return mRealm.where(MarkBean.class).equalTo("type", type).findAll();
        }
    }

    public RealmResults<MarkBean> queryMarkById(String id) {
        return mRealm.where(MarkBean.class).equalTo("id", id).findAll();
    }

    /**
     * close Realm
     */
    public void closeRealm() {
        mRealm.close();
    }

}
