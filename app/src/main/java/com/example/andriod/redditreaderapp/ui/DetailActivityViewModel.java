package com.example.andriod.redditreaderapp.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.andriod.redditreaderapp.model.Detail;
import com.example.andriod.redditreaderapp.remote.RemoteDataSource;

import java.util.List;

public class DetailActivityViewModel extends ViewModel {
    public  String mpostThumbnailURL;
    public  String mpostTitle;
    public  String mpostAuthor;
    public  String mpostUpdated;
    public  String mpostID;

    RemoteDataSource remoteDataSource = new RemoteDataSource();
    MutableLiveData<List<Detail>> mcomments = new MutableLiveData<>();

    public void setViewData(String postThumbnailURL, String postTitle, String postAuthor, String postUpdated, String postID){
        mpostThumbnailURL = postThumbnailURL;
        mpostTitle = postTitle;
        mpostAuthor = postAuthor;
        mpostUpdated = postUpdated;
        mpostID = postID;
    }

    public void  getComments(String commentUrl){
        mcomments.setValue(remoteDataSource.getComments(commentUrl));
    }
}
