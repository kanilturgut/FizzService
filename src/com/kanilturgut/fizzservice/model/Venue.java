package com.kanilturgut.fizzservice.model;

import org.json.JSONObject;

/**
 * Author   : kanilturgut
 * Date     : 21/06/14
 * Time     : 16:42
 */
public class Venue {

    static Venue venue = null;

    public static Venue getInstance() {
        if (venue == null)
            venue = new Venue();

        return venue;
    }

    String id;
    boolean isActive;
    Admin admin;
    String name;
    String hashtag;
    String foursquareId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getFoursquaxreId() {
        return foursquareId;
    }

    public void setFoursquareId(String foursquareId) {
        this.foursquareId = foursquareId;
    }

    public static Venue fromJSON(JSONObject jsonObject, String password) {
        Venue venue = getInstance();

        venue.setId(jsonObject.optString("_id"));
        venue.setActive(jsonObject.optBoolean("isActive"));
        venue.setAdmin(Admin.fromJSON(jsonObject.optJSONObject("admin"), password));
        venue.setName(jsonObject.optString("name"));
        venue.setHashtag(jsonObject.optString("hashtag"));
        venue.setFoursquareId(jsonObject.optString("foursquareId"));

        return venue;
    }
}

