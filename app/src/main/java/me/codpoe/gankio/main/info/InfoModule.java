package me.codpoe.gankio.main.info;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Codpoe on 2016/11/10.
 */

@Module
public class InfoModule {

    private final InfoContract.View mView;

    public InfoModule(InfoContract.View view) {
        mView = view;
    }

    @Provides
    InfoContract.View provideInfoContractView() {
        return mView;
    }

}
