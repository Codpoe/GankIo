package me.codpoe.gankio.main.gank.home;

import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import me.codpoe.gankio.R;
import me.codpoe.gankio.data.Repository;
import me.codpoe.gankio.data.bean.AllBean;
import me.codpoe.gankio.data.bean.DayBean;
import me.codpoe.gankio.data.bean.MarkBean;
import me.codpoe.gankio.main.gank.item.GankHeadItem;
import me.codpoe.gankio.main.gank.item.GankImgSubItem;
import me.codpoe.gankio.main.gank.item.GankSubItem;
import me.drakeet.multitype.Item;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Codpoe on 2016/11/13.
 */

public class HomePresenter implements HomeContract.Presenter {

    private static final String NO_CONTENT = "no content";

    private HomeContract.View mView;
    private Repository mRepository;

    @Inject
    public HomePresenter(HomeContract.View view) {
        mView = view;
        mRepository = Repository.getInstance();
    }

    @Override
    public void loadData(String day) {
        mRepository.getDayData(day)
                .flatMap(new Func1<DayBean.ResultsBean, Observable<List<DayBean.ResultsBean.GankBean>>>() {
                    @Override
                    public Observable<List<DayBean.ResultsBean.GankBean>> call(DayBean.ResultsBean resultsBean) {
                        mView.deleteAllItem();
                        return Observable.just(
                                resultsBean.getIOS(),
                                resultsBean.getAndroid(),
                                resultsBean.getWeb(),
                                resultsBean.getExResource(),
                                resultsBean.getRecommendation(),
                                resultsBean.getApp()
                        );
                    }
                })
                .filter(new Func1<List<DayBean.ResultsBean.GankBean>, Boolean>() {
                    @Override
                    public Boolean call(List<DayBean.ResultsBean.GankBean> gankBeen) {
                        return gankBeen != null;
                    }
                })
                .subscribe(new Subscriber<List<DayBean.ResultsBean.GankBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.complete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && e.getMessage().equals(NO_CONTENT)) {
                            loadData(loadDate(true));
                        }
                    }

                    @Override
                    public void onNext(List<DayBean.ResultsBean.GankBean> gankBeen) {
                        mView.showItem(new GankHeadItem(gankBeen.get(0).getType()));
                        mView.showAllItem(flattenData(gankBeen));
                    }
                });
    }

    @Override
    public void loadDataByType(String type, int page) {
        mRepository.getAll(type, page)
                .subscribe(new Subscriber<List<AllBean.ResultsBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<AllBean.ResultsBean> resultsBeen) {
                        mView.showSheetData(resultsBeen);
                    }
                });
    }

    @Override
    public String loadDate(boolean isNull) {

        Calendar calendar = Calendar.getInstance();

        if (!isNull) {
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SUNDAY:
                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 2);
                    break;
                case Calendar.SATURDAY:
                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
                    break;
                default:
                    break;
            }
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 3);
        } else {
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        }

        return new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime());
    }

    @Override
    public List<Item> flattenData(List<DayBean.ResultsBean.GankBean> postList) {
        List<Item> itemList = new ArrayList<>();
        for (DayBean.ResultsBean.GankBean post : postList) {
            if (post.getImages() != null) {
                itemList.add(new GankImgSubItem(post.getId(), post.getDesc(), post.getWho(), post.getType(), post.getPublishedAt(), post.getUrl(), post.getImages().get(0)));
            } else {
                itemList.add(new GankSubItem(post.getId(), post.getDesc(), post.getWho(), post.getType(), post.getPublishedAt(), post.getUrl()));
            }
        }
        return itemList;
    }

    @Override
    public void saveData(GankSubItem item) {
        MarkBean markBean = new MarkBean();
        markBean.setId(item.mId);
        markBean.setDesc(item.mTitle);
        markBean.setPublishedAt(item.mPublishAt);
        markBean.setType(item.mType);
        markBean.setWho(item.mWho);
        markBean.setUrl(item.mUrl);
        mRepository.insertMark(markBean);
    }

    @Override
    public void saveSheetData(AllBean.ResultsBean resultsBean) {
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
