package com.kanilturgut.fizzservice;

import android.content.Context;
import android.os.AsyncTask;

import com.kanilturgut.fizzservice.model.Venue;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Author   : kanilturgut
 * Date     : 20/06/14
 * Time     : 20:13
 */
public class LoginTask extends AsyncTask<String, Void, String> {

    final String TAG = "LoginTask";
    Context context;
    String email, password;

    public LoginTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        this.email = strings[0];
        this.password = strings[1];

        JSONObject loginObject = new JSONObject();
        try {
            loginObject.put("email", email);
            loginObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            cancel(true);
        }

        try {
            HttpResponse httpResponse = Requests.post("/auth", loginObject.toString());
            if (Requests.checkStatusCode(httpResponse, HttpStatus.SC_OK))
                return Requests.readResponse(httpResponse);
            else
                return null;

        } catch (IOException e) {
            e.printStackTrace();
            cancel(true);
        }

        return null;
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();

        //TODO ne yapılacak ?
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Venue.fromJSON(jsonObject, password);

                PubnubController.getInstance(context).subscribeToChannel();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
