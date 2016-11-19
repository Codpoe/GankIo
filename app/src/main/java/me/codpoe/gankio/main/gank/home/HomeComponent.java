package me.codpoe.gankio.main.gank.home;

import dagger.Component;

/**
 * Created by Codpoe on 2016/11/13.
 */

@Component(modules = HomeModule.class)
public interface HomeComponent {

    void inject(HomeFrag homeFrag);

}
