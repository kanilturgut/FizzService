package com.kanilturgut.fizzservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kanilturgut.mylib.Logs;

/**
 * Author   : kanilturgut
 * Date     : 18/05/14
 * Time     : 14:19
 */
public class BootFinishedReceiver extends BroadcastReceiver {

    final String TAG = "BootFinishedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Logs.d(TAG, "Boot finished for Service Application");

        Intent i = new Intent(context, MyService.class);
        context.startService(i);
    }
}
