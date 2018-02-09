package com.ge.grahamelliott.sharkfeed.common.network;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author graham.elliott
 */
@RunWith(RobolectricTestRunner.class)
public class FlickrServiceTest {

    @Mock
    FlickrApi mockApi;

    @Mock
    Callback<SearchPhotosResponse> mockSearchPhotosCallback;

    @Mock
    Call<SearchPhotosResponse> mockCall;

    private FlickrService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new FlickrService(mockApi);
    }

    @Test
    public void testRequestImagesWithValidParams() {
        when(mockApi.searchImagesOfText(anyString(), ArgumentMatchers.<String, String>anyMap())).thenReturn(mockCall);
        service.requestImagesWithText("dog", 1, 100, mockSearchPhotosCallback);

        verify(mockApi).searchImagesOfText(eq("dog"), any(Map.class));
        verify(mockCall).enqueue(eq(mockSearchPhotosCallback));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestImagesWithInvalidParams() {
        service.requestImagesWithText("dog", 0, 100, mockSearchPhotosCallback);
    }
}
