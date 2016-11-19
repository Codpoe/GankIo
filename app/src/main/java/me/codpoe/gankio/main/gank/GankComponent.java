package me.codpoe.gankio.main.gank;

import dagger.Component;
import me.codpoe.gankio.main.gank.fragment.AndroidFrag;
import me.codpoe.gankio.main.gank.fragment.IOSFrag;
import me.codpoe.gankio.main.gank.fragment.WebFrag;

/**
 * Created by Codpoe on 2016/11/10.
 */

@Component(modules = GankModule.class)
public interface GankComponent {

    void inject(IOSFrag iosFrag);
    void inject(AndroidFrag androidFrag);
    void inject(WebFrag webFrag);

}
