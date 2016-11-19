package me.codpoe.gankio.picture;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Codpoe on 2016/11/10.
 */

@Module
public class PictureModule {

    private final PictureContract.View mView;

    public PictureModule(PictureContract.View view) {
        mView = view;
    }

    @Provides
    PictureContract.View providePictureContractView() {
        return mView;
    }

}
