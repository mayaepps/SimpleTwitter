package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    // For each row, inflate a layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the elements
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = tweets.get(position);

        // Bind the tweet with ViewHolder
        holder.bind(tweet);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvRelativeTimestamp;
        ImageView ivMedia;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvRelativeTimestamp = itemView.findViewById(R.id.tvRelativeTimestamp);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            tvName = itemView.findViewById(R.id.tvName);
        }

        // Bind the Tweet object to the item_tweet view by setting each of the views using tweet parameter
        public void bind(Tweet tweet) {
            tvBody.setText(tweet.getBody());
            tvScreenName.setText("@" + tweet.getUser().getScreenName());
            tvName.setText(tweet.getUser().getName());
            Glide.with(context).load(tweet.getUser().getPublicImageUrl()).transform(new RoundedCorners(30)).into(ivProfileImage);
            tvRelativeTimestamp.setText(tweet.getRelativeTimeAgo());
            if (tweet.getMediaUrl() != null) {
                ivMedia.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.getMediaUrl()).transform(new RoundedCorners(30)).into(ivMedia);
            } else {
                ivMedia.setVisibility(View.GONE);
            }
        }
    }

    // Clean all elements of the recycler for the swipe to refresh feature:
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

}
