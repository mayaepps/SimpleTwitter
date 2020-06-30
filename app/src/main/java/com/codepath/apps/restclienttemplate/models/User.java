package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public String name;
    public String screenName;
    public String publicImageUrl;


    // Takes a JSON user object and returns a User object
    public static User fromJSON(JSONObject json) throws JSONException {
        User user = new User();
        user.name = json.getString("name");
        user.screenName = json.getString("screen_name");
        user.publicImageUrl = json.getString("profile_image_url_https");

        return user;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getPublicImageUrl() {
        return publicImageUrl;
    }

}
