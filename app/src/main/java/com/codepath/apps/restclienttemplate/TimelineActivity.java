package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";
    // Request code used by the activity, can be any number as long as it is unique to the request
    public static final int REQUEST_CODE = 20;

    private SwipeRefreshLayout swipeContainer;

    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter tweetsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbTwitter);
        // Set the Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        client = TwitterApp.getRestClient(this);

        // Find the recycler view and swipe container view
        rvTweets = findViewById(R.id.rvTweets);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        //Instantiate my OnClickListener interface
        TweetsAdapter.OnClickListener clickListener = new TweetsAdapter.OnClickListener() {
            @Override
            public void onReplyClick(int position) {
                // make sure the position exists
                if (position != RecyclerView.NO_POSITION) {
                    // get the movie at the position just clicked on
                    Tweet clickedTweet = tweets.get(position);

                    Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
                    // serialize the clickedTweet using parceler with its name as the key
                    intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(clickedTweet));
                    //then start the activity
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        };

        // Initialize the list of tweets in adapter
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(this, tweets, clickListener);

        // Set up recycler view: layout manager and the adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetsAdapter);

        populateHomeTimeline();

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Call the API to get the new tweets and populate them into the timeline (a refresh)
                populateHomeTimeline();
            }
        });
    }



    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                try {
                    // Clear out old items before appending in the new ones
                    tweetsAdapter.clear();

                    // ...the data has come back, add new items to your adapter...
                    tweets.addAll(Tweet.fromJSONArray(jsonArray));
                    tweetsAdapter.notifyDataSetChanged();

                    // signal that the refresh has completed
                    swipeContainer.setRefreshing(false);

                } catch (JSONException e) {
                    Log.e(TAG, "Exception parsing JSON object", e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "API request failed with status code " + statusCode + " and response " + response, throwable);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // must return true for the menu to be displayed
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.compose) {
            // Compose item has been tapped
            // Navigate to the compose activity
            Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
            startActivityForResult(i, REQUEST_CODE);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    // When the other activity returns with a result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check that the activity is the same ComposeActitvity and the operation succeeded
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Get data from the intent (tweet)
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra(ComposeActivity.INTENT_NAME_TWEET));

            // Update the RecyclerView with this new Tweet
            // Modify data source (tweets): add tweet at the beginning of the list of tweets for the feed
            tweets.add(0, tweet);

            // Notify the adapter that the data has changed at position 0 and it should reevaluate
            tweetsAdapter.notifyItemInserted(0);

            // AdapterView scrolls up to the top of the list of tweets so the newly created tweet is visible
            rvTweets.smoothScrollToPosition(0);


        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}