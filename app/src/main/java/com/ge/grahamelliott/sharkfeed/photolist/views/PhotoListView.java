package com.ge.grahamelliott.sharkfeed.photolist.views;

/**
 * @author graham.elliott
 */

public interface PhotoListView {
    void notifyNewPhotosAdded(int newElementCount, int totalElementCount);

    void notifyRefreshComplete();

    void notifyAllPhotosRefreshed();

    void launchPhotoDetailFragment(int position);

    void showFailedLoadToast();
}
