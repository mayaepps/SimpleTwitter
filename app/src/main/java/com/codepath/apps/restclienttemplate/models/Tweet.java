package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {

    public String body;
    public String createdAt;
    public User user;


    public static Tweet fromJSON(JSONObject json) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = json.getString("text");
            tweet.createdAt = json.getString("create_at");
            tweet.user = User.fromJSON(json.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

}
