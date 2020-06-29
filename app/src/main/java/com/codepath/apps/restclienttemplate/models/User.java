package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public String name;
    public String screenName;
    public String publicImageUrl;



    public static User fromJSON(JSONObject json) {
        User user = new User();
        try {
            user.name = json.getString("name");
            user.screenName = json.getString("screen_name");
            user.publicImageUrl = json.getString("url");

        } catch (JSONException e) {
            Log.e("User", e.toString());
        }
        return user;
    }
}
