package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    private static final String LOG = "TWEET";
    public static final String TWITTER_DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    public String body;
    public String createdAt;
    public User user;

    // Empty constructer for Parceler library
    public Tweet() { }

    // Takes a JSON tweet and returns a Tweet object
    public static Tweet fromJSON(JSONObject json) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = json.getString("text");
        tweet.createdAt = json.getString("created_at");
        tweet.user = User.fromJSON(json.getJSONObject("user"));

        return tweet;
    }

    // Takes an array of JSON tweets and returns an array of Tweet objects
    public static List<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJSON(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    // Method mostly from https://gist.github.com/nesquena/f786232f5ef72f6e10a7 to parse a relative twitter date
    // For example: getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo() {
        SimpleDateFormat simpleFormat = new SimpleDateFormat(TWITTER_DATE_FORMAT, Locale.ENGLISH);

        //if the date is in an unusual format, the parser should be lenient and try other formats
        simpleFormat.setLenient(true);

        String relativeDate = "";
        try {
            //get the time of the tweet's creation
            long dateMillis = simpleFormat.parse(getCreatedAt()).getTime();
            // given the current date, get how much time has passed since the tweet's creation
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS,
                    // Abbreviate the date so "hours" becomes "hrs" and "minutes" becomes "mins"
                    DateUtils.FORMAT_ABBREV_RELATIVE).toString();

        } catch (ParseException e) {
            Log.e(LOG, "Error when parsing the date: " + e.getStackTrace());
        }
        return relativeDate;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }
}
