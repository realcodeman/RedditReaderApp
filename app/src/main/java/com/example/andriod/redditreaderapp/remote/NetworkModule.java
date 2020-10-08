package com.example.andriod.redditreaderapp.remote;

import com.example.andriod.redditreaderapp.utils.Commons;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class NetworkModule {
    private static Retrofit retrofit = null;

    public static OkHttpClient providesClient(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static Retrofit provideApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Commons.BASEURL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .client(providesClient())
                    .build();
        }
        return retrofit;
    }
}

