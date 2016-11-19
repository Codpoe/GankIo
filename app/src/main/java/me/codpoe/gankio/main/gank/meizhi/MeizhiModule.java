package me.codpoe.gankio.main.gank.meizhi;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Codpoe on 2016/11/10.
 */

@Module
public class MeizhiModule {

    private final MeizhiContract.View mView;

    public MeizhiModule(MeizhiContract.View view) {
        mView = view;
    }

    @Provides
    MeizhiContract.View provideMeizhiContractView() {
        return mView;
    }

}
