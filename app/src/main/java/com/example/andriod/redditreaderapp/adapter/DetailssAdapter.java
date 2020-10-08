package com.example.andriod.redditreaderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andriod.redditreaderapp.R;
import com.example.andriod.redditreaderapp.model.Detail;

import java.util.List;

public class DetailssAdapter extends RecyclerView.Adapter<DetailssAdapter.CommentsViewHolder> {

    private List<Detail> mDetails;

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.detail_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent,false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {

        holder.author.setText(mDetails.get(position).getAuthor());
        holder.date_updated.setText(mDetails.get(position).getUpdated());
        holder.comment.setText(mDetails.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {

        TextView comment;
        TextView author;
        TextView date_updated;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.CommentTv);
            author = itemView.findViewById(R.id.AuthorTv);
            date_updated = itemView.findViewById(R.id.DateAddedTv);

        }

    }
    public void setData(List<Detail> details){
        if (mDetails !=null){
            mDetails.clear();
        }
        mDetails = details;
        notifyDataSetChanged();
    }
}
