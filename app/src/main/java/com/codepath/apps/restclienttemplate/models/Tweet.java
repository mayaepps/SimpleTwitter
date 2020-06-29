package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {

    public String body;
    public String createdAt;
    public User user;

    // Takes a JSON tweet and returns a Tweet objcect
    public static Tweet fromJSON(JSONObject json) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = json.getString("text");
        tweet.createdAt = json.getString("create_at");
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
}
