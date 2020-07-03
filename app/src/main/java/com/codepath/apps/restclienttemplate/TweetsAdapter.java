package com.codepath.apps.restclienttemplate;

import android.content.Context;
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

    // Interface used by the ViewHolder so OnClickListeners can be defined more cleanly
    // rather than defining all the OnClickListeners in the ViewHolder constructer
    public interface OnClickListener {
        void onReplyClick(int position);

    }

    public static final int IMAGE_RADIUS = 30;

    Context context;
    List<Tweet> tweets;
    OnClickListener clickListener;

    public TweetsAdapter(Context context, List<Tweet> tweets, OnClickListener clickListener) {
        this.context = context;
        this.tweets = tweets;
        this.clickListener = clickListener;
    }

    // For each row (view), inflate a layout for a tweet
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

    // Clean all elements of the recycler for the swipe to refresh feature:
    // Don't want to use old data
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
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
        ImageView ivReply;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvRelativeTimestamp = itemView.findViewById(R.id.tvRelativeTimestamp);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            tvName = itemView.findViewById(R.id.tvName);
            ivReply = itemView.findViewById(R.id.ivReply);

            // Listen for clicks on the reply-to-tweet icon
            ivReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onReplyClick(getAdapterPosition()); // Defined in timelineActivity to keep constructor clean
                }
            });
        }

        // Bind the Tweet object to the item_tweet view by setting each of the views using tweet parameter
        public void bind(Tweet tweet) {

            tvBody.setText(tweet.getBody());
            tvScreenName.setText("@" + tweet.getUser().getScreenName());
            tvName.setText(tweet.getUser().getName());
            Glide.with(context).load(tweet.getUser().getPublicImageUrl()).transform(new RoundedCorners(IMAGE_RADIUS)).into(ivProfileImage);
            tvRelativeTimestamp.setText(tweet.getRelativeTimeAgo());

            // Not all tweets have media, but if they do: show and load them into the media image view. If not, disappear ivMedia
            if (tweet.getMediaUrl() != null) {
                ivMedia.setVisibility(View.VISIBLE);
                Glide.with(context).load(tweet.getMediaUrl()).transform(new RoundedCorners(IMAGE_RADIUS)).into(ivMedia);
            } else {
                ivMedia.setVisibility(View.GONE);
            }
        }
    }
}
