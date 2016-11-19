package me.codpoe.gankio.picture;

import dagger.Component;

/**
 * Created by Codpoe on 2016/11/10.
 */

@Component(modules = PictureModule.class)
public interface PictureComponent {

    void inject(PictureActivity pictureActivity);

}
