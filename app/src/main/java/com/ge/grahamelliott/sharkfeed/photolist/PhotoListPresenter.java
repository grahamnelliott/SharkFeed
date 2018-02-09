package com.ge.grahamelliott.sharkfeed.photolist;

import com.ge.grahamelliott.sharkfeed.common.data.PhotosDataSource;
import com.ge.grahamelliott.sharkfeed.common.helpers.PhotoUriHelper;
import com.ge.grahamelliott.sharkfeed.common.models.Photo;
import com.ge.grahamelliott.sharkfeed.common.mvp.BaseMvpPresenter;
import com.ge.grahamelliott.sharkfeed.photolist.views.PhotoItemView;
import com.ge.grahamelliott.sharkfeed.photolist.views.PhotoListView;

import java.lang.ref.WeakReference;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author graham.elliott
 */
@Singleton
public class PhotoListPresenter extends BaseMvpPresenter<PhotoListView> implements PhotosDataSource.PhotoDataListener {

    private PhotosDataSource photosDataSource;

    private static final String SHARK_TEXT = "san jose sharks";

    @Inject
    public PhotoListPresenter(PhotosDataSource photosDataSource) {
        this.photosDataSource = photosDataSource;
        updatePhotos();
    }

    public void updatePhotos() {
        photosDataSource.fetchPhotos(SHARK_TEXT,
                                     new WeakReference<PhotosDataSource.PhotoDataListener>(this));
    }

    public void onBindPhotoViewAtPosition(PhotoItemView itemView, int position) {
        Photo photo = photosDataSource.getPhotoAtPosition(position);
        if (photo == null) {
            return;
        }

        itemView.setImageFromUri(PhotoUriHelper.getSmallestPhotoUri(photo));
    }

    public int getPhotoViewCount() {
        return photosDataSource.getNumberOfPhotos();
    }

    public void onScrolled(int firstItem, int visibleItems, int totalItems) {
        if (visibleItems + firstItem >= totalItems && firstItem >= 0 && totalItems >= photosDataSource.getPageSize()) {
            updatePhotos();
        }
    }

    public void onSwipeToRefresh() {
        photosDataSource.refreshPhotos(SHARK_TEXT,
                                       new WeakReference<PhotosDataSource.PhotoDataListener>(this));
    }

    public void onPhotoClicked(int position) {
        view.launchPhotoDetailFragment(position);
    }

    @Override
    public void onPhotosLoaded(int newCount, int totalCount) {
        view.notifyRefreshComplete();
        if (newCount == totalCount) {
            view.notifyAllPhotosRefreshed();
        } else {
            view.notifyNewPhotosAdded(newCount, totalCount);
        }
    }

    @Override
    public void onFailure() {
        view.notifyRefreshComplete();
        view.showFailedLoadToast();
    }
}
