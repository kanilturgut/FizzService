package com.kanilturgut.fizzservice;

import android.content.Context;
import android.content.SharedPreferences;
import com.kanilturgut.mylib.Logs;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author   : kanilturgut
 * Date     : 20/05/14
 * Time     : 11:17
 */
public class PubnubController {

    Context context;
    static PubnubController pubnubController = null;
    static Pubnub pubnub = null;
    final String TAG = "PubnubController";
    final String PUBLISH_KEY = "pub-c-79ce4f35-20dd-4972-9f8c-8f9d3a4dbe59";
    final String SUBSCRIBE_KEY = "sub-c-2706dfc2-f87a-11e3-bacb-02ee2ddab7fe";
    String CHANNEL = "fizz";

    String INSTRUCTION = "instruction";

    final int INSTALL = 1;
    final int UNINSTALL = 2;
    final int UNINSTALL_WITH_CACHE = 3;
    final int LAUNCH_APP = 4;
    final int SHOW_PROCESS = 5;
    final int KILL = 6;
    final int LOGCAT = 7;
    final int LOGCAT_CLEAR = 8;
    final int CHANGE_DOWNLOAD_URL = 9;
    final int SHOW_DOWNLOAD_URL = 10;
    final int DOWNLOAD_NEW_APK = 11;

    private PubnubController(Context context) {
        pubnub = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY);
        this.context = context;

        SharedPreferences sharedPreferences = context.getSharedPreferences("Pubnup", Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("uuid", null);
        if (uuid == null || uuid.length() == 0) {
            uuid = pubnub.uuid();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uuid", uuid);
            editor.apply();
        }

        pubnub.setUUID(uuid);
    }

    public static PubnubController getInstance(Context context) {
        if (pubnubController == null)
            pubnubController = new PubnubController(context);

        return pubnubController;
    }

    public void subscribeToChannel() {

        if (pubnub != null && CHANNEL != null) {
            try {
                pubnub.subscribe(CHANNEL, new Callback() {

                    @Override
                    public void connectCallback(String s, Object o) {
                        super.connectCallback(s, o);
                        Logs.i(TAG, "SUBSCRIBE : CONNECT on channel : " + CHANNEL + " : " + o.toString());
                    }

                    @Override
                    public void disconnectCallback(String s, Object o) {
                        super.disconnectCallback(s, o);
                        Logs.i(TAG, "SUBSCRIBE : DISCONNECT on channel : " + CHANNEL + " : " + o.toString());
                    }

                    @Override
                    public void errorCallback(String s, PubnubError pubnubError) {
                        super.errorCallback(s, pubnubError);
                        Logs.e(TAG, "SUBSCRIBE : ERROR on channel : " + CHANNEL + " : " + pubnubError.getErrorString());
                    }

                    @Override
                    public void successCallback(String s, Object o) {

                        Logs.i(TAG, "SUBSCRIBE : SUCCESS_CALLBACK on channel " + CHANNEL + " : " + o.toString());

                        try {
                            JSONObject pubnupResponse = new JSONObject(o.toString());

                            if (pubnupResponse.has(INSTRUCTION)) {
                                int instructionNumber = pubnupResponse.getInt(INSTRUCTION);

                                switch (instructionNumber) {
                                    case INSTALL:
                                        ADB.getInstance(context).install(instructionNumber);
                                        break;
                                    case UNINSTALL:
                                        ADB.getInstance(context).uninstall(instructionNumber);
                                        break;
                                    case UNINSTALL_WITH_CACHE:
                                        ADB.getInstance(context).uninstallWithCache(instructionNumber);
                                        break;
                                    case LAUNCH_APP:
                                        ADB.getInstance(context).launchApp(instructionNumber);
                                        break;
                                    case SHOW_PROCESS:
                                        ADB.getInstance(context).showProcess(instructionNumber);
                                        break;
                                    case KILL:
                                        ADB.getInstance(context).kill(instructionNumber);
                                        break;
                                    case LOGCAT:
                                        ADB.getInstance(context).logcat(instructionNumber);
                                        break;
                                    case LOGCAT_CLEAR:
                                        ADB.getInstance(context).logcatClear(instructionNumber);
                                        break;
                                    case CHANGE_DOWNLOAD_URL:
                                        ADB.getInstance(context).changeDownloadURL(instructionNumber, pubnupResponse.getString("url"));
                                        break;
                                    case SHOW_DOWNLOAD_URL:
                                        ADB.getInstance(context).showDownloadURL(instructionNumber);
                                        break;
                                    case DOWNLOAD_NEW_APK:
                                        ADB.getInstance(context).downloadNewAPK(instructionNumber);
                                        break;
                                }
                            }

                        } catch (JSONException e) {
                            Logs.e(TAG, "JSONException on successCallback", e);
                            //TODO exception olursa bir tane elle yazdığımız post gelsin
                        }
                    }
                });
            } catch (PubnubException e) {
                Logs.e(TAG, "ERROR on attaching channel " + CHANNEL, e);
            }
        }
    }
}
