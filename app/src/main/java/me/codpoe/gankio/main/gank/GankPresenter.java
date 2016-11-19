package me.codpoe.gankio.main.gank;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import javax.inject.Inject;

import me.codpoe.gankio.R;
import me.codpoe.gankio.data.Repository;
import me.codpoe.gankio.data.bean.AllBean;
import me.codpoe.gankio.data.bean.MarkBean;
import rx.Subscriber;

/**
 * Created by Codpoe on 2016/10/8.
 */

public class GankPresenter implements GankContract.Presenter {

    private GankContract.View mView;
    private Repository mRepository;

    @Inject
    public GankPresenter(GankContract.View view) {
        mView = view;
        mRepository = Repository.getInstance();
    }

    @Override
    public void loadData(String type, int page) {
        mRepository.getAll(type, page)
                .subscribe(new Subscriber<List<AllBean.ResultsBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<AllBean.ResultsBean> list) {
                        mView.showData(list);
                    }
                });
    }

    @Override
    public void saveData(AllBean.ResultsBean resultsBean) {
        MarkBean markBean = new MarkBean();
        markBean.setId(resultsBean.getId());
        markBean.setDesc(resultsBean.getDesc());
        markBean.setPublishedAt(resultsBean.getPublishedAt());
        markBean.setType(resultsBean.getType());
        markBean.setWho(resultsBean.getWho());
        markBean.setUrl(resultsBean.getUrl());
        mRepository.insertMark(markBean);
    }

    @Override
    public void deleteDataById(String id) {
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

    @Override
    public void share(Context context, String url, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, String.format(context.getString(R.string.share_article), title, url));
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_gank)));
    }

}
