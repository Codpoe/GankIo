package me.codpoe.gankio.main.gank;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Codpoe on 2016/11/10.
 */

@Module
public class GankModule {

    private final GankContract.View mView;

    public GankModule(GankContract.View view) {
        mView = view;
    }

    @Provides
    GankContract.View provideGankContractView() {
        return mView;
    }

}
