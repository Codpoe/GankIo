package me.codpoe.gankio.main.gank.meizhi;

import java.util.List;

import javax.inject.Inject;

import me.codpoe.gankio.data.Repository;
import me.codpoe.gankio.data.bean.AllBean;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Codpoe on 2016/10/24.
 */

public class MeizhiPresenter implements MeizhiContract.Presenter {

    private MeizhiContract.View mView;
    private Repository mRepository;

    @Inject
    public MeizhiPresenter(MeizhiContract.View view) {
        mView = view;
        mRepository = Repository.getInstance();
    }

    @Override
    public void loadMeizhi(int page) {
        mRepository.getAll("福利", page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<AllBean.ResultsBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMsg(e.getMessage());
                    }

                    @Override
                    public void onNext(List<AllBean.ResultsBean> resultsBeen) {
                        mView.showMeizhi(resultsBeen);
                    }
                });
    }
}
