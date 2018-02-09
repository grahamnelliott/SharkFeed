package com.ge.grahamelliott.sharkfeed.photodetail;

import android.graphics.Bitmap;
import android.net.Uri;

import com.ge.grahamelliott.sharkfeed.common.data.FileCachingManager;
import com.ge.grahamelliott.sharkfeed.common.data.PhotosDataSource;
import com.ge.grahamelliott.sharkfeed.common.helpers.PhotoUriHelper;
import com.ge.grahamelliott.sharkfeed.common.models.Photo;
import com.ge.grahamelliott.sharkfeed.common.models.PhotoDetail;
import com.ge.grahamelliott.sharkfeed.common.mvp.BaseMvpPresenter;

import java.lang.ref.WeakReference;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author graham.elliott
 */
@Singleton
public class PhotoDetailPresenter extends BaseMvpPresenter<PhotoDetailView> implements
                                                                            FileCachingManager.SaveFileListener,
                                                                            PhotosDataSource.PhotoDetailListener {
    private Photo photo;

    private PhotoDetail details;

    private PhotosDataSource photosDataSource;

    private FileCachingManager fileCachingManager;

    @Inject
    public PhotoDetailPresenter(PhotosDataSource photosDataSource, FileCachingManager fileCachingManager) {
        this.photosDataSource = photosDataSource;
        this.fileCachingManager = fileCachingManager;
    }

    void bindView(PhotoDetailView view, int photoPosition) {
        super.bindView(view);
        this.photo = photosDataSource.getPhotoAtPosition(photoPosition);
        if (photo != null) {
            updatePhoto();
        }
    }

    private void updatePhoto() {
        photosDataSource.fetchPhotoDetails(photo.getId(),
                                           new WeakReference<PhotosDataSource.PhotoDetailListener>(this));

        Uri fullSizeUri = PhotoUriHelper.getLargestPhotoUri(photo);
        Uri thumbnailUri = PhotoUriHelper.getSmallestPhotoUri(photo);

        if (fullSizeUri != null) {
            view.loadImageFromUri(thumbnailUri, fullSizeUri);
        }
        view.updateImageAttributes(photo.getTitle(), null);
    }

    void downloadImageToDevice() {
        Uri fullSizeUri = PhotoUriHelper.getLargestPhotoUri(photo);
        if (fullSizeUri != null) {
            view.saveImageFromUri(fullSizeUri);
        }
    }

    void saveBitmapToDevice(Bitmap bitmap) {
            fileCachingManager.saveBitmapToExternalStorage(photo.getId(),
                                                           bitmap,
                                                           new WeakReference<FileCachingManager.SaveFileListener>
                                                                   (this));
    }

    void openImageInWeb() {
        if (details == null || details.getPhotoUrl() == null) {
            return;
        }
        view.launchWebpage(Uri.parse(details.getPhotoUrl()));
    }

    @Override
    public void onFileSaved() {
        view.showImageSavedToast();
    }

    @Override
    public void onPhotoDetailsLoaded() {
        details = photosDataSource.getPhotoDetailsWithId(photo.getId());
        if (details != null) {
            view.updateImageAttributes(details.getTitle(), details.getViews());
        }
    }

    @Override
    public void onPhotoDetailLoadFailure() {
        view.showPhotoDetailLoadFailureToast();
    }

    @Override
    public void onImageSaveFailure() {
        view.showImageSaveFailureToast();
    }
}
