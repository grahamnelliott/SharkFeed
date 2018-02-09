package com.ge.grahamelliott.sharkfeed.photodetail;

import android.net.Uri;

/**
 * @author graham.elliott
 */
public interface PhotoDetailView {

    void loadImageFromUri(Uri thumbnailUri, Uri fullSizeUri);

    void saveImageFromUri(Uri uri);

    void showImageSavedToast();

    void showImageSaveFailureToast();

    void showPhotoDetailLoadFailureToast();

    void updateImageAttributes(String title, String views);

    void launchWebpage(Uri uri);
}
