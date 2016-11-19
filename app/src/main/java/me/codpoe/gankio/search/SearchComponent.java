package me.codpoe.gankio.search;

import dagger.Component;

/**
 * Created by Codpoe on 2016/11/10.
 */

@Component(modules = SearchModule.class)
public interface SearchComponent {

    void inject(SearchActivity searchActivity);

}
