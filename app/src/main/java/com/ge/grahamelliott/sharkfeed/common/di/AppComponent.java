package com.ge.grahamelliott.sharkfeed.common.di;

import com.ge.grahamelliott.sharkfeed.common.activities.MainActivity;
import com.ge.grahamelliott.sharkfeed.photodetail.PhotoDetailFragment;
import com.ge.grahamelliott.sharkfeed.photolist.PhotoListFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author graham.elliott
 */
@Singleton
@Component(modules = {MainModule.class, ApiModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(PhotoListFragment fragment);

    void inject(PhotoDetailFragment fragment);
}
