package com.example.andriod.redditreaderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.andriod.redditreaderapp.R;
import com.example.andriod.redditreaderapp.model.Post;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsAdapterViewHolder> {

    private List<Post> mPosts;
    private PostsAdapterOnClickHandler mClickHandler;
    private Context ctx;

    public PostsAdapter(Context context, PostsAdapterOnClickHandler ClickHandler){
        this.ctx = context;
        this.mClickHandler = ClickHandler;
    }

public class PostsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView title;
    TextView author;
    TextView date_updated;
    ImageView thumbnailURL;
    public PostsAdapterViewHolder(@NonNull View itemView) {

        super(itemView);
        title = itemView.findViewById(R.id.cardTitle);
        author = itemView.findViewById(R.id.cardAuthor);
        date_updated = itemView.findViewById(R.id.cardUpdated);
        thumbnailURL = itemView.findViewById(R.id.scheduleImage);
    itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    });
    }
    @Override
    public void onClick(View view) {
        int adapterPosition = getAdapterPosition();
        mClickHandler.onClick(adapterPosition);
    }

}

    @NonNull
    @Override
    public PostsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.post_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent,false);
    return new PostsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapterViewHolder holder, int position) {

        holder.title.setText(mPosts.get(position).getTitle());
        holder.author.setText(mPosts.get(position).getAuthor());
        holder.date_updated.setText(mPosts.get(position).getDate_updated());

        Glide.with(ctx)
                .load(mPosts.get(position).getThumbnailURL())
                .centerCrop()
                .placeholder(R.drawable.reddit_alien)
                .into(holder.thumbnailURL);
    }

    @Override
    public int getItemCount() {
        if (null == mPosts){
        return 0;
    }
        return mPosts.size();
    }

    public interface PostsAdapterOnClickHandler {
        void onClick(int adapterPosition);
    }

    public void setData(List<Post> posts){
        if (mPosts!=null){
            mPosts.clear();
        }
        mPosts = posts;
        notifyDataSetChanged();
    }

}
