package me.codpoe.gankio.main.mark;

import javax.inject.Inject;

import io.realm.RealmResults;
import me.codpoe.gankio.data.Repository;
import me.codpoe.gankio.data.bean.MarkBean;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Codpoe on 2016/10/10.
 */

public class MarkPresenter implements MarkContract.Presenter {

    private MarkContract.View mView;
    private Repository mRepository;
    private boolean mAutoRefresh = true;
    private Subscription mSubscription = null;
    private CompositeSubscription mCompositeSubscription;

    @Inject
    public MarkPresenter(MarkContract.View view) {
        mView = view;
        mRepository = Repository.getInstance();
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void loadDataByType(final String type) {
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
        }
        mSubscription = mRepository.queryMarkByType(type).asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RealmResults<MarkBean>>() {
                    @Override

                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RealmResults<MarkBean> markBeen) {
                        if (mAutoRefresh) {
                            mView.showData(markBeen);
                        }
                        mAutoRefresh = true;
                    }
                });
        mCompositeSubscription.add(mSubscription);
    }

    @Override
    public void deleteDataById(String id) {
        mAutoRefresh = false;
        mRepository.deleteMarkById(id);
    }



}
