package com.ge.grahamelliott.sharkfeed.common.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ge.grahamelliott.sharkfeed.common.models.Photo;
import com.ge.grahamelliott.sharkfeed.common.models.PhotoDetail;
import com.ge.grahamelliott.sharkfeed.common.network.FlickrApiConstants;
import com.ge.grahamelliott.sharkfeed.common.network.FlickrService;
import com.ge.grahamelliott.sharkfeed.common.network.PhotoDetailResponse;
import com.ge.grahamelliott.sharkfeed.common.network.SearchPhotosResponse;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author graham.elliott
 */
@Singleton
public class PhotosDataSource {

    private static final String TAG = PhotosDataSource.class.getName();

    private FlickrService flickrService;

    private List<Photo> photos;

    private Map<String, PhotoDetail> photoDetailMap;

    private int currentPage;

    // TODO: check thread
    private boolean isLoadingPhotos;

    @Inject
    public PhotosDataSource(FlickrService flickrService) {
        photos = new ArrayList<>();
        photoDetailMap = new HashMap<>();
        currentPage = 1;
        this.flickrService = flickrService;
    }

    @Nullable
    public Photo getPhotoAtPosition(int position) {
        if (position >= photos.size()) {
            Log.e(TAG, String.format("Can't retrieve photo at position %s because %s photos in list",
                                     position, photos.size()));
            return null;
        }
        return photos.get(position);
    }

    @Nullable
    public PhotoDetail getPhotoDetailsWithId(String id) {
        return photoDetailMap.get(id);
    }

    public int getNumberOfPhotos() {
        return photos.size();
    }

    public void fetchPhotos(String text, WeakReference<PhotoDataListener> listener) {
        requestPhotosWithText(text, false, listener);
    }

    public void refreshPhotos(String text, WeakReference<PhotoDataListener> listener) {
        currentPage = 1;
        requestPhotosWithText(text, true, listener);
    }

    public int getPageSize() {
        return FlickrApiConstants.PAGE_SIZE;
    }

    private void requestPhotosWithText(String text, final boolean clearCache,
                                       final WeakReference<PhotoDataListener> weakListener) {
        if (isLoadingPhotos) {
            return;
        }

        isLoadingPhotos = true;
        Log.d(TAG, String.format("Requesting: %s, Page: %s", text, currentPage));
        flickrService.requestImagesWithText(text, currentPage, FlickrApiConstants.PAGE_SIZE, new
                Callback<SearchPhotosResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchPhotosResponse> call,
                                   @NonNull Response<SearchPhotosResponse> response) {
                isLoadingPhotos = false;
                if (response.isSuccessful() && response.body() != null && response.body().getPhotos() != null) {
                    currentPage += 1;
                    List<Photo> newPhotos = response.body().getPhotos().getPhotoList();
                    if (clearCache) {
                        photos.clear();
                        photoDetailMap.clear();
                    }
                    photos.addAll(newPhotos);
                    int totalPhotoCount = photos.size();
                    int newPhotoCount = newPhotos.size();
                    if (weakListener.get() != null) {
                        weakListener.get().onPhotosLoaded(newPhotoCount, totalPhotoCount);
                    }
                } else {
                    Log.w(TAG, String.format("Failed to load images, error code: %s -> %s", response.code(), response
                            .message()));
                    if (weakListener.get() != null) {
                        weakListener.get().onFailure();
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchPhotosResponse> call, Throwable t) {
                isLoadingPhotos = false;
                Log.e(TAG, String.format("Failed to load images: %s", t.getLocalizedMessage()));
                if (weakListener.get() != null) {
                    weakListener.get().onFailure();
                }
            }
        });
    }

    public void fetchPhotoDetails(final String id, final WeakReference<PhotoDetailListener> weakListener) {
        flickrService.requestPhotoDetailsWithId(id, new Callback<PhotoDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<PhotoDetailResponse> call,
                                   @NonNull Response<PhotoDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    photoDetailMap.put(id, response.body().getPhotoDetails());
                    if (weakListener.get() != null) {
                        weakListener.get().onPhotoDetailsLoaded();
                    }
                } else {
                    Log.w(TAG, String.format("Failed to load image details, error code: %s -> %s", response.code(),
                                             response.message()));
                    if (weakListener.get() != null) {
                        weakListener.get().onPhotoDetailLoadFailure();
                    }
                }
            }

            @Override
            public void onFailure(Call<PhotoDetailResponse> call, Throwable t) {
                Log.e(TAG, String.format("Failed to load image details: %s", t.getLocalizedMessage()));
                if (weakListener.get() != null) {
                    weakListener.get().onPhotoDetailLoadFailure();
                }
            }
        });
    }

    public interface PhotoDetailListener {
        void onPhotoDetailsLoaded();

        void onPhotoDetailLoadFailure();
    }

    public interface PhotoDataListener {
        void onPhotosLoaded(int newCount, int totalCount);

        void onFailure();
    }
}
