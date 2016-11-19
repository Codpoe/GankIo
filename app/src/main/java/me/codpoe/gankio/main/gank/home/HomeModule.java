package me.codpoe.gankio.main.gank.home;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Codpoe on 2016/11/13.
 */

@Module
public class HomeModule {

    private final HomeContract.View mView;

    public HomeModule(HomeContract.View view) {
        mView = view;
    }

    @Provides
    HomeContract.View provideHomeContractView() {
        return mView;
    }

}
