package com.ge.grahamelliott.sharkfeed.common.network;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author graham.elliott
 */
@Singleton
public class FlickrService {

    private FlickrApi flickrApi;

    @Inject
    public FlickrService(FlickrApi flickrApi) {
        this.flickrApi = flickrApi;
    }

    public void requestImagesWithText(String text, int page,
                                      int pageSize, Callback<SearchPhotosResponse> callback) {
        if (page < 1) {
            throw new IllegalArgumentException("Can't search for <1 pages of data");
        }

        Map<String, String> optionsMap = new HashMap<>();
        optionsMap.put("extras", "url_t,url_c,url_l,url_o");
        optionsMap.put("page", String.valueOf(page));
        optionsMap.put("per_page", String.valueOf(pageSize));
        optionsMap.put("safe_search", FlickrApiConstants.SAFE_SEARCH);

        Call<SearchPhotosResponse> call = flickrApi.searchImagesOfText(text, optionsMap);
        call.enqueue(callback);
    }

    public void requestPhotoDetailsWithId(String id, Callback<PhotoDetailResponse> callback) {
        Call<PhotoDetailResponse> call = flickrApi.getPhotoDetailsOfId(id);
        call.enqueue(callback);
    }
}
