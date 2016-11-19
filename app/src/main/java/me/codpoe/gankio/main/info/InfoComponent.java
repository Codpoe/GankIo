package me.codpoe.gankio.main.info;

import dagger.Component;

/**
 * Created by Codpoe on 2016/11/10.
 */

@Component(modules = InfoModule.class)
public interface InfoComponent {

    void inject(InfoFrag infoFrag);

}
