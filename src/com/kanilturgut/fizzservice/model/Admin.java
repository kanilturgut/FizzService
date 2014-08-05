package com.kanilturgut.fizzservice.model;

import org.json.JSONObject;

/**
 * Author   : kanilturgut
 * Date     : 22/06/14
 * Time     : 18:59
 */
public class Admin {

    static Admin admin = null;

    public static Admin getInstance() {
        if (admin == null)
            admin = new Admin();

        return admin;
    }

    String id;
    String email;
    String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Admin fromJSON(JSONObject jsonObject, String password) {

        Admin admin = getInstance();
        admin.setId(jsonObject.optString("_id"));
        admin.setEmail(jsonObject.optString("email"));
        admin.setPassword(password);

        return admin;
    }
}
