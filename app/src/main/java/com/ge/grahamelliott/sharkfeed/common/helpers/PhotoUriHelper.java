package com.ge.grahamelliott.sharkfeed.common.helpers;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ge.grahamelliott.sharkfeed.common.models.Photo;

/**
 * @author graham.elliott
 */
public class PhotoUriHelper {

    @Nullable
    public static Uri getSmallestPhotoUri(@NonNull Photo photo) {
        if (photo.getSmallUrl() != null) {
            return Uri.parse(photo.getSmallUrl());
        } else if (photo.getMediumUrl() != null) {
            return Uri.parse(photo.getMediumUrl());
        } else if (photo.getLargeUrl() != null) {
            return Uri.parse(photo.getLargeUrl());
        } else if (photo.getOriginalUrl() != null) {
            return Uri.parse(photo.getOriginalUrl());
        }
        return null;
    }

    @Nullable
    public static Uri getLargestPhotoUri(@NonNull Photo photo) {
        if (photo.getOriginalUrl() != null) {
            return Uri.parse(photo.getOriginalUrl());
        } else if (photo.getLargeUrl() != null) {
            return Uri.parse(photo.getLargeUrl());
        } else if (photo.getMediumUrl() != null) {
            return Uri.parse(photo.getMediumUrl());
        } else if (photo.getSmallUrl() != null) {
            return Uri.parse(photo.getSmallUrl());
        }
        return null;
    }

}
