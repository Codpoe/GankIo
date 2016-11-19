package me.codpoe.gankio.main.mark;

import dagger.Component;

/**
 * Created by Codpoe on 2016/11/10.
 */

@Component(modules = MarkModule.class)
public interface MarkComponent {

    void inject(MarkFrag markFrag);

}
