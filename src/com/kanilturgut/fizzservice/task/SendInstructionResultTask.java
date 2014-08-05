package com.kanilturgut.fizzservice.task;

import android.os.AsyncTask;
import com.kanilturgut.fizzservice.Requests;
import com.kanilturgut.mylib.Logs;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Author   : kanilturgut
 * Date     : 23/07/14
 * Time     : 12:31
 */
public class SendInstructionResultTask extends AsyncTask<Void, Void, Void> {

    final String TAG = SendInstructionResultTask.class.getSimpleName();

    String venueId;
    int instruction;
    String log;

    public SendInstructionResultTask(String venueId, int instruction, String log) {
        this.venueId = venueId;
        this.instruction = instruction;
        this.log = log;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("venueId", venueId);
            jsonObject.put("instruction", instruction);
            jsonObject.put("log", log);

            HttpResponse httpResponse = Requests.post("/remote/sendInstructionLog", jsonObject.toString());
            if (Requests.checkStatusCode(httpResponse, HttpStatus.SC_OK))
                Logs.d(TAG, "SUCCESS on SendInstructionResultTask");
            else
                Logs.d(TAG, "FAILED on SendInstructionResultTask");

        } catch (JSONException e) {
            Logs.e(TAG, "ERROR on SendInstructionResultTask doInBackground", e);
        } catch (IOException e) {
            Logs.e(TAG, "ERROR on SendInstructionResultTask doInBackground", e);
        }

        return null;
    }
}
