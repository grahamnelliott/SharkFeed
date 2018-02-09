package com.ge.grahamelliott.sharkfeed.common.helpers;

import android.net.Uri;

import com.ge.grahamelliott.sharkfeed.common.models.Photo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

/**
 * @author graham.elliott
 */
@RunWith(RobolectricTestRunner.class)
public class PhotoUriHelperTest {

    @Mock
    Photo mockPhoto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSingleValidUrl() {
        when(mockPhoto.getSmallUrl()).thenReturn("url");

        assertThat(PhotoUriHelper.getSmallestPhotoUri(mockPhoto), is(Uri.parse("url")));

        // should still return smallest because it is only valid url
        assertThat(PhotoUriHelper.getLargestPhotoUri(mockPhoto), is(Uri.parse("url")));
    }

    @Test
    public void testThreeValidUrls() {
        when(mockPhoto.getSmallUrl()).thenReturn("s");
        when(mockPhoto.getMediumUrl()).thenReturn("m");
        when(mockPhoto.getLargeUrl()).thenReturn("l");

        assertThat(PhotoUriHelper.getSmallestPhotoUri(mockPhoto), is(Uri.parse("s")));
        assertThat(PhotoUriHelper.getLargestPhotoUri(mockPhoto), is(Uri.parse("l")));
    }

    @Test
    public void testNoValidUrls() {
        assertThat(PhotoUriHelper.getSmallestPhotoUri(mockPhoto), nullValue());
        assertThat(PhotoUriHelper.getLargestPhotoUri(mockPhoto), nullValue());
    }
}
