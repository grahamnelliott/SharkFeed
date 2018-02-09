package com.ge.grahamelliott.sharkfeed.common.di;

import android.support.annotation.NonNull;

import com.ge.grahamelliott.sharkfeed.common.network.FlickrApi;
import com.ge.grahamelliott.sharkfeed.common.network.FlickrApiConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * DI module for API related classes.
 *
 * @author graham.elliott
 */
@Module
public class ApiModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url();

                HttpUrl qUrl = url.newBuilder()
                                    .addQueryParameter("api_key", FlickrApiConstants.API_KEY)
                                    .addQueryParameter("format", FlickrApiConstants.JSON_FORMAT)
                                    .addQueryParameter("nojsoncallback", FlickrApiConstants.JSON_CALLBACK)
                                    .build();
                Request.Builder qRequest = request.newBuilder()
                                                    .url(qUrl);

                return chain.proceed(qRequest.build());
            }
        }).build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder().client(client).baseUrl(FlickrApiConstants.BASE_URL)
                                     .addConverterFactory(GsonConverterFactory
                                                                  .create(gson))
                                     .build();
    }

    @Provides
    @Singleton
    FlickrApi provideFlickrApi(Retrofit retrofit) {
        return retrofit.create(FlickrApi.class);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().setLenient()
                                .create();
    }
}
