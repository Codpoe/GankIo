package me.codpoe.gankio.main.mark;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Codpoe on 2016/11/10.
 */

@Module
public class MarkModule {

    private final MarkContract.View mView;

    public MarkModule(MarkContract.View view) {
        mView = view;
    }

    @Provides
    MarkContract.View provideMarkContractView() {
        return mView;
    }

}
