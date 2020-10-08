package com.example.andriod.redditreaderapp.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.andriod.redditreaderapp.model.Post;
import com.example.andriod.redditreaderapp.remote.RemoteDataSource;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    MutableLiveData<List<Post>> mposts = new MutableLiveData<>();

    RemoteDataSource remoteDataSource = new RemoteDataSource();


    public void getPosts(String  feedName){
        mposts.setValue(remoteDataSource.getFeeds(feedName));
    }
}
