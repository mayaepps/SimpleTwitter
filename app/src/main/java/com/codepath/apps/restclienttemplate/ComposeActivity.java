package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.databinding.ActivityComposeBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {


    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 280;
    public static final String INTENT_NAME_TWEET = "tweet";


    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        final ActivityComposeBinding binding = ActivityComposeBinding.inflate(getLayoutInflater());

        // layout of activity is stored in a special property called root
        View view = binding.getRoot();
        setContentView(view);

        Intent i = getIntent();

        if (i.hasExtra(Tweet.class.getSimpleName())) {
            Tweet replyTweet = Parcels.unwrap(i.getParcelableExtra(Tweet.class.getSimpleName()));
            binding.etCompose.setText("@" + replyTweet.getUser().getScreenName() + " ");
        }

        client = TwitterApp.getRestClient(this);

        // Set a click listener on the button
        binding.btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = binding.etCompose.getText().toString();

                if (tweetContent.isEmpty()) { // If they try to publish a tweet without writing anything
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet cannot be empty!", Toast.LENGTH_LONG).show();
                    return;

                } else if (tweetContent.length() > MAX_TWEET_LENGTH) { // If the proposed tweet is over Twitter's max character limit
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(ComposeActivity.this, "Tweeting: " + tweetContent, Toast.LENGTH_LONG).show();

                // Make an API call to Twitter to publish the tweet
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        try {
                            // parse the returned Tweet to get the text of the published tweet
                            Tweet returnedTweet = Tweet.fromJSON(json.jsonObject);

                            sendTweetToTimeline(returnedTweet);

                        } catch (JSONException e) {
                            Log.e(TAG, "Exception parsing Tweet object from JSON tweet returned by Twitter.");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "Failed to publish tweet, API returned status of " + statusCode + " and responded with " + response, throwable);
                    }
                });

            }
        });

    }

    private void sendTweetToTimeline(Tweet returnedTweet) {

        // Create a new intent and put the tweet in it to go back to parent activity
        Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
        // Make returnedTweet model into a Parcel object which the intent can handle
        intent.putExtra(INTENT_NAME_TWEET, Parcels.wrap(returnedTweet));

        // Set result code and bundle data for response
        setResult(RESULT_OK);

        // Close the activity and pass tweet to parent
        finish();
    }

}