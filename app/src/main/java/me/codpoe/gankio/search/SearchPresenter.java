package me.codpoe.gankio.search;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import me.codpoe.gankio.data.Repository;
import me.codpoe.gankio.data.bean.MarkBean;
import me.codpoe.gankio.data.bean.SearchBean;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Codpoe on 2016/10/23.
 */

public class SearchPresenter implements SearchContract.Presenter {

    private Context mContext;
    private SearchContract.View mView;
    private Repository mRepository;

    @Inject
    public SearchPresenter(SearchContract.View view) {
        mContext = (Context)view;
        mView = view;
        mRepository = Repository.getInstance();
    }

    @Override
    public void loadSearch(String query, String category, int page) {
        mRepository.getSearch(query, category, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SearchBean.ResultsBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<SearchBean.ResultsBean> resultsBeen) {
                        mView.showSearch(resultsBeen);
                    }
                });
    }

    @Override
    public void saveData(SearchBean.ResultsBean resultsBean) {
        MarkBean markBean = new MarkBean();
        markBean.setId(resultsBean.getGanhuo_id());
        markBean.setDesc(resultsBean.getDesc());
        markBean.setPublishedAt(resultsBean.getPublishedAt());
        markBean.setType(resultsBean.getType());
        markBean.setWho(resultsBean.getWho());
        markBean.setUrl(resultsBean.getUrl());
        mRepository.insertMark(markBean);
    }

    @Override
    public void deleteataById(String id) {
        mRepository.deleteMarkById(id);
    }

    @Override
    public boolean isExist(String id) {
        if (mRepository.queryMarkById(id).size() >= 1) {
            return true;
        } else {
            return false;
        }
    }

}
