package com.ge.grahamelliott.sharkfeed.common.network;

import com.ge.grahamelliott.sharkfeed.common.models.PhotoDetail;
import com.google.gson.annotations.SerializedName;

/**
 * Photo Detail object for Retrofit to parse JSON response to.
 *
 * @author graham.elliott
 */
public class PhotoDetailResponse {
    private PhotoDetail photo;

    @SerializedName("stat")
    private String status;

    public PhotoDetail getPhotoDetails() {
        return photo;
    }
}
