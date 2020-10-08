package com.example.andriod.redditreaderapp.remote;

import com.example.andriod.redditreaderapp.model.Feed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface APIService {
    @GET("{feed_search}/.rss")
    Call<Feed> getFeed(@Path("feed_search") String feed_name);

}
