package com.ge.grahamelliott.sharkfeed.photolist;

import android.net.Uri;

import com.ge.grahamelliott.sharkfeed.common.data.PhotosDataSource;
import com.ge.grahamelliott.sharkfeed.common.models.Photo;
import com.ge.grahamelliott.sharkfeed.photolist.views.PhotoItemView;
import com.ge.grahamelliott.sharkfeed.photolist.views.PhotoListView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.lang.ref.WeakReference;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author graham.elliott
 */
@RunWith(RobolectricTestRunner.class)
public class PhotoListPresenterTest {

    private PhotoListPresenter presenter;

    @Mock
    PhotosDataSource mockPhotoDataSource;

    @Mock
    PhotoListView mockView;

    @Mock
    Photo mockPhoto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mockPhoto.getSmallUrl()).thenReturn("url");
        presenter = new PhotoListPresenter(mockPhotoDataSource);
        presenter.bindView(mockView);
    }

    @Test
    public void testPhotosUpdatedOnStart() {
        verify(mockPhotoDataSource).fetchPhotos(eq("san jose sharks"), any(WeakReference.class));
    }

    @Test
    public void testBindPhotoAtPosition() {
        PhotoItemView mockItemView = mock(PhotoItemView.class);
        when(mockPhotoDataSource.getPhotoAtPosition(1)).thenReturn(mockPhoto);

        presenter.onBindPhotoViewAtPosition(mockItemView, 1);

        verify(mockPhotoDataSource).getPhotoAtPosition(eq(1));
        verify(mockItemView).setImageFromUri(any(Uri.class));
    }

    @Test
    public void testBindPhotoAtPositionWithNullUri() {
        PhotoItemView mockItemView = mock(PhotoItemView.class);
        when(mockPhotoDataSource.getPhotoAtPosition(1)).thenReturn(mock(Photo.class));

        presenter.onBindPhotoViewAtPosition(mockItemView, 1);

        verify(mockPhotoDataSource).getPhotoAtPosition(eq(1));
        verify(mockItemView, never()).setImageFromUri(any(Uri.class));
    }

    @Test
    public void testPhotosLoadedWithNewPhotos() {
        presenter.onPhotosLoaded(2, 6);

        verify(mockView).notifyRefreshComplete();
        verify(mockView).notifyNewPhotosAdded(eq(2), eq(6));
    }

    @Test
    public void testPhotosLoadedWithRefresh() {
        presenter.onPhotosLoaded(2, 2);

        verify(mockView).notifyRefreshComplete();
        verify(mockView).notifyAllPhotosRefreshed();
    }

    @Test
    public void testPhotosLoadedNone() {
        presenter.onPhotosLoaded(0, 0);

        verify(mockView).notifyRefreshComplete();
        verify(mockView, never()).notifyAllPhotosRefreshed();
    }

    @Test
    public void testPhotosLoadedFailure() {
        presenter.onPhotoLoadFailure();

        verify(mockView).notifyRefreshComplete();
        verify(mockView).showFailedLoadToast();
    }
}
