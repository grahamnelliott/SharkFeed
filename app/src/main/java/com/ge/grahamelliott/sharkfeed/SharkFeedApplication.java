package com.ge.grahamelliott.sharkfeed;

import android.app.Application;

import com.ge.grahamelliott.sharkfeed.common.di.ApiModule;
import com.ge.grahamelliott.sharkfeed.common.di.AppComponent;
import com.ge.grahamelliott.sharkfeed.common.di.DaggerAppComponent;
import com.ge.grahamelliott.sharkfeed.common.di.MainModule;

/**
 * @author graham.elliott
 */

public class SharkFeedApplication extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setUpDi();
    }

    private void setUpDi() {
        appComponent = DaggerAppComponent.builder()
                                         .mainModule(new MainModule(this))
                                         .apiModule(new ApiModule())
                                         .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
