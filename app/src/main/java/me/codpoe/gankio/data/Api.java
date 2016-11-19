package me.codpoe.gankio.data;

import me.codpoe.gankio.data.bean.AllBean;
import me.codpoe.gankio.data.bean.DayBean;
import me.codpoe.gankio.data.bean.SearchBean;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Codpoe on 2016/10/9.
 */

public interface Api {

    @GET("data/{type}/10/{page}")
    Observable<AllBean> fetchAll(@Path("type") String type, @Path("page") int page);

    @GET("search/query/{query}/category/{category}/count/10/page/{page}")
    Observable<SearchBean> fetchSearch(@Path("query") String query, @Path("category") String category, @Path("page") int page);

    @GET("day/{day}")
    Observable<DayBean> fetchDay(@Path("day") String day);

}
