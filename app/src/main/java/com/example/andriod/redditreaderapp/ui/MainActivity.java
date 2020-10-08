package com.example.andriod.redditreaderapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.andriod.redditreaderapp.AnalyticsApplication;
import com.example.andriod.redditreaderapp.R;
import com.example.andriod.redditreaderapp.RedditWidget;
import com.example.andriod.redditreaderapp.adapter.PostsAdapter;
import com.example.andriod.redditreaderapp.model.Post;
import com.example.andriod.redditreaderapp.remote.RemoteDataSource;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PostsAdapter.PostsAdapterOnClickHandler {

    List<Post> mposts = new ArrayList<>();
    RecyclerView recycler;
    private PostsAdapter adapter;
    Button searchFeeds;
    EditText feedName;
    private MainActivityViewModel viewModel;
    String feedID;
    int currentSelection;
    private String BUNDLE_RECYCLER_LAYOUT = "com.android.MainActivity.Layout";
    Parcelable savedRecyclerLayoutState;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchFeeds = findViewById(R.id.btnRefreshFeed);
        recycler = findViewById(R.id.recycle);
        feedName = findViewById(R.id.etFeedName);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        String putFeed = "Enter a feed name";
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();





        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recycler.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            recycler.setAdapter(adapter);
            recycler.setLayoutManager(new LinearLayoutManager(this));
            recycler.setHasFixedSize(true);
        }


        adapter = new PostsAdapter(this,this);
        recycler.setAdapter(adapter);
        Log.v("Posts",mposts.toString());

        searchFeeds.setOnClickListener((View.OnClickListener) view -> {
            if(!feedName.getText().toString().isEmpty()){

                 feedID =feedName.getText().toString();

                SharedPreferences.Editor editor = getSharedPreferences("Title", MODE_PRIVATE).edit();
                editor.putString("feedID", feedID);
                editor.apply();

                Context context = MainActivity.this;
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.reddit_widget);
                ComponentName thisWidget = new ComponentName(context, RedditWidget.class);

                remoteViews.setTextViewText(R.id.appwidget_text, feedID);
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);

                viewModel.getPosts(feedName.getText().toString());
                viewModel.mposts.observe(MainActivity.this, (Observer<List<Post>>) posts -> {
                mposts = posts;
                adapter.setData(posts);
                recycler.setAdapter(adapter);
                recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recycler.setHasFixedSize(true);
            });
            }else {
                Toast.makeText(MainActivity.this, putFeed, Toast.LENGTH_SHORT).show();
            }

        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        mposts = viewModel.mposts.getValue();
        adapter.setData(mposts);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setHasFixedSize(true);
        Log.v("OnResume","We're here");
        Log.v("OnResume",""+viewModel.mposts.getValue());


    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recycler.getLayoutManager().onSaveInstanceState());
        outState.putInt("sort", currentSelection);
    }

    @Override
    public void onClick(int position) {

        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra("url", mposts.get(position).getPostURL());
        intent.putExtra("thumbnail", mposts.get(position).getThumbnailURL());
        intent.putExtra("title", mposts.get(position).getTitle());
        intent.putExtra("author", mposts.get(position).getAuthor());
        intent.putExtra("updated", mposts.get(position).getDate_updated());
        intent.putExtra("post_id", feedID);
        startActivity(intent);
    }
}