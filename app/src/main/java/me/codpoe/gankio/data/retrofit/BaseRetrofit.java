package me.codpoe.gankio.data.retrofit;

import java.util.concurrent.TimeUnit;

import me.codpoe.gankio.data.Api;
import me.codpoe.gankio.data.bean.AllBean;
import me.codpoe.gankio.data.bean.DayBean;
import me.codpoe.gankio.data.bean.SearchBean;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Codpoe on 2016/10/9.
 */

public class BaseRetrofit {

    private static final String BASE_URL = "http://gank.io/api/";

    private static BaseRetrofit sRetrofit;
    private Api mApi;
    private Retrofit mRetrofit;

    private BaseRetrofit() {

        // set up OkHttpClient
        OkHttpClient client;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        client = builder.build();

        // set up Retrofit of all
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApi = mRetrofit.create(Api.class);

    }

    public synchronized static BaseRetrofit getInstance() {
        if (sRetrofit == null) {
            sRetrofit = new BaseRetrofit();
        }
        return sRetrofit;
    }

    /**
     * get typed data from net
     * @param type
     * @param page
     * @return
     */
    public Observable<AllBean> getAllFromNet(String type, int page) {
        return mApi.fetchAll(type, page)
                .subscribeOn(Schedulers.io());
    }

    /**
     * get search result from net
     * @param query
     * @param type
     * @param page
     * @return
     */
    public Observable<SearchBean> getSearchFromNet(String query, String type, int page) {
        return mApi.fetchSearch(query, type, page)
                .subscribeOn(Schedulers.io());
    }

    /**
     * get data using specific date
     * @param day
     * @return
     */
    public Observable<DayBean> getDayFromNet(String day) {
        return mApi.fetchDay(day)
                .subscribeOn(Schedulers.io());
    }

}
