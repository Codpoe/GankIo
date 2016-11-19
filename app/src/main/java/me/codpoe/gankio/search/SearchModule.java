package me.codpoe.gankio.search;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Codpoe on 2016/11/10.
 */

@Module
public class SearchModule {

    private final SearchContract.View mView;

    public SearchModule(SearchContract.View view) {
        mView = view;
    }

    @Provides
    SearchContract.View provideSearchContractView() {
        return mView;
    }

}
