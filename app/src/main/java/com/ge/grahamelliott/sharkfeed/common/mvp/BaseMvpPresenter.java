package com.ge.grahamelliott.sharkfeed.common.mvp;

/**
 * Base class for MVP presenter
 *
 * @author graham.elliott
 */
public abstract class BaseMvpPresenter<T> {

    protected T view;

    public BaseMvpPresenter() {
    }

    public void bindView(T view) {
        this.view = view;
    }
}
