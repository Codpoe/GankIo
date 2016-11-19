package me.codpoe.gankio.main.gank.meizhi;

import dagger.Component;

/**
 * Created by Codpoe on 2016/11/10.
 */

@Component(modules = MeizhiModule.class)
public interface MeizhiComponent {

    void inject(MeizhiFrag meizhiFrag);

}
