package com.ge.grahamelliott.sharkfeed.common.data;

import com.ge.grahamelliott.sharkfeed.common.models.Photo;
import com.ge.grahamelliott.sharkfeed.common.network.FlickrService;
import com.ge.grahamelliott.sharkfeed.common.network.SearchPhotosResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author graham.elliott
 */
@RunWith(RobolectricTestRunner.class)
public class PhotosDataSourceTest {

    private PhotosDataSource dataSource;

    @Mock
    FlickrService flickrService;

    @Mock
    WeakReference<PhotosDataSource.PhotoDataListener> mockWeakReference;

    @Mock
    PhotosDataSource.PhotoDataListener mockPhotoDataListener;

    @Mock
    Call<SearchPhotosResponse> mockSearchCall;

    @Mock
    SearchPhotosResponse mockPhotos;

    @Captor
    ArgumentCaptor<Callback<SearchPhotosResponse>> photosResponseCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mockWeakReference.get()).thenReturn(mockPhotoDataListener);
        dataSource = new PhotosDataSource(flickrService);
    }

    @Test
    public void testGetPhotoAtInvalidPosition() {
        assertThat(dataSource.getPhotoAtPosition(1), is(nullValue()));
    }

    @Test
    public void testRefreshPhotosSuccess() {
        dataSource.refreshPhotos("dog", mockWeakReference);
        verify(flickrService).requestImagesWithText(eq("dog"), eq(1), eq(100),
                                                    photosResponseCaptor.capture());

        Response<SearchPhotosResponse> mockSuccessResponse = Response.success(mockPhotos);
        when(mockPhotos.getPhotos()).thenReturn(mock(SearchPhotosResponse.PageDetails.class));

        photosResponseCaptor.getValue().onResponse(mockSearchCall, mockSuccessResponse);

        verify(mockPhotoDataListener).onPhotosLoaded(eq(0), eq(0));
    }

    @Test
    public void testRefreshPhotosFailureNoBody() {
        dataSource.refreshPhotos("dog", mockWeakReference);
        verify(flickrService).requestImagesWithText(eq("dog"), eq(1), eq(100),
                                                    photosResponseCaptor.capture());

        Response<SearchPhotosResponse> mockFailureResponse = Response.success(mockPhotos);

        // empty photos

        photosResponseCaptor.getValue().onResponse(mockSearchCall, mockFailureResponse);

        verify(mockPhotoDataListener).onPhotoLoadFailure();
    }

    @Test
    public void testRefreshPhotosFailure() {
        dataSource.refreshPhotos("dog", mockWeakReference);
        verify(flickrService).requestImagesWithText(eq("dog"), eq(1), eq(100),
                                                    photosResponseCaptor.capture());

        Response<SearchPhotosResponse> mockFailureResponse = Response.error(404, ResponseBody.create(
                MediaType.parse("application/json"), "{\"key\":[\"somestuff\"]}"
        ));

        photosResponseCaptor.getValue().onResponse(mockSearchCall, mockFailureResponse);

        verify(mockPhotoDataListener).onPhotoLoadFailure();
    }

    @Test
    public void testFetchPhotosSuccess() {
        dataSource.fetchPhotos("dog", mockWeakReference);
        verify(flickrService).requestImagesWithText(eq("dog"), eq(1), eq(100),
                                                    photosResponseCaptor.capture());

        Response<SearchPhotosResponse> mockSuccessResponse = Response.success(mockPhotos);
        SearchPhotosResponse.PageDetails mockDetails = mock(SearchPhotosResponse.PageDetails.class);
        List<Photo> photoList = new ArrayList<>();
        photoList.add(mock(Photo.class));
        photoList.add(mock(Photo.class));
        photoList.add(mock(Photo.class));
        when(mockDetails.getPhotoList()).thenReturn(photoList);
        when(mockPhotos.getPhotos()).thenReturn(mockDetails);

        photosResponseCaptor.getValue().onResponse(mockSearchCall, mockSuccessResponse);

        verify(mockPhotoDataListener).onPhotosLoaded(eq(3), eq(3));

        // now make sure list empties after refresh
        reset(flickrService);
        testRefreshPhotosSuccess();
    }


}
