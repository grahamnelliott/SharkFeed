package com.ge.grahamelliott.sharkfeed.common.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Flickr endpoints for Retrofit.
 *
 * @author graham.elliott
 */
public interface FlickrApi {

    @GET("/services/rest/?method=flickr.photos.search")
    Call<SearchPhotosResponse> searchImagesOfText(
            @Query("text") String text,
            @QueryMap Map<String, String> options
    );

    @GET("/services/rest/?method=flickr.photos.getInfo")
    Call<PhotoDetailResponse> getPhotoDetailsOfId(
            @Query("photo_id") String id);
}
