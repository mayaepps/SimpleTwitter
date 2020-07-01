package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";

    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter tweetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        // Find the recycler view
        rvTweets = findViewById(R.id.rvTweets);

        // Initialize the list of tweets in adapter
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(this, tweets);

        // Set up recycler view: layout manager and the adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetsAdapter);

        populateHomeTimeline();
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess " + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    tweets.addAll(Tweet.fromJSONArray(jsonArray));
                    Log.d(TAG, tweets.toString());
                    tweetsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure", throwable);
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
            startActivity(i);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}