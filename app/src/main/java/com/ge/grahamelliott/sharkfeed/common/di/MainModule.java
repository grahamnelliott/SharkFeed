package com.ge.grahamelliott.sharkfeed.common.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author graham.elliott
 */
@Module
public class MainModule {

    private Context context;

    public MainModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return context;
    }
}
