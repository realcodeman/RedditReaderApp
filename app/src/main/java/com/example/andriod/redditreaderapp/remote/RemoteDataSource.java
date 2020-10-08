package com.example.andriod.redditreaderapp.remote;


import android.os.AsyncTask;


import com.example.andriod.redditreaderapp.model.Detail;
import com.example.andriod.redditreaderapp.model.Entry;
import com.example.andriod.redditreaderapp.model.Feed;
import com.example.andriod.redditreaderapp.model.Post;
import com.example.andriod.redditreaderapp.utils.Commons;
import com.example.andriod.redditreaderapp.utils.ParseXML;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteDataSource {

    private static APIService apiService;
    GetFeeds getFeeds;
    GetComments getComments;

    private static List<Post> posts = new ArrayList<>();
    private static List<Detail> mDetails = new ArrayList<>();

    public RemoteDataSource() {
        apiService = NetworkModule.provideApiService().create(APIService.class);
    }






    public List<Post> getFeeds(String feedName){
        getFeeds = new GetFeeds();
        getFeeds.execute(feedName);
        return posts;
    }

    public List<Detail> getComments(String commentUrl){

        String comment = null;
        try {
            String[] splitURL = commentUrl.split(Commons.BASEURL);
            comment = splitURL[1];
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        getComments = new GetComments();
        getComments.execute(comment);
        return mDetails;
    }

    public static class GetFeeds extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {
            apiService.getFeed(strings[0]).enqueue(new Callback<Feed>() {
                @Override
                public void onResponse(Call<Feed> call, Response<Feed> response) {
                    List<Entry> entrys = response.body().getEntrys();
                    for (int i = 0; i < entrys.size(); i++) {
                        ParseXML parseXMLContent = new ParseXML(entrys.get(i).getContent(), "<a href=");
                        List<String> postContent = parseXMLContent.start();


                        ParseXML image = new ParseXML(entrys.get(i).getContent(), "<img src=");

                        try {
                            postContent.add(image.start().get(0));
                        } catch (NullPointerException e) {
                            postContent.add(null);
                        } catch (IndexOutOfBoundsException e) {
                            postContent.add(null);
                        }

                        int lastPosition = postContent.size() - 1;
                        try {
                            posts.add(new Post(
                                    entrys.get(i).getTitle(),
                                    entrys.get(i).getAuthor().getName(),
                                    entrys.get(i).getUpdated(),
                                    postContent.get(0),
                                    postContent.get(lastPosition),
                                    entrys.get(i).getId()
                            ));
                        } catch (NullPointerException e) {
                            posts.add(new Post(
                                    entrys.get(i).getTitle(),
                                    "None",
                                    entrys.get(i).getUpdated(),
                                    postContent.get(0),
                                    postContent.get(lastPosition),
                                    entrys.get(i).getId()
                            ));
                        }

                    }
                }


                @Override
                public void onFailure(Call<Feed> call, Throwable t) {

                }
            });

            return null;
        }
    }

    public static class GetComments extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {

            Call<Feed> call = apiService.getFeed(strings[0]);

            call.enqueue(new Callback<Feed>() {
                @Override
                public void onResponse(Call<Feed> call, Response<Feed> response) {

                    List<Entry> data = response.body().getEntrys();
                    for (int i = 0; i < data.size(); i++) {
                        ParseXML extract = new ParseXML(data.get(i).getContent(), "<div class=\"md\"><p>", "</p>");
                        List<String> commens = extract.start();


                        try {
                            mDetails.add(new Detail(
                                    commens.get(0),
                                    data.get(i).getAuthor().getName(),
                                    data.get(i).getUpdated(),
                                    data.get(i).getId()
                            ));

                        } catch (IndexOutOfBoundsException e) {
                            mDetails.add(new Detail(
                                    "....",
                                    "....",
                                    "....",
                                    "...."
                            ));
                        } catch (NullPointerException e) {
                            mDetails.add(new Detail(
                                    commens.get(0),
                                    "....",
                                    data.get(i).getUpdated(),
                                    data.get(i).getId()

                            ));
                        }
                    }
                }

                @Override
                public void onFailure(Call<Feed> call, Throwable t) {
                }
            });
            return null;
        }
    }








}
