package com.kanilturgut.fizzservice.task;

import android.content.Context;
import android.os.AsyncTask;

import com.kanilturgut.fizzservice.ADB;
import com.kanilturgut.fizzservice.Venue;
import com.kanilturgut.mylib.Logs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author   : kanilturgut
 * Date     : 24/07/14
 * Time     : 09:06
 */
public class DownloadAPKTask extends AsyncTask<String, Void, String> {

    Context context;

    public DownloadAPKTask(Context context) {
        this.context = context;
    }

    private final String TAG = DownloadAPKTask.class.getSimpleName();

    @Override
    protected String doInBackground(String... sUrl) {

        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }


            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(ADB.FIZZ_DOWNLOAD_TARGET);

            byte data[] = new byte[4096];
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }

                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {

        SendInstructionResultTask sendInstructionResultTask;

        if (s != null) {
            Logs.e(TAG, "Download ERROR: " + s);
            sendInstructionResultTask = new SendInstructionResultTask(Venue.getInstance().getId(), 10, "Download ERROR: " + s);
        } else {
            Logs.d(TAG, "APK Downloaded");
            ADB.getInstance(context).copyAPKToSystem();
            sendInstructionResultTask = new SendInstructionResultTask(Venue.getInstance().getId(), 10, "APK Downloaded");
        }

        sendInstructionResultTask.execute();
    }
}
