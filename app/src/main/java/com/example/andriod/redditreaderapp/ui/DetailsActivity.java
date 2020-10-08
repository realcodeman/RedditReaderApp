package com.example.andriod.redditreaderapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.andriod.redditreaderapp.R;
import com.example.andriod.redditreaderapp.adapter.DetailssAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailsActivity extends AppCompatActivity {

    private static String subRedditName;
    private DetailssAdapter adapter;
    private CheckBox bookmark;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        TextView updated = findViewById(R.id.Updated);
        ImageView thumbnail = findViewById(R.id.Thumbnail);
        TextView title = findViewById(R.id.Title);
        TextView author = findViewById(R.id.Author);
        bookmark = findViewById(R.id.btnPostReply);


        RecyclerView recycler = findViewById(R.id.commentsRecycer);
        DetailActivityViewModel viewModel = new ViewModelProvider(this).get(DetailActivityViewModel.class);


        adapter = new DetailssAdapter();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("favourites");

        Intent incomingIntent = getIntent();
        String postTitle = incomingIntent.getStringExtra("post_title");
        String postAuthor = incomingIntent.getStringExtra("post_author");
        String postUpdated = incomingIntent.getStringExtra("post_updated");
        String postURL = incomingIntent.getStringExtra("post_url");
        String postThumbnailURL = incomingIntent.getStringExtra("post_thumbnail");
        subRedditName = incomingIntent.getStringExtra("post_id");


        viewModel.setViewData(postThumbnailURL, postTitle, postAuthor, postUpdated, subRedditName);
        title.setText(viewModel.mpostTitle);
        author.setText(viewModel.mpostAuthor);
        updated.setText(viewModel.mpostUpdated);
        bookmark.setText(String.format("Bookmark this post: %s", viewModel.mpostID));

        Glide.with(DetailsActivity.this)
                .load(viewModel.mpostThumbnailURL)
                .centerCrop()
                .placeholder(R.drawable.reddit_alien)
                .into(thumbnail);

        viewModel.getComments(postURL);
        viewModel.mcomments.observe(this, comments -> {
            adapter.setData(comments);
            if (comments != null) {
                Log.v("OnChanged", String.valueOf(comments.size()));
            }
            recycler.setAdapter(adapter);
            recycler.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
            recycler.setHasFixedSize(true);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bookmark.isChecked()) {
            String id = myRef.push().getKey();
            assert id != null;
            myRef.child(id).setValue(subRedditName);
        } else {
            DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference("favourites").child(subRedditName);
            DeleteReference.removeValue();
        }
    }
}